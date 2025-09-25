package es.ull.project.adapter.mongodb.reader;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import es.ull.project.adapter.mongodb.MongoFields;
import es.ull.project.configuration.MongoConfiguration;
import es.ull.project.domain.entity.Execution;
import es.ull.project.domain.enumerate.ExecuteStatus;
import es.ull.project.domain.valueobject.*;

@ReadingConverter
public class ExecutionReadingConverter implements Converter<Document, Execution> {

    private static final Logger logger = LoggerFactory.getLogger(WorkFlowReadingConverter.class);
    private MongoConfiguration mongoConfiguration;

    public ExecutionReadingConverter(MongoConfiguration mongoConfiguration) {
        this.mongoConfiguration = mongoConfiguration;
    }
 
    @Override
    public Execution convert(Document doc) {
        logger.info("Convert ExecuteData: ExecuteData to read from document '{}'", doc);

        UUID id = (UUID) doc.get(MongoFields.ID);
        WorkFlowName workFlowName = new WorkFlowName(doc.getString(MongoFields.WORKFLOW_NAME));

        Date date = doc.getDate(MongoFields.EXECUTION_DATE);
        LocalDateTime executionDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        ExecuteStatus status = ExecuteStatus.valueOf(doc.getString(MongoFields.EXECUTE_STATUS));
        String message = doc.getString(MongoFields.EXECUTE_MESSAGE);

        Duration totalExecutionTime = Duration.ofMillis(doc.getLong(MongoFields.EXECUTION_TIME));
        int numberOfOperationsExecuted = doc.getInteger(MongoFields.NUMBER_OF_OPERATION_EXECUTED, 0);

        // Output Files
        List<Document> outputFileDocs = (List<Document>) doc.get(MongoFields.OUTPUT_FILES);
        List<File> outputFiles = outputFileDocs.stream().map(fileDoc -> {
            FileName fileName = new FileName(fileDoc.getString(MongoFields.FILE_NAME));
            URI uri = fileDoc.containsKey(MongoFields.FILE_URI)
                    ? URI.create(fileDoc.getString(MongoFields.FILE_URI))
                    : null;
            return new File(fileName, Optional.ofNullable(uri).get());
        }).collect(Collectors.toList());

        // Executed Nodes
        List<Document> executedNodeDocs = (List<Document>) doc.get(MongoFields.NODES_EXECUTED);
        List<NodeExecuteData> executedNodes = executedNodeDocs.stream().map(nodeDoc -> {
            NodeName nodeName = new NodeName(nodeDoc.getString(MongoFields.NODE_NAME));
            Duration duration = Duration.ofMillis(nodeDoc.getLong(MongoFields.NODE_TIME_EXECUTION));
            return new NodeExecuteData(nodeName, duration);
        }).collect(Collectors.toList());

        return new Execution(
                id,
                workFlowName,
                executionDate,
                status,
                message,
                outputFiles,
                executedNodes,
                totalExecutionTime,
                numberOfOperationsExecuted
        );
    }
}

