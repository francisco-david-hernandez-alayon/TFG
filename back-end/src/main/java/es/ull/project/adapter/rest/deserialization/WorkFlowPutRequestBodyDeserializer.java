package es.ull.project.adapter.rest.deserialization;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.fasterxml.jackson.databind.JsonNode;

import es.ull.project.adapter.rest.JsonFields;
import es.ull.project.adapter.rest.request.WorkFlowPutRequestBody;
import es.ull.project.domain.enumerate.NodeColor;
import es.ull.project.domain.enumerate.NodeContentType;
import es.ull.project.domain.enumerate.NodeIcon;
import es.ull.project.domain.valueobject.WorkFlowName;
import es.ull.project.domain.valueobject.XPosition;
import es.ull.project.domain.valueobject.YPosition;
import es.ull.project.domain.valueobject.Conditional;
import es.ull.project.domain.valueobject.ConditionalName;
import es.ull.project.domain.valueobject.File;
import es.ull.project.domain.valueobject.FileName;
import es.ull.project.domain.valueobject.Link;
import es.ull.project.domain.valueobject.LinkName;
import es.ull.project.domain.valueobject.Node;
import es.ull.project.domain.valueobject.NodeContent;
import es.ull.project.domain.valueobject.NodeName;
import es.ull.project.domain.valueobject.Operation;
import es.ull.project.domain.valueobject.OperationName;
import es.ull.utils.docker.UllDockerImageName;
import es.ull.utils.rest.error.ApiSubError;
import es.ull.utils.rest.error.ApiSubErrorMessageRejectedValueField;
import es.ull.utils.rest.serialization.UllJsonDeserializer;

public class WorkFlowPutRequestBodyDeserializer extends UllJsonDeserializer<WorkFlowPutRequestBody> {

    private static final String ERROR_WORKFLOW_NAME_NOT_DEFINED = "Workflow name not defined";
    private static final String ERROR_NODES_NOT_DEFINED = "Nodes not defined";

