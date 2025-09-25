package es.ull.project.adapter.mongodb.writter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.Decimal128;
import java.math.RoundingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import es.ull.project.adapter.mongodb.MongoFields;
import es.ull.project.configuration.MongoConfiguration;
import es.ull.project.domain.entity.WorkFlow;
import es.ull.project.domain.valueobject.Link;
import es.ull.project.domain.valueobject.Node;
import es.ull.project.domain.valueobject.NodeContent;
import es.ull.project.domain.valueobject.NodeName;
import es.ull.project.domain.valueobject.Conditional;
import es.ull.project.domain.valueobject.File;
import es.ull.project.domain.valueobject.Operation;

@WritingConverter
public class WorkFlowWritingConverter implements Converter<WorkFlow, Document> {

    private static final Logger logger = LoggerFactory.getLogger(WorkFlowWritingConverter.class);
    private MongoConfiguration mongoConfiguration;

    public WorkFlowWritingConverter(MongoConfiguration mongoConfiguration) {
        this.mongoConfiguration = mongoConfiguration;
    }

    @Override
    public Document convert(WorkFlow workFlow) {
        logger.info("WorkFlow with id '{}' to be written", workFlow.getId());
        Document document = new Document();
        // workflow metadata
        document.put(MongoFields.ID, workFlow.getId());
        document.put(MongoFields.WORKFLOW_NAME, workFlow.getName().getName());
        document.put(MongoFields.WORKFLOW_DATE, workFlow.getCreationDate());
        document.put(MongoFields.ENABLED, workFlow.isEnabled());
        // Nodes
        List<Document> nodesList = new ArrayList<>();
        for (Node node : workFlow.getNodes()) {
            Document nodeDoc = new Document();
            nodeDoc.put(MongoFields.NODE_NAME, node.getName().getName());
            // Transform positions to Decimal128 with 15 decimal places
            BigDecimal xPos = node.getXPosition().getXPosition().setScale(15, RoundingMode.HALF_UP);
            BigDecimal yPos = node.getYPosition().getYPosition().setScale(15, RoundingMode.HALF_UP);
            nodeDoc.put(MongoFields.X_POSITION, new Decimal128(xPos));
            nodeDoc.put(MongoFields.Y_POSITION, new Decimal128(yPos));
            nodeDoc.put(MongoFields.NODE_COLOR, node.getColor().toString());
            nodeDoc.put(MongoFields.ICON, node.getIcon());
            // Content
            NodeContent content = node.getContent();
            Document contentDoc = new Document();
            contentDoc.put(MongoFields.NODETYPE, content.getType().name());
            switch (content.getType()) {
                case FILE:
                    File file = (File) content;
                    contentDoc.put(MongoFields.FILE_NAME, file.getName().getName());
                    if (file.getUri().isPresent()) {
                        contentDoc.put(MongoFields.FILE_URI, file.getUri().get().toString());
                    }
                    break;

                case OPERATION:
                    Operation op = (Operation) content;
                    contentDoc.put(MongoFields.OPERATION_NAME, op.getName().getName());
                    //contentDoc.put(MongoFields.DOCKER_IMAGE_NAME, op.getDockerImage().toString());
                    break;
                case CONDITIONAL:
                    Conditional conditional = (Conditional) content;
                    contentDoc.put(MongoFields.CONDITIONAL_NAME, conditional.getName().getName());
                    contentDoc.put(MongoFields.EXECUTION_CODE, conditional.getExecutionCode());

                    List<Document> linksConditionsList = new ArrayList<>();
                    for (Map.Entry<NodeName, Boolean> entry : conditional.getLinksConditions().entrySet()) {
                        Document condDoc = new Document();
                        condDoc.put(MongoFields.CONDITIONAL_LINK_NODE_NAME, entry.getKey().getName());
                        condDoc.put(MongoFields.CONDITIONAL_LINK_VALUE, entry.getValue());
                        linksConditionsList.add(condDoc);
                    }
                    contentDoc.put(MongoFields.LINKS_CONDITIONS, linksConditionsList);
                    break;

                default:
                    contentDoc.put("Error", "Unsupported content type: " + content.getType());
                    break;
            }

            nodeDoc.put(MongoFields.CONTENT, contentDoc);

            // Links from this node
            List<Document> linksList = new ArrayList<>();
            for (Link link : node.getLinks()) {
                Document linkDoc = new Document();
                linkDoc.put(MongoFields.LINK_NAME, link.getName().getValue());
                linkDoc.put(MongoFields.INITIAL_NODE_LINK, link.getInitialNode().getName());
                linkDoc.put(MongoFields.FINAL_NODE_LINK, link.getFinalNode().getName());
                linkDoc.put(MongoFields.LINK_CONDITION, link.getCondition());
                linksList.add(linkDoc);
            }
            nodeDoc.put(MongoFields.LINKS, linksList);

            nodesList.add(nodeDoc);
        }
        document.put(MongoFields.NODES, nodesList);

        return document;
    }
}
