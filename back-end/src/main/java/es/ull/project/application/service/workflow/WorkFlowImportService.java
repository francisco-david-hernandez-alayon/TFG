package es.ull.project.application.service.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.ull.project.application.repository.WorkFlowRepository;
import es.ull.project.application.usecase.workflow.WorkFlowImportUseCase;
import es.ull.project.application.repository.OperationRepository;
import es.ull.project.domain.entity.WorkFlow;
import es.ull.project.domain.enumerate.NodeContentType;
import es.ull.project.domain.valueobject.ExportFile;
import es.ull.project.domain.valueobject.Node;
import es.ull.project.domain.valueobject.NodeContent;
import es.ull.project.domain.valueobject.Operation;
import es.ull.project.domain.valueobject.OperationName;

@Service
public class WorkFlowImportService implements WorkFlowImportUseCase {

    @Autowired
    private WorkFlowRepository flowRepository;
    @Autowired
    private OperationRepository operationRepository;

    private static int MAX_RETRIES_OPERATION_NAME_GENERATION = 100;

    @Override
    public Optional<WorkFlow> importWorkFlow(ExportFile exportFile) {
        WorkFlow importedFlow = exportFile.getData();

        List<Node> newNodes = new ArrayList<>();
        Map<OperationName, Operation> operationCache = new HashMap<>(); // avoid processing the same operations on
                                                                        // different nodes
        for (Node oldNode : importedFlow.getNodes()) {
            NodeContent content = oldNode.getContent();
            // if the content is an operation, check if it already exists that name for
            // operation in the repository
            if (oldNode.getType() == NodeContentType.OPERATION && content instanceof Operation oldOperation) {
                OperationName originalName = oldOperation.getName();
                // Use cached operation if it already exists to avoid creating duplicates
                Operation newOperation = operationCache.get(originalName);
                if (newOperation == null) {
                    newOperation = new Operation(oldOperation.getName(), oldOperation.getDockerImage());
                    // Rename this new operation if already exists in repository
                    if (operationRepository.fetchByName(newOperation.getName()) != null) {
                        OperationName newName = getUniqueOperationName(newOperation.getName());
                        newOperation = newOperation.setName(newName);
                    }

                    operationRepository.save(newOperation);
                    operationCache.put(originalName, newOperation);
                }
                content = newOperation; // Replace the content with the cached one
            }
            Node newNode = new Node(
                    oldNode.getName(),
                    oldNode.getXPosition(),
                    oldNode.getYPosition(),
                    oldNode.getColor(),
                    oldNode.getIcon(),
                    content,
                    oldNode.getLinks());
            newNodes.add(newNode);
        }

        // Create a new WorkFlow with the imported data
        WorkFlow newFlow = new WorkFlow(importedFlow.getName(), newNodes, importedFlow.isEnabled());
        WorkFlow savedFlow = flowRepository.save(newFlow);
        return Optional.ofNullable(savedFlow);
    }

    /**
     * @brief Get a unique operation name based on the base name.
     * @param baseName
     * @return
     */
    private OperationName getUniqueOperationName(OperationName baseName) {
        String base = baseName.getName();
        String baseRoot = base;
        int counter = 1;

        // Check if the base name ends like that: '_number'
        int lastUnderscore = base.lastIndexOf('_');
        if (lastUnderscore != -1) {
            String possibleNumber = base.substring(lastUnderscore + 1);
            try {
                int num = Integer.parseInt(possibleNumber);
                baseRoot = base.substring(0, lastUnderscore);
                counter = num + 1;
            } catch (NumberFormatException e) {
                // not end with a number, so we keep the base as is
            }
        }

        OperationName newName = new OperationName(base);
        while (operationRepository.fetchByName(newName) != null) {
            newName = new OperationName(baseRoot + "_" + counter);
            counter++;
            if (counter > MAX_RETRIES_OPERATION_NAME_GENERATION) {
                throw new RuntimeException("Too many duplicated operation names for operation: " + baseRoot);
            }
        }
        return newName;
    }

}