    @Override
    public WorkFlowPutRequestBody parse(JsonNode flowNode) {
        validate(flowNode);
        WorkFlowName flowName = new WorkFlowName(flowNode.get(JsonFields.WORKFLOW_NAME).asText());
        boolean enabled = flowNode.get(JsonFields.ENABLED).asBoolean();
        List<Node> nodes = new ArrayList<>();
        Map<NodeName, Node> nodeMap = new HashMap<>();
        JsonNode jsonNodes = flowNode.get(JsonFields.NODES);
        for (JsonNode nodeJson : jsonNodes) {
            NodeName nodeName = new NodeName(nodeJson.get(JsonFields.NODE_NAME).asText());
            double x = nodeJson.get(JsonFields.X_POSITION).asDouble();
            double y = nodeJson.get(JsonFields.Y_POSITION).asDouble();
            NodeColor color = NodeColor.valueOf(nodeJson.get(JsonFields.NODE_COLOR).asText());
            NodeIcon icon = NodeIcon.valueOf(nodeJson.get(JsonFields.ICON).textValue());
            JsonNode contentJson = nodeJson.get(JsonFields.CONTENT);
            NodeContentType contentType = NodeContentType.valueOf(contentJson.get(JsonFields.NODETYPE).asText());
            NodeContent content;
            try {
                switch (contentType) {
                    case FILE:
                        FileName fileName = new FileName(contentJson.get(JsonFields.FILE_NAME).asText());
                        if (!contentJson.has(JsonFields.FILE_URI)) { // not exist URI
                            content = new File(fileName);
                            break;

                        } else {
                            URI url = new URI(contentJson.get(JsonFields.FILE_URI).asText());
                            content = new File(fileName, url);
                        }
                        break;
                    case OPERATION:
                        OperationName operationName = new OperationName(
                                contentJson.get(JsonFields.OPERATION_NAME).asText());
                        UllDockerImageName dockerImage = new UllDockerImageName(
                                contentJson.get(JsonFields.DOCKER_IMAGE_NAME).asText());
                        content = new Operation(operationName, dockerImage);
                        break;
                    case CONDITIONAL:
                        String conditionalNameStr = contentJson.get(JsonFields.CONDITIONAL_NAME).asText();
                        String executionCode = contentJson.get(JsonFields.EXECUTION_CODE).asText();

                        Map<NodeName, Boolean> linksConditions = new HashMap<>();
                        JsonNode linksConditionsJson = contentJson.get(JsonFields.LINKS_CONDITIONS);
                        if (linksConditionsJson.isArray()) {
                            for (JsonNode conditionNode : linksConditionsJson) {
                                String nodeNameStr = conditionNode.get(JsonFields.CONDITIONAL_LINK_NODE_NAME).asText();
                                boolean value = conditionNode.get(JsonFields.CONDITIONAL_LINK_VALUE).asBoolean();
                                linksConditions.put(new NodeName(nodeNameStr), value);
                            }
                        }

                        Conditional conditional = new Conditional(
                                new ConditionalName(conditionalNameStr),
                                executionCode,
                                linksConditions);
                        content = conditional;
                        break;

                    default:
                        throw new IllegalArgumentException("Unsupported node content type: " + contentType);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse node content for node: ", e);
            }
            Node node = new Node(
                    nodeName,
                    new XPosition(BigDecimal.valueOf(x)),
                    new YPosition(BigDecimal.valueOf(y)),
                    color,
                    icon,
                    content,
                    new ArrayList<>());
            nodes.add(node);
            nodeMap.put(nodeName, node);
        }

        // Parse links
        for (JsonNode nodeJson : jsonNodes) {
            NodeName currentNodeName = new NodeName(nodeJson.get(JsonFields.NODE_NAME).asText());
            Node currentNode = nodeMap.get(currentNodeName);
            JsonNode nodeLinksJson = nodeJson.get(JsonFields.LINKS);
            
            if (nodeLinksJson != null) {
                for (JsonNode linkJson : nodeLinksJson) {
                    LinkName linkName = new LinkName(linkJson.get(JsonFields.LINK_NAME).asText());
                    NodeName initialName = new NodeName(linkJson.get(JsonFields.INITIAL_NODE_LINK).asText());
                    NodeName finalName = new NodeName(linkJson.get(JsonFields.FINAL_NODE_LINK).asText());
                    boolean condition = linkJson.get(JsonFields.LINK_CONDITION).asBoolean();

                    if (initialName == null || finalName == null) {
                        throw new IllegalArgumentException(
                                "Link refers to unknown node(s): " + initialName + ", " + finalName);
                    }

                    Link link = new Link(linkName, initialName, finalName, condition);
                    currentNode = currentNode.addLink(link);
                    nodeMap.put(currentNodeName, currentNode);
                }
            }
        }
        nodes = new ArrayList<>(nodeMap.values());

        // Construct and return the workflow
        WorkFlowPutRequestBody parsedRequest = new WorkFlowPutRequestBody();
        parsedRequest.setName(flowName);
        parsedRequest.setNodes(nodes);
        parsedRequest.setEnabled(enabled);

        return parsedRequest;
    }

    @Override
    protected Optional<List<ApiSubError>> validate(JsonNode jsonNodeData) {
        List<ApiSubError> errors = new ArrayList<>();
        if (!jsonNodeData.has(JsonFields.WORKFLOW_NAME)) {
            errors.add(new ApiSubErrorMessageRejectedValueField(ERROR_WORKFLOW_NAME_NOT_DEFINED,
                    JsonFields.WORKFLOW_NAME));
        }
        if (!jsonNodeData.has(JsonFields.NODES)) {
            errors.add(new ApiSubErrorMessageRejectedValueField(ERROR_NODES_NOT_DEFINED, JsonFields.NODES));
        }
        return errors.isEmpty() ? Optional.empty() : Optional.of(errors);
    }
}
