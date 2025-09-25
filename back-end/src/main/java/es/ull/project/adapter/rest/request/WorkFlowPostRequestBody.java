package es.ull.project.adapter.rest.request;

import java.util.List;

import es.ull.project.domain.valueobject.WorkFlowName;
import es.ull.project.domain.valueobject.Node;

public class WorkFlowPostRequestBody {

    private WorkFlowName name;
    private List<Node> nodes;
    private boolean enabled;

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public WorkFlowName getName() {
        return name;
    }

    public void setName(WorkFlowName name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return String.format(
                "WorkflowPostRequestBody={flowName=%s, nodes=%s, enabled=%s}",
                this.name, this.nodes, this.enabled);
    }
}
