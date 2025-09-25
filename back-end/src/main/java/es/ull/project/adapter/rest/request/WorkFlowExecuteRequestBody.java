package es.ull.project.adapter.rest.request;

import java.net.URI;
import java.util.Map;

import es.ull.project.domain.valueobject.NodeName;

/**
 * @brief Request body to execute a workflow.
 */
public class WorkFlowExecuteRequestBody {

    private Map<NodeName, URI> pendingUriFiles;

    public Map<NodeName, URI> getPendingUriFiles() {
        return pendingUriFiles;
    }

    public void setPendingUriFiles(Map<NodeName, URI> pendingUriFiles) {
        this.pendingUriFiles = pendingUriFiles;
    }

    @Override
    public String toString() {
        return String.format(
                "WorkFlowExecuteRequestBody={pendingUriFiles=%s}",
                this.pendingUriFiles);
    }
}
