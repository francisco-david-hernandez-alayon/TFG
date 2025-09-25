package es.ull.project.adapter.mongodb.repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import es.ull.project.adapter.mongodb.MongoFields;
import es.ull.project.application.repository.WorkFlowRepository;
import es.ull.project.domain.entity.WorkFlow;
import es.ull.project.domain.valueobject.Conditional;
import es.ull.project.domain.valueobject.File;
import es.ull.project.domain.valueobject.Link;
import es.ull.project.domain.valueobject.Node;
import es.ull.project.domain.valueobject.NodeContent;
import es.ull.project.domain.valueobject.NodeName;
import es.ull.project.domain.valueobject.Operation;

public class WorkFlowMongoRepository implements WorkFlowRepository {

    public static final String COLLECTION_NAME = "workflows";
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public WorkFlow delete(UUID entityId) {

        if (entityId == null) {
            throw new IllegalArgumentException("Entity ID cant be null");
        }

        Query query = new Query(Criteria.where(MongoFields.ID).is(entityId));
        WorkFlow flowToDelete = mongoTemplate.findOne(query, WorkFlow.class, COLLECTION_NAME);
        mongoTemplate.remove(query, WorkFlow.class, COLLECTION_NAME);
        return flowToDelete;

    }

    @Override
    public List<WorkFlow> fetchAll() {
        return this.mongoTemplate.findAll(WorkFlow.class, COLLECTION_NAME);
    }

    @Override
    public WorkFlow save(WorkFlow entity) {
        return this.mongoTemplate.save(entity, COLLECTION_NAME);
    }

    @Override
    public WorkFlow fetchById(UUID flowID) {
        if (flowID == null) {
            throw new IllegalArgumentException("Workflow ID cant be null");
        }
        Query query = new Query(Criteria.where(MongoFields.ID).is(flowID));
        return mongoTemplate.findOne(query, WorkFlow.class, COLLECTION_NAME);
    }

    @Override
    public WorkFlow update(WorkFlow entity) {
        Query query = new Query(Criteria.where(MongoFields.ID).is(entity.getId()));
        Update update = new Update();
        if (entity.getName() != null) {
            update.set(MongoFields.WORKFLOW_NAME, entity.getName().getName());
        }
        if (entity.getCreationDate() != null) {
            update.set(MongoFields.WORKFLOW_DATE, entity.getCreationDate());
        }
        if (entity.getNodes() != null) {
            List<Document> nodesList = convertNodes(entity.getNodes());
            update.set(MongoFields.NODES, nodesList);
        }
        update.set(MongoFields.ENABLED, entity.isEnabled());
        return mongoTemplate.findAndModify(query, update, WorkFlow.class, COLLECTION_NAME);
    }

    private List<Document> convertNodes(List<Node> nodes) {
        List<Document> nodesList = new ArrayList<>();
        if (nodes != null) {
            for (Node node : nodes) {
                Document nodeDoc = new Document();
                nodeDoc.put(MongoFields.NODE_NAME, node.getName().getName());
                // Transform positions to Decimal128 with 15 decimal places
                BigDecimal xPos = node.getXPosition().getXPosition().setScale(15, RoundingMode.HALF_UP);
                BigDecimal yPos = node.getYPosition().getYPosition().setScale(15, RoundingMode.HALF_UP);
                nodeDoc.put(MongoFields.X_POSITION, new Decimal128(xPos));
                nodeDoc.put(MongoFields.Y_POSITION, new Decimal128(yPos));
                nodeDoc.put(MongoFields.NODE_COLOR, node.getColor().name());
                nodeDoc.put(MongoFields.ICON, node.getIcon());
                // Convert content
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
                        Operation opContent = (Operation) content;
                        contentDoc.put(MongoFields.OPERATION_NAME, opContent.getName().getName());
                        contentDoc.put(MongoFields.DOCKER_IMAGE_NAME, opContent.getDockerImage().getValue());
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
                        throw new IllegalArgumentException("Unsupported node content type: " + content.getType());
                }
                nodeDoc.put(MongoFields.CONTENT, contentDoc);
                // Convert links
                List<Document> linksList = convertLinks(node.getLinks());
                nodeDoc.put(MongoFields.LINKS, linksList);
                nodesList.add(nodeDoc);
            }
        }
        return nodesList;
    }

    private List<Document> convertLinks(List<Link> links) {
        List<Document> linksList = new ArrayList<>();
        if (links != null) {
            for (Link link : links) {
                if (link != null &&
                        link.getName() != null &&
                        link.getInitialNode() != null &&
                        link.getFinalNode() != null) {
                    Document linkDoc = new Document();
                    linkDoc.put(MongoFields.LINK_NAME, link.getName().getValue());
                    linkDoc.put(MongoFields.INITIAL_NODE_LINK, link.getInitialNode().getName());
                    linkDoc.put(MongoFields.FINAL_NODE_LINK, link.getFinalNode().getName());
                    linkDoc.put(MongoFields.LINK_CONDITION, link.getCondition());
                    linksList.add(linkDoc);
                }
            }
        }
        return linksList;
    }
}