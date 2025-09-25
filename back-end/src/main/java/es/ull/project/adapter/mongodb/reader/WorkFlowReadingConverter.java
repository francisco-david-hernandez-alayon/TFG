package es.ull.project.adapter.mongodb.reader;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import es.ull.project.adapter.mongodb.MongoFields;
import es.ull.project.configuration.MongoConfiguration;
import es.ull.project.domain.entity.WorkFlow;
import es.ull.project.domain.enumerate.NodeColor;
import es.ull.project.domain.enumerate.NodeContentType;
import es.ull.project.domain.enumerate.NodeIcon;
import es.ull.project.domain.valueobject.*;
import es.ull.utils.docker.UllDockerImageName;

@ReadingConverter
public class WorkFlowReadingConverter implements Converter<Document, WorkFlow> {

    private static final Logger logger = LoggerFactory.getLogger(WorkFlowReadingConverter.class);
    private MongoConfiguration mongoConfiguration;

    public WorkFlowReadingConverter(MongoConfiguration mongoConfiguration) {
        this.mongoConfiguration = mongoConfiguration;
    }

    @Override
    public WorkFlow convert(Document document) {
        logger.info("Convert WorkFlow: WorkFlow to read from document '{}'", document);
        UUID id = (UUID) document.get(MongoFields.ID);
        WorkFlowName flowName = new WorkFlowName(document.getString(MongoFields.WORKFLOW_NAME));
        Date date = document.getDate(MongoFields.WORKFLOW_DATE);
        LocalDateTime creationDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        boolean enabled = document.getBoolean(MongoFields.ENABLED);
        // Create all nodes without links
        List<Node> nodes = new ArrayList<>();
        List<Document> nodesList = document.getList(MongoFields.NODES, Document.class);
        for (Document nodeDoc : nodesList) {
            NodeName nodeName = new NodeName(nodeDoc.getString(MongoFields.NODE_NAME));
            BigDecimal x = new BigDecimal(nodeDoc.get(MongoFields.X_POSITION).toString());
            BigDecimal y = new BigDecimal(nodeDoc.get(MongoFields.Y_POSITION).toString());
            NodeColor color = NodeColor.valueOf(nodeDoc.getString(MongoFields.NODE_COLOR));
            NodeIcon icon = NodeIcon.valueOf(nodeDoc.getString(MongoFields.ICON));
            // Reconstruct content
            Document contentDoc = (Document) nodeDoc.get(MongoFields.CONTENT);
            NodeContentType contentType = NodeContentType.valueOf(contentDoc.getString(MongoFields.NODETYPE));
            NodeContent content = null;
            try {
                switch (contentType) {
                    case FILE:
                        FileName fileName = new FileName(contentDoc.getString(MongoFields.FILE_NAME));
                        if (contentDoc.containsKey(MongoFields.FILE_URI)) {
                            URI url = new URI(contentDoc.getString(MongoFields.FILE_URI));
                            content = new File(fileName, url);
                        } else {
                            content = new File(fileName); // No URI provided
                        }
                        break;
                    case OPERATION:
                        OperationName opName = new OperationName(contentDoc.getString(MongoFields.OPERATION_NAME));
                        Operation operation = mongoConfiguration.operationRepository().fetchByName(opName); // search for operation by name
                        if (operation == null) {
                            throw new IllegalStateException(
                                    "Operation with Name: " + opName.getName() + " not found in repository.");
                        }
                        content = operation;
                        break;

                    case CONDITIONAL:
                        ConditionalName conditionalName = new ConditionalName(
                                contentDoc.getString(MongoFields.CONDITIONAL_NAME));
                        String executionCode = contentDoc.getString(MongoFields.EXECUTION_CODE);

                        Object rawLinksConditions = contentDoc.get(MongoFields.LINKS_CONDITIONS);
                        Map<NodeName, Boolean> linksConditions = new java.util.HashMap<>();

                        if (rawLinksConditions instanceof List<?>) {
                            List<?> rawList = (List<?>) rawLinksConditions;
                            for (Object item : rawList) {
                                if (item instanceof Document) {
                                    Document condDoc = (Document) item;
                                    NodeName targetNode = new NodeName(
                                            condDoc.getString(MongoFields.CONDITIONAL_LINK_NODE_NAME));
                                    boolean value = condDoc.getBoolean(MongoFields.CONDITIONAL_LINK_VALUE);
                                    linksConditions.put(targetNode, value);

                                } else {
                                    throw new IllegalStateException(
                                            "Mongo reader: Expected Document in linksConditions list, but found: "
                                                    + item.getClass());
                                }
                            }
                        } else {
                            throw new IllegalStateException(
                                    "Mongo reader: Expected linksConditions to be a List, but found: "
                                            + rawLinksConditions.getClass());
                        }

                        content = new Conditional(conditionalName, executionCode, linksConditions);
                        break;

                    default:
                        throw new IllegalArgumentException("Unsupported content type: " + contentType);
                }
            } catch (Exception exception) {
                logger.error("Error reconstructing content for node '{}': {}", nodeName, exception.getMessage());
                throw new RuntimeException("Failed to reconstruct node content", exception);
            }
            // Create node with empty links for now
            final Node node = new Node(
                    nodeName,
                    new XPosition(x),
                    new YPosition(y),
                    color,
                    icon,
                    content,
                    new ArrayList<>());
            nodes.add(node);
        }
        // Add links to each node, resolving references to existing nodes
        for (int i = 0; i < nodesList.size(); i++) {
            Document nodeDoc = nodesList.get(i);
            Node currentNode = nodes.get(i);
            List<Document> linkDocs = nodeDoc.getList(MongoFields.LINKS, Document.class);
            List<Link> links = new ArrayList<>();
            if (linkDocs != null) {
                for (Document linkDoc : linkDocs) {
                    LinkName linkName = new LinkName(linkDoc.getString(MongoFields.LINK_NAME));
                    NodeName initialname = new NodeName(linkDoc.getString(MongoFields.INITIAL_NODE_LINK));
                    NodeName finalName = new NodeName(linkDoc.getString(MongoFields.FINAL_NODE_LINK));
                    boolean condition = linkDoc.getBoolean(MongoFields.LINK_CONDITION);
                    Link link = new Link(linkName, initialname, finalName, condition);
                    links.add(link);
                }
            }
            // Set resolved links to current node
            nodes.set(i, currentNode.setLinks(links));
        }
        return new WorkFlow(id, flowName, nodes, creationDate, enabled);
    }
}
