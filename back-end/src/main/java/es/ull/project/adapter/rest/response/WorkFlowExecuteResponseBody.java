package es.ull.project.adapter.rest.response;

import es.ull.project.domain.valueobject.File;
import es.ull.project.domain.valueobject.NodeExecuteData;
import es.ull.project.domain.valueobject.WorkFlowName;
import es.ull.project.domain.entity.Execution;
import es.ull.project.domain.enumerate.ExecuteStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class WorkFlowExecuteResponseBody {

    private UUID id;
    private WorkFlowName workFlowName;
    private LocalDateTime creationDate;
    private ExecuteStatus status;
    private String message;
    private List<File> outputFiles;
    private List<NodeExecuteData> nodesExecuted;
    private Duration totalExecutionTime;
    private int numberOfOperationExecuted;

    public static WorkFlowExecuteResponseBody from(Execution data) {
        WorkFlowExecuteResponseBody body = new WorkFlowExecuteResponseBody();
        body.id = data.getId();
        body.workFlowName = data.getWorkFlowName();
        body.creationDate = data.getCreationDate();
        body.status = data.getStatus();
        body.message = data.getMessage();
        body.outputFiles = data.getOutputFiles();
        body.nodesExecuted = data.getNodesExecuted();
        body.totalExecutionTime = data.getTotalExecutionTime();
        body.numberOfOperationExecuted = data.getNumberOfOperationExecuted();
        return body;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public WorkFlowName getWorkFlowName() {
        return workFlowName;
    }

    public void setWorkFlowName(WorkFlowName workFlowName) {
        this.workFlowName = workFlowName;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ExecuteStatus getStatus() {
        return status;
    }

    public void setStatus(ExecuteStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<File> getOutputFiles() {
        return outputFiles;
    }

    public void setOutputFiles(List<File> outputFiles) {
        this.outputFiles = outputFiles;
    }

    public List<NodeExecuteData> getNodesExecuted() {
        return nodesExecuted;
    }

    public void setNodesExecuted(List<NodeExecuteData> nodesExecuted) {
        this.nodesExecuted = nodesExecuted;
    }

    public Duration getTotalExecutionTime() {
        return totalExecutionTime;
    }

    public void setTotalExecutionTime(Duration totalExecutionTime) {
        this.totalExecutionTime = totalExecutionTime;
    }

    public int getNumberOfOperationExecuted() {
        return numberOfOperationExecuted;
    }

    public void setNumberOfOperationExecuted(int numberOfOperationExecuted) {
        this.numberOfOperationExecuted = numberOfOperationExecuted;
    }

    @Override
    public String toString() {
        return String.format(
                "WorkFlowExecuteResponseBody={status=%s, message=%s, outputFiles=%s, nodesExecuted=%s, executionTime=%s, numberOfOperationExecuted=%d}",
                this.status, this.message, this.outputFiles, this.nodesExecuted, this.totalExecutionTime,
                this.numberOfOperationExecuted);
    }
}
