package es.ull.project.adapter.mongodb.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import es.ull.project.adapter.mongodb.MongoFields;
import es.ull.project.application.repository.OperationRepository;
import es.ull.project.domain.valueobject.Operation;
import es.ull.project.domain.valueobject.OperationName;

public class OperationMongoRepository implements OperationRepository {

    public static final String COLLECTION_NAME = "operations";
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Operation delete(OperationName operationName) {
        if (operationName == null) {
            throw new IllegalArgumentException("Operation Name cant be null");
        }

        Query query = new Query(Criteria.where(MongoFields.OPERATION_NAME).is(operationName.getName()));
        Operation operationToDelete = mongoTemplate.findOne(query, Operation.class, COLLECTION_NAME);
        mongoTemplate.remove(query, Operation.class, COLLECTION_NAME);
        return operationToDelete;
    }

    @Override
    public List<Operation> fetchAll() {
        return this.mongoTemplate.findAll(Operation.class, COLLECTION_NAME);
    }

    @Override
    public Operation save(Operation entity) {
        return this.mongoTemplate.save(entity, COLLECTION_NAME);
    }

    @Override
    public Operation fetchByName(OperationName operationName) {
        if (operationName == null) {
            throw new IllegalArgumentException("Operation Name cant be null");
        }
        Query query = new Query(Criteria.where(MongoFields.OPERATION_NAME).is(operationName.getName()));
        return mongoTemplate.findOne(query, Operation.class, COLLECTION_NAME);
    }

    @Override
    public Operation update(OperationName operationName, Operation entity) {
        Query query = new Query(Criteria.where(MongoFields.OPERATION_NAME).is(operationName.getName()));
        Update update = new Update();
        if (entity.getName() != null) {
            update.set(MongoFields.OPERATION_NAME, entity.getName().getName());
        }
        if (entity.getDockerImage() != null) {
            update.set(MongoFields.DOCKER_IMAGE_NAME, entity.getDockerImage().getValue());
        }
        return mongoTemplate.findAndModify(query, update, Operation.class, COLLECTION_NAME);
    }
}
