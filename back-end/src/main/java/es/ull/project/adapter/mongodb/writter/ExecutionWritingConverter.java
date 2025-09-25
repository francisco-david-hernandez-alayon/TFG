package es.ull.project.adapter.mongodb.writter;

import java.util.*;
import java.util.stream.Collectors;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import es.ull.project.adapter.mongodb.MongoFields;
import es.ull.project.configuration.MongoConfiguration;
import es.ull.project.domain.entity.Execution;

@WritingConverter
public class ExecutionWritingConverter implements Converter<Execution, Document> {

    private static final Logger logger = LoggerFactory.getLogger(WorkFlowWritingConverter.class);
    private MongoConfiguration mongoConfiguration;

    public ExecutionWritingConverter(MongoConfiguration mongoConfiguration) {
        this.mongoConfiguration = mongoConfiguration;
    }

    @Override
    public Document convert(Execution executeData) {
        logger.info("Execution with id '{}' to be written", executeData.getId());

        Document doc = new Document();
        doc.put(MongoFields.ID, executeData.getId());
        doc.put(MongoFields.WORKFLOW_NAME, executeData.getWorkFlowName().getName());
        doc.put(MongoFields.EXECUTION_DATE, executeData.getCreationDate());

        doc.put(MongoFields.EXECUTE_STATUS, executeData.getStatus().name());
        doc.put(MongoFields.EXECUTE_MESSAGE, executeData.getMessage());
        doc.put(MongoFields.EXECUTION_TIME, executeData.getTotalExecutionTime().toMillis());
        doc.put(MongoFields.NUMBER_OF_OPERATION_EXECUTED, executeData.getNumberOfOperationExecuted());

        // Output Files
        List<Document> outputFileDocs = executeData.getOutputFiles().stream().map(file -> {
            Document fDoc = new Document();
            fDoc.put(MongoFields.FILE_NAME, file.getName().getName());
            file.getUri().ifPresent(uri -> fDoc.put(MongoFields.FILE_URI, uri.toString()));
            return fDoc;
        }).collect(Collectors.toList());
        doc.put(MongoFields.OUTPUT_FILES, outputFileDocs);

        // Executed Nodes
        List<Document> executedNodeDocs = executeData.getNodesExecuted().stream().map(node -> {
            Document nodeDoc = new Document();
            nodeDoc.put(MongoFields.NODE_NAME, node.getNodeName().getName());
            nodeDoc.put(MongoFields.NODE_TIME_EXECUTION, node.getDuration().toMillis());
            return nodeDoc;
        }).collect(Collectors.toList());
        doc.put(MongoFields.NODES_EXECUTED, executedNodeDocs);

        return doc;
    }
}
