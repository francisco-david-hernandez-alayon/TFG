package es.ull.project.adapter.rest.deserialization;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;

import es.ull.project.adapter.rest.JsonFields;
import es.ull.project.adapter.rest.request.OperationPostRequestBody;
import es.ull.project.domain.valueobject.OperationName;
import es.ull.utils.docker.UllDockerImageName;
import es.ull.utils.rest.error.ApiSubError;
import es.ull.utils.rest.error.ApiSubErrorMessageRejectedValueField;
import es.ull.utils.rest.serialization.UllJsonDeserializer;

public class OperationPostRequestBodyDeserializer extends UllJsonDeserializer<OperationPostRequestBody> {

    private static final String ERROR_OPERATION_NAME_NOT_DEFINED = "Name for Operation not defined";
    private static final String ERROR_DOCKER_IMAGE_NAME_NOT_DEFINED = "Docker image name for Operation not defined";

    public OperationPostRequestBody parse(JsonNode operationNode) {
        validate(operationNode);
        OperationPostRequestBody parsedRequest = new OperationPostRequestBody();
        UllDockerImageName dockerName = new UllDockerImageName(operationNode.get(JsonFields.DOCKER_IMAGE_NAME).textValue());
        OperationName operationName = new OperationName(operationNode.get(JsonFields.OPERATION_NAME).textValue());
        parsedRequest.setOperationName(operationName);
        parsedRequest.setDockerImageName(dockerName);
        return parsedRequest;
    }

    @Override
    protected Optional<List<ApiSubError>> validate(JsonNode node) {
        List<ApiSubError> errors = new ArrayList<>();
        errors.addAll(this.validateName(node));
        errors.addAll(this.validateDockerImage(node));
        return (errors.isEmpty()) ? Optional.empty() : Optional.of(errors);
    }

    private List<ApiSubError> validateName(JsonNode jsonNode) {
        List<ApiSubError> errors = new ArrayList<>();
        if (jsonNode.has(JsonFields.OPERATION_NAME)) {
            final String name = jsonNode.get(JsonFields.OPERATION_NAME).textValue();
            try {
                new OperationName(name);
            } catch (Exception exception) {
                errors.add(new ApiSubErrorMessageRejectedValueField(
                    exception.getMessage(),
                    name,
                    JsonFields.OPERATION_NAME));
            }
        } else {
            errors.add(new ApiSubErrorMessageRejectedValueField(
                ERROR_OPERATION_NAME_NOT_DEFINED,
                JsonFields.OPERATION_NAME));
        }
        return errors;
    }

    private List<ApiSubError> validateDockerImage(JsonNode jsonNode) {
        List<ApiSubError> errors = new ArrayList<>();
        if (jsonNode.has(JsonFields.DOCKER_IMAGE_NAME)) {
            final String name = jsonNode.get(JsonFields.DOCKER_IMAGE_NAME).textValue();
            try {
                new UllDockerImageName(name);
            } catch (Exception exception) {
                errors.add(new ApiSubErrorMessageRejectedValueField(
                    exception.getMessage(),
                    name,
                    JsonFields.DOCKER_IMAGE_NAME));
            }
        } else {
            errors.add(new ApiSubErrorMessageRejectedValueField(
                ERROR_DOCKER_IMAGE_NAME_NOT_DEFINED,
                JsonFields.DOCKER_IMAGE_NAME));
        }
        return errors;
    }
}
