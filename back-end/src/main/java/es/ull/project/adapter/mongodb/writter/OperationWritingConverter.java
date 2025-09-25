package es.ull.project.adapter.mongodb.writter;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import es.ull.project.adapter.mongodb.MongoFields;
import es.ull.project.configuration.MongoConfiguration;
import es.ull.project.domain.valueobject.Operation;

public class OperationWritingConverter implements Converter<Operation, Document> {

    private static final Logger logger = LoggerFactory.getLogger(OperationWritingConverter.class);
    private MongoConfiguration mongoConfiguration;

    public OperationWritingConverter(MongoConfiguration mongoConfiguration) {
        this.mongoConfiguration = mongoConfiguration;
    }

    @Override
    public Document convert(Operation operation) {
        logger.info("Convert Operation: Writing OperationContent '{}'", operation);
        Document document = new Document();
        document.put(MongoFields.OPERATION_NAME, operation.getName().getName());
        document.put(MongoFields.DOCKER_IMAGE_NAME, operation.getDockerImage().getValue());
        return document;
    }
}
