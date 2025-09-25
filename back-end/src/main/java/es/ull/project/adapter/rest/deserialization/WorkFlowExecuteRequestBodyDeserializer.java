package es.ull.project.adapter.rest.deserialization;

import com.fasterxml.jackson.databind.JsonNode;
import es.ull.project.adapter.rest.JsonFields;
import es.ull.project.adapter.rest.request.WorkFlowExecuteRequestBody;
import es.ull.project.domain.valueobject.NodeName;

import es.ull.utils.rest.error.ApiSubError;
import es.ull.utils.rest.serialization.UllJsonDeserializer;

import java.net.URI;
import java.util.*;

/**
 * @brief Deserializer for WorkFlowExecuteRequestBody.
 */
public class WorkFlowExecuteRequestBodyDeserializer extends UllJsonDeserializer<WorkFlowExecuteRequestBody> {

    @Override
    public WorkFlowExecuteRequestBody parse(JsonNode rootNode) {
        Map<NodeName, URI> pendingUriFiles = new HashMap<>();
        JsonNode pendingFilesNode = rootNode.get(JsonFields.PENDING_URI_FILES);
        if (pendingFilesNode != null && pendingFilesNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = pendingFilesNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                NodeName nodeName = new NodeName(entry.getKey());
                URI uri = URI.create(entry.getValue().asText());
                pendingUriFiles.put(nodeName, uri);
            }
        }
        WorkFlowExecuteRequestBody request = new WorkFlowExecuteRequestBody();
        request.setPendingUriFiles(pendingUriFiles);
        return request;
    }

    @Override
    protected Optional<List<ApiSubError>> validate(JsonNode rootNode) {
        return Optional.empty();
    }
}
