package es.ull.project.adapter.mongodb.repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import es.ull.project.adapter.mongodb.MongoFields;
import es.ull.project.application.repository.ExecutionRepository;
import es.ull.project.domain.entity.Execution;

public class ExecutionMongoRepository implements ExecutionRepository {
    public static final String COLLECTION_NAME = "executions";
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Execution delete(UUID executionId) {
        if (executionId == null) {
            throw new IllegalArgumentException("executionId cant be null");
        }

        Query query = new Query(Criteria.where(MongoFields.ID).is(executionId));
        Execution executionToDelete = mongoTemplate.findOne(query, Execution.class, COLLECTION_NAME);
        mongoTemplate.remove(query, Execution.class, COLLECTION_NAME);
        return executionToDelete;
    }

    @Override
    public List<Execution> fetchAll() {
        return this.mongoTemplate.findAll(Execution.class, COLLECTION_NAME);
    }

    @Override
    public Execution save(Execution entity) {
        return this.mongoTemplate.save(entity, COLLECTION_NAME);
    }

    @Override
    public Execution fetchById(UUID executionId) {
        if (executionId == null) {
            throw new IllegalArgumentException("executionId Name cant be null");
        }
        Query query = new Query(Criteria.where(MongoFields.ID).is(executionId));
        return mongoTemplate.findOne(query, Execution.class, COLLECTION_NAME);
    }

    @Override
    public Execution update(UUID executionId, Execution entity) {
        Query query = new Query(Criteria.where(MongoFields.ID).is(executionId));
        Execution executionToUpdate = fetchById(executionId);
        System.out.println("Execution to be updated: " + executionToUpdate);

        Update update = new Update();

        System.out.println("Entity to be updated: " + entity);

        // NOT UPDATE ID AND CREATION DATE
        if (entity.getWorkFlowName() != null && entity.getWorkFlowName().getName() != null) {
            update.set(MongoFields.WORKFLOW_NAME, entity.getWorkFlowName().getName());
        }

        if (entity.getStatus() != null) {
            update.set(MongoFields.EXECUTE_STATUS, entity.getStatus());
        }

        if (entity.getMessage() != null) {
            update.set(MongoFields.EXECUTE_MESSAGE, entity.getMessage());
        }

        if (entity.getTotalExecutionTime() != null) {
            update.set(MongoFields.EXECUTION_TIME, entity.getTotalExecutionTime().toMillis());
        }

        if (entity.getNumberOfOperationExecuted() != 0) {
            update.set(MongoFields.NUMBER_OF_OPERATION_EXECUTED, entity.getNumberOfOperationExecuted());
        }

        if (entity.getOutputFiles() != null) {
            List<Document> outputFileDocs = entity.getOutputFiles().stream().map(file -> {
                Document fDoc = new Document();
                fDoc.put(MongoFields.FILE_NAME, file.getName().getName());
                file.getUri().ifPresent(uri -> fDoc.put(MongoFields.FILE_URI, uri.toString()));
                return fDoc;
            }).collect(Collectors.toList());
            update.set(MongoFields.OUTPUT_FILES, outputFileDocs);
        }

        if (entity.getNodesExecuted() != null) {
            List<Document> executedNodeDocs = entity.getNodesExecuted().stream().map(node -> {
                Document nodeDoc = new Document();
                nodeDoc.put(MongoFields.NODE_NAME, node.getNodeName().getName());
                nodeDoc.put(MongoFields.NODE_TIME_EXECUTION, node.getDuration().toMillis());
                return nodeDoc;
            }).collect(Collectors.toList());
            update.set(MongoFields.NODES_EXECUTED, executedNodeDocs);
        }

        mongoTemplate.findAndModify(query, update, Execution.class, COLLECTION_NAME);
        
        return fetchById(executionId);
    }
}
