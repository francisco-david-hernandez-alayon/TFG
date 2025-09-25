package es.ull.project.adapter.mongodb.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.convert.ReadingConverter;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;

import es.ull.project.adapter.mongodb.MongoFields;
import es.ull.project.configuration.MongoConfiguration;
import es.ull.project.domain.valueobject.Operation;
import es.ull.project.domain.valueobject.OperationName;
import es.ull.utils.docker.UllDockerImageName;

@ReadingConverter
public class OperationReadingConverter implements Converter<Document, Operation> {

    private static final Logger logger = LoggerFactory.getLogger(OperationReadingConverter.class);
    private MongoConfiguration mongoConfiguration;

    public OperationReadingConverter(MongoConfiguration mongoConfiguration) {
        this.mongoConfiguration = mongoConfiguration;
    }

    @Override
    public Operation convert(Document document) {
        logger.info("Convert Operation: Reading from document '{}'", document);
        OperationName operationName = new OperationName(document.getString(MongoFields.OPERATION_NAME));
        UllDockerImageName dockerImageName = new UllDockerImageName(document.getString(MongoFields.DOCKER_IMAGE_NAME));
        logger.info("Convert Operation: Created operation content with name '{}', dockerImage '{}'",
                operationName, dockerImageName);
        return new Operation(operationName, dockerImageName);
    }
}