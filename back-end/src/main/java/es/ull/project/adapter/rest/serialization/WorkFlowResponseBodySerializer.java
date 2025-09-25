package es.ull.project.adapter.rest.serialization;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import es.ull.project.adapter.rest.JsonFields;
import es.ull.project.adapter.rest.response.WorkFlowResponseBody;
import es.ull.project.domain.valueobject.Link;
import es.ull.project.domain.valueobject.Node;
import es.ull.project.domain.valueobject.NodeContent;
import es.ull.project.domain.valueobject.Conditional;
import es.ull.project.domain.valueobject.File;
import es.ull.project.domain.valueobject.Operation;

public class WorkFlowResponseBodySerializer extends StdSerializer<WorkFlowResponseBody> {

    public WorkFlowResponseBodySerializer() {
        this(null);
    }

    public WorkFlowResponseBodySerializer(Class<WorkFlowResponseBody> t) {
        super(t);
    }

    @Override
    public void serialize(WorkFlowResponseBody workflow, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeStartObject();
        try {
            gen.writeStringField(JsonFields.ID, workflow.getId().toString());
            gen.writeStringField(JsonFields.WORKFLOW_NAME, workflow.getName().getName());
            gen.writeStringField(JsonFields.WORKFLOW_DATE, workflow.getCreationDate().toString());
            gen.writeBooleanField(JsonFields.ENABLED, workflow.isEnabled());
            gen.writeArrayFieldStart(JsonFields.NODES);
            List<Node> nodes = workflow.getNodes();
            for (Node node : nodes) {
                gen.writeStartObject();
                gen.writeStringField(JsonFields.NODE_NAME, node.getName().getName());
                gen.writeNumberField(JsonFields.X_POSITION, node.getXPosition().getXPosition());
                gen.writeNumberField(JsonFields.Y_POSITION, node.getYPosition().getYPosition());
                gen.writeStringField(JsonFields.NODE_COLOR, node.getColor().toString());
                gen.writeStringField(JsonFields.ICON, node.getIcon().toString());
                NodeContent content = node.getContent();
                gen.writeObjectFieldStart(JsonFields.CONTENT);
                gen.writeStringField(JsonFields.NODETYPE, content.getType().name());
                switch (content.getType()) {
                    case FILE:
                        File file = (File) content;
                        gen.writeStringField(JsonFields.FILE_NAME, file.getName().getName());
                        if (file.hasUri()) {
                            gen.writeStringField(JsonFields.FILE_URI, file.getUri().get().toString());
                        }
                        break;
                    case OPERATION:
                        Operation op = (Operation) content;
                        gen.writeStringField(JsonFields.OPERATION_NAME, op.getName().getName());
                        gen.writeStringField(JsonFields.DOCKER_IMAGE_NAME, op.getDockerImage().toString());
                        break;
                    case CONDITIONAL:
                        Conditional conditional = (Conditional) content;
                        gen.writeStringField(JsonFields.CONDITIONAL_NAME, conditional.getName().getName());
                        gen.writeStringField(JsonFields.EXECUTION_CODE, conditional.getExecutionCode());
                        gen.writeArrayFieldStart(JsonFields.LINKS_CONDITIONS);
                        for (var entry : conditional.getLinksConditions().entrySet()) {
                            gen.writeStartObject();
                            gen.writeStringField(JsonFields.CONDITIONAL_LINK_NODE_NAME, entry.getKey().getName());
                            gen.writeBooleanField(JsonFields.CONDITIONAL_LINK_VALUE, entry.getValue());
                            gen.writeEndObject();
                        }
                        gen.writeEndArray();
                        break;
                    default:
                        gen.writeStringField("Error", "Unsupported content type: " + content.getType());
                        break;
                }
                gen.writeEndObject();
                gen.writeArrayFieldStart(JsonFields.LINKS);
                List<Link> links = node.getLinks();
                for (Link link : links) {
                    gen.writeStartObject();
                    gen.writeStringField(JsonFields.LINK_NAME, link.getName().getValue());
                    gen.writeStringField(JsonFields.INITIAL_NODE_LINK, link.getInitialNode().getName());
                    gen.writeStringField(JsonFields.FINAL_NODE_LINK, link.getFinalNode().getName());
                    gen.writeBooleanField(JsonFields.LINK_CONDITION, link.getCondition());
                    gen.writeEndObject();
                }
                gen.writeEndArray();
                gen.writeEndObject();
            }
            gen.writeEndArray();
            gen.writeEndObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error serializing WorkFlowResponseBody", e);
        }
    }
}
