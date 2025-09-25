package es.ull.project.application.service.workflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;

import es.ull.project.application.repository.ExecutionRepository;
import es.ull.project.application.repository.WorkFlowRepository;
import es.ull.project.application.usecase.workflow.WorkFlowExecuteUseCase;
import es.ull.project.domain.entity.Execution;
import es.ull.project.domain.entity.WorkFlow;
import es.ull.project.domain.enumerate.ExecuteStatus;
import es.ull.project.domain.enumerate.NodeContentType;
import es.ull.project.domain.valueobject.Conditional;
import es.ull.project.domain.valueobject.File;
import es.ull.project.domain.valueobject.FileName;
import es.ull.project.domain.valueobject.Link;
import es.ull.project.domain.valueobject.Node;
import es.ull.project.domain.valueobject.NodeName;
import es.ull.project.domain.valueobject.Operation;
import es.ull.utils.collection.UllImmutablePair;
import es.ull.project.domain.valueobject.NodeExecuteData;

public class WorkFlowExecuteService implements WorkFlowExecuteUseCase {

    @Autowired
    private WorkFlowRepository workflowRepository;

    @Autowired
    private ExecutionRepository executionRepository;

    private static final String SHARED_CONTAINER_DIR = System.getenv("SHARED_CONTAINER_PATH");
    private static final String HOST_FILES_PATH_HOST = System.getenv("HOST_FILES_PATH");
    private static final String CONDITIONAL_NODE_DOCKER_IMAGE = "fdavidhernandez/conditional-execute-code:latest"; // DOCKER-HUB
                                                                                                                   // IMAGE

    private static final String ERROR_WORKFLOW_NULL = "Workflow cannot be null";
    private static final String ERROR_WORKFLOW_NO_NODES = "Workflow must have at least one node";
    private static final String ERROR_WORKFLOW_NO_FIRST_NODE = "Workflow must have a first node defined";
    private static final String ERROR_WORKFLOW_NOT_ENABLED = "Workflow is not enabled to execute";
    private static final String ERROR_FIRST_NODE_NOT_FILE = "First node must be of type FILE to start execution";
    private static final String ERROR_WORKFLOW_CYCLIC = "Workflow is cyclic, cannot execute";

    private static final String INPUT_FILE_NAME = "input.txt";

