package es.ull.project.adapter.rest.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import es.ull.project.adapter.rest.JsonFields;
import es.ull.project.adapter.rest.response.WorkFlowExecuteResponseBody;
import es.ull.project.domain.valueobject.File;
import es.ull.project.domain.valueobject.NodeExecuteData;

import java.io.IOException;
import java.util.List;

public class WorkFlowExecuteResponseBodySerializer extends StdSerializer<WorkFlowExecuteResponseBody> {

    public WorkFlowExecuteResponseBodySerializer() {
        this(null);
    }

    public WorkFlowExecuteResponseBodySerializer(Class<WorkFlowExecuteResponseBody> t) {
        super(t);
    }

    @Override
    public void serialize(WorkFlowExecuteResponseBody value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeStartObject();
        try {
            gen.writeStringField(JsonFields.ID, value.getId().toString());
            gen.writeStringField(JsonFields.WORKFLOW_NAME, value.getWorkFlowName().getName());
            gen.writeStringField(JsonFields.EXECUTION_DATE, value.getCreationDate().toString());
            gen.writeStringField(JsonFields.EXECUTE_STATUS, value.getStatus().name());
            gen.writeStringField(JsonFields.EXECUTE_MESSAGE, value.getMessage());
            gen.writeNumberField(JsonFields.EXECUTION_TIME, value.getTotalExecutionTime().toMillis());
            gen.writeNumberField(JsonFields.NUMBER_OF_OPERATION_EXECUTED, value.getNumberOfOperationExecuted());
            gen.writeArrayFieldStart(JsonFields.OUTPUT_FILES);
            List<File> outputFiles = value.getOutputFiles();
            for (File output : outputFiles) {
                gen.writeStartObject();
                gen.writeStringField(JsonFields.FILE_NAME, output.getName().getName());
                if (output.hasUri()) {
                    gen.writeStringField(JsonFields.FILE_URI, output.getUri().get().toString());
                }
                gen.writeEndObject();
            }
            gen.writeEndArray();
            gen.writeArrayFieldStart(JsonFields.NODES_EXECUTED);
            for (NodeExecuteData nodeData : value.getNodesExecuted()) {
                gen.writeStartObject();
                gen.writeStringField(JsonFields.NODE_NAME, nodeData.getNodeName().getName());
                gen.writeNumberField(JsonFields.NODE_TIME_EXECUTION, nodeData.getDuration().toMillis());
                gen.writeEndObject();
            }
            gen.writeEndArray();
            gen.writeEndObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error serializing WorkFlowExecuteResponseBody", e);
        }
    }
}
