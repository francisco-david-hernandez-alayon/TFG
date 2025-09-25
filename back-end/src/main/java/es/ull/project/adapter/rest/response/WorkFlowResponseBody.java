package es.ull.project.adapter.rest.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import es.ull.project.domain.entity.WorkFlow;
import es.ull.project.domain.valueobject.WorkFlowName;
import es.ull.project.domain.valueobject.Node;

public class WorkFlowResponseBody {

    private UUID id;
    private WorkFlowName name;
    private List<Node> nodes;
    private LocalDateTime creationDate;
    private boolean enabled;

    public static WorkFlowResponseBody from(WorkFlow workflow) {
        WorkFlowResponseBody responseBody = new WorkFlowResponseBody();
        responseBody.id = workflow.getId();
        responseBody.nodes = workflow.getNodes();
        responseBody.creationDate = workflow.getCreationDate();
        responseBody.name = workflow.getName();
        responseBody.enabled = workflow.isEnabled();
        return responseBody;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
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
                "FlowResponseBody={id=%s, flowName=%s, creationDate=%s, nodes=%s, enabled=%s}",
                this.id, this.name, this.creationDate, this.nodes, this.enabled);
    }
}