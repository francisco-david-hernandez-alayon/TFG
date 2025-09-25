package es.ull.project.adapter.rest.serialization;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import es.ull.project.adapter.rest.JsonFields;
import es.ull.project.adapter.rest.response.OperationResponseBody;

public class OperationResponseBodySerializer extends StdSerializer<OperationResponseBody> {

    public OperationResponseBodySerializer() {
        this(null);
    }

    public OperationResponseBodySerializer(Class<OperationResponseBody> op) {
        super(op);
    }

    @Override
    public void serialize(OperationResponseBody entity, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
        generator.writeStartObject();
        generator.writeStringField(JsonFields.OPERATION_NAME, entity.getOperationName().getName());
        generator.writeStringField(JsonFields.DOCKER_IMAGE_NAME, entity.getDockerImageName().getValue());
        generator.writeEndObject();
    }
}