    // -------------------------------------------------------------VALIDATION------------------------------------------------------------//
    /**
     * @brief Validates if the workflow is acyclic with DFS algorithm.
     * @param nodes
     * @return
     */
    public boolean isAcyclic(List<Node> nodes) {
        Map<NodeName, Node> nodeMap = new HashMap<>();
        for (Node node : nodes) {
            nodeMap.put(node.getName(), node);
        }

        // Initialize visited and recursion stack maps
        Map<NodeName, Boolean> visited = new HashMap<>();
        Map<NodeName, Boolean> inRecursion = new HashMap<>();

        for (NodeName nodeName : nodeMap.keySet()) {
            visited.put(nodeName, false);
            inRecursion.put(nodeName, false);
        }

        for (Node node : nodes) {
            if (!visited.get(node.getName())) {
                if (dfsDetectCycle(node, nodeMap, visited, inRecursion)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @brief Depth-First Search (DFS) to detect cycles in the graph.
     * @param currentNode
     * @param nodeMap
     * @param visited
     * @param inRecursion
     * @return
     */
    private boolean dfsDetectCycle(Node currentNode,
            Map<NodeName, Node> nodeMap,
            Map<NodeName, Boolean> visited,
            Map<NodeName, Boolean> inRecursion) {
        NodeName nodeName = currentNode.getName();
        visited.put(nodeName, true);
        inRecursion.put(nodeName, true);

        // Explore all links from the current node
        for (Link link : currentNode.getLinks()) {
            if (link.getCondition() == true) { // if link is active
                NodeName neighborName = link.getFinalNode();
                if (!visited.get(neighborName)) {
                    // If the neighbor node has not been visited, recursively visit it
                    if (dfsDetectCycle(nodeMap.get(neighborName), nodeMap, visited, inRecursion)) {
                        return true;
                    }
                } else if (inRecursion.get(neighborName)) {
                    return true;
                }
            }
        }

        inRecursion.put(nodeName, false);
        return false;
    }

    /**
     * @brief Validates the workflow before execution.
     * @param workFlow
     */
    private Optional<UllImmutablePair<ExecuteStatus, String>> validateWorkFlowWithStatus(WorkFlow workFlow) {
        if (workFlow == null) {
            return Optional.of(new UllImmutablePair<>(ExecuteStatus.FAILED, ERROR_WORKFLOW_NULL));
        }
        if (workFlow.getNodes() == null || workFlow.getNodes().isEmpty()) {
            return Optional.of(new UllImmutablePair<>(ExecuteStatus.FAILED, ERROR_WORKFLOW_NO_NODES));
        }
        if (workFlow.getFirstNode() == null) {
            return Optional.of(new UllImmutablePair<>(ExecuteStatus.FAILED, ERROR_WORKFLOW_NO_FIRST_NODE));
        }
        if (workFlow.getFirstNode().getType() != NodeContentType.FILE) {
            return Optional.of(new UllImmutablePair<>(ExecuteStatus.FAILED, ERROR_FIRST_NODE_NOT_FILE));
        }
        if (!workFlow.isEnabled()) {
            return Optional.of(new UllImmutablePair<>(ExecuteStatus.NOT_ENABLED, ERROR_WORKFLOW_NOT_ENABLED));
        }
        if (!isAcyclic(workFlow.getNodes())) {
            return Optional.of(new UllImmutablePair<>(ExecuteStatus.FAILED, ERROR_WORKFLOW_CYCLIC));
        }

        return Optional.empty(); // No errors, no Optional content:
    }

    // -------------------------------------------------------------EXECUTE------------------------------------------------------------//
    @Override
    public Optional<Execution> executeWorkFlow(UUID workFlowId, Map<NodeName, URI> pendingUriFiles) {
        Instant startTotal = Instant.now();

        // 1- Get workflow and create execution result
        List<NodeExecuteData> executedNodes = new ArrayList<>();
        int operationExecutedCount = 0;
        List<File> outputFiles = new ArrayList<>();
        WorkFlow workFlow = workflowRepository.fetchById(workFlowId);
        if (workFlow == null) {
            return Optional.empty();
        }

        Execution result = new Execution(workFlow.getName(),
                ExecuteStatus.EXECUTING,
                "executing workflow: " + workFlow.getName().getName(),
                outputFiles,
                executedNodes, Duration.between(startTotal, startTotal), operationExecutedCount);
        
        Execution executionSaved = executionRepository.save(result);
        System.out.println("Execution saved: " + executionSaved);

        // 2- Validate the workflow
        Optional<UllImmutablePair<ExecuteStatus, String>> validationResult = validateWorkFlowWithStatus(workFlow);

        if (validationResult.isPresent()) {
            UllImmutablePair<ExecuteStatus, String> error = validationResult.get();

            Instant endTotal = Instant.now();
            Duration totalExecutionTime = Duration.between(startTotal, endTotal);
            result = new Execution(
                    workFlow.getName(),
                    validationResult.get().getFirst(),
                    error.getSecond(),
                    outputFiles,
                    executedNodes, totalExecutionTime, operationExecutedCount);

        } else {

            // 3- Create execution folder
            String workflowNameSafe = workFlow.getName().getName().replaceAll("[^a-zA-Z0-9-_\\.]", "_");
            String timestamp = String.valueOf(System.currentTimeMillis());
            String executionFolder = SHARED_CONTAINER_DIR + "/" + workflowNameSafe + "-" + timestamp;
            String hostExecutionFolder = HOST_FILES_PATH_HOST + "/" + workflowNameSafe + "-" + timestamp;

            java.io.File execDirFile = new java.io.File(executionFolder);
            if (!execDirFile.exists()) {
                boolean created = execDirFile.mkdirs();
                if (!created) {
                    System.out.println("[DEBUG] Execution directory not created: " + execDirFile);
                    return Optional.empty(); // o manejar error creando carpeta
                }
            }

            try {
                // 4 - Update nodes with pending URI files
                List<Node> updatedNodes = new ArrayList<>();

                for (Node node : workFlow.getNodes()) {
                    if (node.getType() == NodeContentType.FILE) {
                        File originalFile = (File) node.getContent();

                        // If the file node does not have a URI, we will assign it from pendingUriFiles
                        Optional<URI> optUri = originalFile.getUri();
                        if (!optUri.isPresent() || optUri.get().toString().isBlank()) {
                            NodeName nodeName = node.getName();

                            if (!pendingUriFiles.containsKey(nodeName)) {
                                throw new URISyntaxException("Missing URI for node: " + nodeName.getName(),
                                        "URI not found in pending files");
                            }

                            URI uri = pendingUriFiles.get(nodeName);

                            if (uri == null || uri.toString().isBlank()) {
                                throw new URISyntaxException("Invalid URI for node: " + nodeName.getName(),
                                        "URI is null or blank");
                            }

                            File updatedFile = new File(originalFile.getName(), uri);

                            Node updatedNode = new Node(
                                    node.getName(),
                                    node.getXPosition(),
                                    node.getYPosition(),
                                    node.getColor(),
                                    node.getIcon(),
                                    updatedFile,
                                    node.getLinks());

                            updatedNodes.add(updatedNode);

                        } else {
                            updatedNodes.add(node);
                        }

                    } else {
                        updatedNodes.add(node);
                    }
                }

                workFlow = workFlow.setNodes(updatedNodes);

                // 5- Get File for the first node
                Node currentNode = workFlow.getFirstNode();

                String inputPath = executionFolder + "/" + INPUT_FILE_NAME;

                System.out.println("[DEBUG] Input file path: " + inputPath);

                if (currentNode.getType() == NodeContentType.FILE) {
                    File nodeFile = (File) currentNode.getContent();

                    if (nodeFile.getUri().isPresent()) {
                        URI remoteUri = nodeFile.getUri().get();

                        if (remoteUri.toString().isBlank()) {
                            throw new RuntimeException("URI empty for node: " + currentNode.getName().getName());
                        }

                        System.out.println("[DEBUG] remoteUri: " + remoteUri);
                        downloadFileFromURL(remoteUri.toString(), inputPath);

                        java.io.File f = new java.io.File(inputPath);
                        System.out.println("[DEBUG] ¿Se creó input.txt? " + f.exists());
                        System.out.println("[DEBUG] ¿Es un archivo? " + f.isFile());
                        System.out.println("[DEBUG] Tamaño del archivo: " + f.length() + " bytes");
                        System.out.println("[DEBUG] Ruta absoluta: " + f.getAbsolutePath());

                    } else {
                        throw new RuntimeException("File node not have valid URI: " + currentNode.getName().getName());
                    }

                } else {
                    throw new RuntimeException(ERROR_FIRST_NODE_NOT_FILE + ": " + currentNode.getName().getName());
                }

                // 6-Execute the workflow
                List<String> finalOutputFiles = new ArrayList<>();
                Queue<UllImmutablePair<Node, String>> queue = new LinkedList<>();
                queue.add(new UllImmutablePair<>(currentNode, inputPath));

                while (!queue.isEmpty()) {
                    UllImmutablePair<Node, String> current = queue.poll();
                    currentNode = current.getFirst();
                    String currentInputPath = current.getSecond();
                    String safeName = currentNode.getName().getName().replace(" ", "_");
                    String tempOutputPath = executionFolder + "/output_" + safeName + ".txt";
                    System.out.println("[DEBUG ]tempOutputPath: " + tempOutputPath);

                    // ----------------------------------------OPERATION-------------------------------------------//
                    if (currentNode.getType() == NodeContentType.OPERATION) {
                        Operation operation = (Operation) currentNode.getContent();
                        String dockerImage = operation.getDockerImage().getValue();

                        // execute node
                        Instant startNode = Instant.now();
                        runDockerProcessFromHubImage(dockerImage, hostExecutionFolder, executionFolder,
                                currentInputPath, tempOutputPath);
                        Instant endNode = Instant.now();
                        Duration durationNode = Duration.between(startNode, endNode);

                        executedNodes.add(new NodeExecuteData(currentNode.getName(), durationNode));
                        operationExecutedCount++;

                        currentInputPath = tempOutputPath;

                        // ----------------------------------------CONDITIONAL-------------------------------------------//
                    } else if (currentNode.getType() == NodeContentType.CONDITIONAL) {
                        Conditional conditional = (Conditional) currentNode.getContent();

                        // execute node
                        Instant startNode = Instant.now();
                        runConditionalDockerProcessFromHubImage(CONDITIONAL_NODE_DOCKER_IMAGE, hostExecutionFolder,
                                executionFolder, currentInputPath,
                                tempOutputPath, conditional.getExecutionCode());
                        Instant endNode = Instant.now();
                        Duration durationNode = Duration.between(startNode, endNode);

                        executedNodes.add(new NodeExecuteData(currentNode.getName(), durationNode));

                        // Get the result from the output file
                        String resultContent = Files.readString(Paths.get(tempOutputPath)).trim().toLowerCase();

                        if (!resultContent.equals("true") && !resultContent.equals("false")) {
                            throw new RuntimeException(
                                    "Output from conditional node is not 'true' or 'false': " + resultContent);
                        }

                        boolean conditionResult = Boolean.parseBoolean(resultContent);

                        // Add nodes to the queue based on the condition result
                        Map<NodeName, Boolean> linksConditions = conditional.getLinksConditions();
                        List<Link> validLinks = currentNode.getLinks().stream()
                                .filter(link -> Boolean.TRUE.equals(link.getCondition()))
                                .toList();

                        for (Map.Entry<NodeName, Boolean> entry : linksConditions.entrySet()) {

                            // Check if link is into valid links
                            NodeName targetNode = entry.getKey();
                            boolean isInValidLinks = validLinks.stream()
                                    .anyMatch(link -> link.getFinalNode().equals(targetNode));

                            // Check if the condition result matches the link condition
                            currentNode.getLinks();
                            if (isInValidLinks && entry.getValue() == conditionResult) {

                                // Find node in the workflow
                                NodeName nextNodeName = entry.getKey();
                                Node nextNode = null;

                                for (Node n : workFlow.getNodes()) {
                                    if (n.getName().equals(nextNodeName)) {
                                        nextNode = n;
                                        break;
                                    }
                                }

                                if (nextNode != null) {
                                    queue.add(new UllImmutablePair<>(nextNode, currentInputPath));

                                } else {
                                    throw new RuntimeException(
                                            "[DEBUG] Nodo no encontrado en workflow: " + nextNodeName.getName());
                                }
                            }
                        }

                        continue; // Skip to the next iteration of the loop

                        // ----------------------------------------FILE-------------------------------------------//
                    } else if (currentNode.getType() == NodeContentType.FILE) {
                        // NOT PROCESSED, just pass the file to the next node
                    }

                    // Check if is final node
                    List<Link> validLinks = currentNode.getLinks().stream()
                            .filter(link -> Boolean.TRUE.equals(link.getCondition()))
                            .toList();

                    if (validLinks.isEmpty()) {
                        finalOutputFiles.add(currentInputPath);

                        FileName fileName = new FileName(safeName);
                        System.out.println("---[DEBUG ]URI.create(tempOutputPath)---");
                        System.out.println("[DEBUG ]URI.create(tempOutputPath): "
                                + URI.create(hostExecutionFolder + "/output_" + safeName + ".txt"));
                        outputFiles
                                .add(new File(fileName,
                                        URI.create(hostExecutionFolder + "/output_" + safeName + ".txt")));
                    }

                    // Add nodes to the queue
                    for (Link link : validLinks) {
                        NodeName nextNodeName = link.getFinalNode();
                        Optional<Node> nextNodeOpt = workFlow.getNodes().stream()
                                .filter(n -> n.getName().equals(nextNodeName))
                                .findFirst();
                        if (nextNodeOpt.isPresent()) {
                            Node nextNode = nextNodeOpt.get();
                            queue.add(new UllImmutablePair<>(nextNode, currentInputPath));
                        } else {
                            System.out.println("[DEBUG] Nodo no encontrado en workflow: " + nextNodeName.getName());
                        }
                    }

                }

                // Delete all files except the final output files
                List<String> fileNamesToKeep = finalOutputFiles.stream()
                        .map(path -> new java.io.File(path).getName())
                        .toList();
                cleanFolderExcept(executionFolder, fileNamesToKeep);

                // 7- Create the result ExecuteData
                Instant endTotal = Instant.now();
                Duration totalExecutionTime = Duration.between(startTotal, endTotal);

                result = new Execution(
                        workFlow.getName(),
                        ExecuteStatus.SUCCESS,
                        "Execution completed successfully",
                        outputFiles,
                        executedNodes, totalExecutionTime, operationExecutedCount);

            } catch (URISyntaxException e) {
                Instant endTotal = Instant.now();
                Duration totalExecutionTime = Duration.between(startTotal, endTotal);
                result = new Execution(
                        workFlow.getName(),
                        ExecuteStatus.BAD_URI_REQUEST,
                        "Bad URI request: " + e.getMessage(),
                        outputFiles,
                        executedNodes, totalExecutionTime, operationExecutedCount);
            } catch (Exception e) {
                Instant endTotal = Instant.now();
                Duration totalExecutionTime = Duration.between(startTotal, endTotal);
                result = new Execution(
                        workFlow.getName(),
                        ExecuteStatus.FAILED,
                        "Execution Error: " + e.getMessage(),
                        outputFiles,
                        executedNodes, totalExecutionTime, operationExecutedCount);

            }
        }

        return Optional.of(executionRepository.update(executionSaved.getId(), result));
    }

    // ------------------------------------------------------------RUN-OPERATIONS------------------------------------------------------------//
    /**
     * @brief Runs a Docker container from a Docker Hub image.
     * @param imageName
     * @param hostFilesPath
     * @param directoryPath
     * @param inputPath
     * @param outputPath
     * @throws IOException
     * @throws InterruptedException
     */
    private void runDockerProcessFromHubImage(String imageName, String hostFilesPath, String directoryPath,
            String inputPath, String outputPath)
            throws IOException, InterruptedException {
        System.out.println("[DEBUG] -----------------> OPERATION: Running Docker Hub image: " + imageName);
        runDockerProcess(imageName, hostFilesPath, directoryPath, inputPath, outputPath);
        removeDockerImage(imageName);
    }

    /**
     * @brief Runs a Docker container with the specified image and input/output
     *        paths.
     * @param imageName
     * @param hostFilesPath
     * @param directoryPath
     * @param inputPath
     * @param outputPath
     * @throws IOException
     * @throws InterruptedException
     */
    private void runDockerProcess(String imageName, String hostFilesPath, String directoryPath, String inputPath,
            String outputPath)
            throws IOException, InterruptedException {
        System.out.println("[CHECK] Archivo de entrada (host): " + inputPath);
        System.out.println("[CHECK] Archivo de salida esperado (host): " + outputPath);

        java.io.File inputFile = new java.io.File(inputPath);
        java.io.File outputFile = new java.io.File(outputPath);

        String sharedHostDir = inputFile.getParent();

        // DEBUG
        System.out.println("[DEBUG] Input file exists? " + inputFile.exists());
        System.out.println("[DEBUG] Shared host dir: " + sharedHostDir);

        List<String> command = List.of(
                "/usr/bin/docker", "run", "--rm",
                "-v", hostFilesPath + ":" + directoryPath,
                imageName,
                directoryPath + "/" + (new java.io.File(inputPath)).getName(),
                directoryPath + "/" + (new java.io.File(outputPath)).getName());

        // DEBUG: Print the Docker command
        System.out.println("[DEBUG] Docker command:");
        command.forEach(arg -> System.out.print(arg + " "));
        System.out.println();

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);
        Process process = builder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[DOCKER] " + line);
            }
        }
        int exitCode = process.waitFor();
        System.out.println("[DEBUG] Docker process exited with code: " + exitCode);
        if (exitCode != 0) {
            throw new RuntimeException("Docker container " + imageName + " failed with exit code " + exitCode);
        }
        // Verify that the output file was created
        if (!outputFile.exists()) {
            throw new RuntimeException("Output file was not created: " + outputPath);
        } else {
            System.out.println("[DEBUG] Output file created: " + outputPath);
        }
    }

    // ------------------------------------------------------------RUN-CONDITIONAL-NODE------------------------------------------------------------//

    /**
     * @brief Runs a Docker container from a Docker Hub image with dynamic Python
     *        code execution.
     * @param imageName
     * @param hostFilesPath
     * @param directoryPath
     * @param inputPath
     * @param outputPath
     * @param pythonCode
     * @throws IOException
     * @throws InterruptedException
     */
    private void runConditionalDockerProcessFromHubImage(String imageName, String hostFilesPath, String directoryPath,
            String inputPath, String outputPath,
            String pythonCode)
            throws IOException, InterruptedException {
        System.out.println(
                "[DEBUG] -----------------> CONDITIONAL: Running Docker Hub image with python code: " + imageName);
        runConditionalDockerProcess(imageName, hostFilesPath, directoryPath, inputPath, outputPath, pythonCode);
        removeDockerImage(imageName);
    }

    /**
     * @brief Runs a Docker container with dynamic Python code execution.
     * @param imageName
     * @param hostFilesPath
     * @param directoryPath
     * @param inputPath
     * @param outputPath
     * @param pythonCode
     * @throws IOException
     * @throws InterruptedException
     */
    private void runConditionalDockerProcess(String imageName, String hostFilesPath, String directoryPath,
            String inputPath, String outputPath, String pythonCode)
            throws IOException, InterruptedException {

        System.out.println("[CHECK] Archivo de entrada (host): " + inputPath);
        System.out.println("[CHECK] Archivo de salida esperado (host): " + outputPath);

        java.io.File inputFile = new java.io.File(inputPath);
        java.io.File outputFile = new java.io.File(outputPath);

        List<String> command = List.of(
                "/usr/bin/docker", "run", "--rm",
                "-v", hostFilesPath + ":" + directoryPath,
                imageName,
                directoryPath + "/" + inputFile.getName(),
                directoryPath + "/" + outputFile.getName(),
                pythonCode);

        System.out.println("[DEBUG] Docker command:");
        command.forEach(arg -> System.out.print(arg + " "));
        System.out.println();

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);
        Process process = builder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[DOCKER] " + line);
            }
        }
        int exitCode = process.waitFor();
        System.out.println("[DEBUG] Docker process exited with code: " + exitCode);
        if (exitCode != 0) {
            throw new RuntimeException("Docker container " + imageName + " failed with exit code " + exitCode);
        }
        if (!outputFile.exists()) {
            throw new RuntimeException("Output file was not created: " + outputPath);
        } else {
            System.out.println("[DEBUG] Output file created: " + outputPath);
        }
    }

    // ------------------------------------------------------------UTILS------------------------------------------------------------//
    /**
     * @brief Removes a Docker image by its name.
     * @param imageName
     * @throws IOException
     * @throws InterruptedException
     */
    private void removeDockerImage(String imageName) throws IOException, InterruptedException {
        List<String> command = List.of("docker", "rmi", "-f", imageName);
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);
        Process process = builder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[DOCKER REMOVE] " + line);
            }
        }
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Failed to remove Docker image: " + imageName);
        }
    }

    /**
     * @brief Downloads a file from a given URL and saves it to the specified local
     *        path.
     * @param urlStr
     * @param localPath
     * @throws IOException
     */
    private void downloadFileFromURL(String urlString, String localPath) throws IOException {
        System.out.println("[DEBUG] Descargando desde: " + urlString + " a " + localPath);
        Path path = Paths.get(localPath);
        Path parent = path.getParent();
        if (!Files.exists(parent)) {
            System.out.println("[ERROR] El directorio padre no existe: " + parent);
        } else if (!Files.isWritable(parent)) {
            System.out.println("[ERROR] No hay permisos de escritura en: " + parent);
        } else {
            System.out.println("[DEBUG] Directorio de destino existe y es escribible.");
        }
        try (InputStream in = new URL(urlString).openStream()) {
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("[DEBUG] Descarga completa.");
        } catch (IOException exception) {
            System.out.println("[ERROR] Error durante la descarga: " + exception.getMessage());
            throw exception;
        }
    }

    /**
     * @brief Cleans a folder by deleting all files except the specified ones.
     * @param folderPath      Path to the folder.
     * @param fileNamesToKeep List of filenames to keep.
     */
    private void cleanFolderExcept(String folderPath, List<String> fileNamesToKeep) {
        java.io.File folder = new java.io.File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("[DEBUG] Folder does not exist or is not a directory: " + folderPath);
            return;
        }
        java.io.File[] files = folder.listFiles();
        if (files == null) {
            return;
        }
        for (java.io.File file : files) {
            if (!fileNamesToKeep.contains(file.getName())) {
                if (file.delete()) {
                    System.out.println("[DEBUG] Deleted file: " + file.getName());
                } else {
                    System.out.println("[DEBUG] Failed to delete file: " + file.getName());
                }
            }
        }
    }
}
