package es.ull.project.domain.entity;

import es.ull.project.domain.enumerate.ExecuteStatus;
import es.ull.project.domain.valueobject.File;
import es.ull.project.domain.valueobject.NodeExecuteData;
import es.ull.project.domain.valueobject.WorkFlowName;

import java.util.ArrayList;
import java.util.Collections;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @brief Value object representing the result of an execution.
 */
public class Execution {

    private static final String ERROR_STATUS_NOT_DEFINED = "Execute status not defined";
    private static final String ERROR_MESSAGE_NOT_DEFINED = "Message not defined";
    private static final String ERROR_OUTPUT_FILE_NOT_DEFINED = "Output file not defined";
    private static final String ERROR_NODES_EXECUTED_NOT_DEFINED = "Executed nodes list not defined";
    private static final String ERROR_TOTAL_EXECUTION_TIME_NOT_DEFINED = "Total execution time not defined";
    private static final String ERROR_NUMBER_OF_OPERATION_EXECUTED_NOT_DEFINED = "Number of operations executed not defined";
    private static final String ERROR_CREATION_DATE_NOT_DEFINED = "Creation date not defined";
    private static final String ERROR_WORKFLOW_NAME_NOT_DEFINED = "Workflow name not defined";
    
    private final UUID id;
    private final WorkFlowName workFlowName;
    private final LocalDateTime creationDate;
    
    private final ExecuteStatus status;
    private final String message;
    private final List<File> outputFiles;
    private final List<NodeExecuteData> nodesExecuted;
    private final Duration totalExecutionTime;
    private final int numberOfOperationExecuted;

    public Execution(
            WorkFlowName workFlowName,
            ExecuteStatus status,
            String message,
            List<File> outputFiles,
            List<NodeExecuteData> nodesExecuted,
            Duration totalExecutionTime,
            int numberOfOperationExecuted) {
        validateWorkFlowName(workFlowName);        
        validateStatus(status);
        validateMessage(message);
        validateOutputFile(outputFiles);
        validateNodesExecuted(nodesExecuted);
        validateTotalExecutionTime(totalExecutionTime);

        this.workFlowName = workFlowName;
        this.status = status;
        this.message = message;
        this.outputFiles = outputFiles;
        this.nodesExecuted = nodesExecuted;
        this.totalExecutionTime = totalExecutionTime;
        this.numberOfOperationExecuted = numberOfOperationExecuted;

        this.id = UUID.randomUUID();
        this.creationDate = LocalDateTime.now();
    }

    public Execution(Execution other) {
        this.id = other.id;
        this.workFlowName = other.workFlowName;
        this.creationDate = other.creationDate;
        this.status = other.status;
        this.message = other.message;
        this.outputFiles = new ArrayList<>(other.outputFiles);
        this.nodesExecuted = new ArrayList<>(other.nodesExecuted);
        this.totalExecutionTime = other.totalExecutionTime;
        this.numberOfOperationExecuted = other.numberOfOperationExecuted;
    }

    public Execution(
            UUID id,
            WorkFlowName workFlowName,
            LocalDateTime creationDate,
            ExecuteStatus status,
            String message,
            List<File> outputFiles,
            List<NodeExecuteData> nodesExecuted,
            Duration totalExecutionTime,
            int numberOfOperationExecuted) {
        validateWorkFlowName(workFlowName);
        validateStatus(status);
        validateMessage(message);
        validateOutputFile(outputFiles);
        validateNodesExecuted(nodesExecuted);
        validateTotalExecutionTime(totalExecutionTime);
        validateNumberOfOperationExecuted(numberOfOperationExecuted);
        validateCreationDate(creationDate);

        this.id = id;
        this.workFlowName = workFlowName;
        this.creationDate = creationDate;
        this.status = status;
        this.message = message;
        this.outputFiles = outputFiles;
        this.nodesExecuted = nodesExecuted;
        this.totalExecutionTime = totalExecutionTime;
        this.numberOfOperationExecuted = numberOfOperationExecuted;
    }

    public UUID getId() {
        return id;
    }

    public WorkFlowName getWorkFlowName() {
        return workFlowName;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public ExecuteStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<File> getOutputFiles() {
        return outputFiles;
    }

    public List<NodeExecuteData> getNodesExecuted() {
        return Collections.unmodifiableList(this.nodesExecuted);
    }

    public Duration getTotalExecutionTime() {
        return totalExecutionTime;
    }

    public int getNumberOfOperationExecuted() {
        return numberOfOperationExecuted;
    }

    public Execution setWorkFlowName(WorkFlowName workFlowName) {
        validateWorkFlowName(workFlowName);
        return new Execution(
                workFlowName,
                this.status,
                this.message,
                this.outputFiles,
                this.nodesExecuted,
                this.totalExecutionTime,
                this.numberOfOperationExecuted);
    }

    public Execution setStatus(ExecuteStatus status) {
        validateStatus(status);
        return new Execution(
                this.workFlowName,
                status,
                this.message,
                this.outputFiles,
                this.nodesExecuted,
                this.totalExecutionTime,
                this.numberOfOperationExecuted);
    }

    public Execution setMessage(String message) {
        validateMessage(message);
        return new Execution(
                this.workFlowName,
                this.status,
                message,
                this.outputFiles,
                this.nodesExecuted,
                this.totalExecutionTime,
                this.numberOfOperationExecuted);
    }

    public Execution setOutputFile(File outputFile) {
        validateOutputFile(List.of(outputFile));
        return new Execution(
                this.workFlowName,
                this.status,
                this.message,
                List.of(outputFile),
                this.nodesExecuted,
                this.totalExecutionTime,
                this.numberOfOperationExecuted);
    }

    public Execution setOutputFiles(List<File> outputFiles) {
        validateOutputFile(outputFiles);
        return new Execution(
                this.workFlowName,
                this.status,
                this.message,
                outputFiles,
                this.nodesExecuted,
                this.totalExecutionTime,
                this.numberOfOperationExecuted);
    }

    public Execution setNodesExecuted(List<NodeExecuteData> nodesExecuted) {
        validateNodesExecuted(nodesExecuted);
        return new Execution(
                this.workFlowName,
                this.status,
                this.message,
                this.outputFiles,
                nodesExecuted,
                this.totalExecutionTime,
                this.numberOfOperationExecuted);
    }

    public Execution setTotalExecutionTime(Duration totalExecutionTime) {
        validateTotalExecutionTime(totalExecutionTime);
        return new Execution(
                this.workFlowName,
                this.status,
                this.message,
                this.outputFiles,
                this.nodesExecuted,
                totalExecutionTime,
                this.numberOfOperationExecuted);
    }

    public Execution setNumberOfOperationExecuted(int numberOfOperationExecuted) {
        validateNumberOfOperationExecuted(numberOfOperationExecuted);
        return new Execution(
                this.workFlowName,
                this.status,
                this.message,
                this.outputFiles,
                this.nodesExecuted,
                this.totalExecutionTime,
                numberOfOperationExecuted);
    }


    private void validateWorkFlowName(WorkFlowName workFlowName) {
        if (workFlowName == null) {
            throw new IllegalArgumentException(ERROR_WORKFLOW_NAME_NOT_DEFINED);
        }
    }

    private void validateCreationDate(LocalDateTime creationDate) {
        if (creationDate == null) {
            throw new IllegalArgumentException(ERROR_CREATION_DATE_NOT_DEFINED);
        }
    }
    private void validateStatus(ExecuteStatus status) {
        if (status == null) {
            throw new IllegalArgumentException(ERROR_STATUS_NOT_DEFINED);
        }
    }

    private void validateMessage(String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_DEFINED);
        }
    }

    private void validateOutputFile(List<File> outputFile) {
        if (outputFile == null) {
            throw new IllegalArgumentException(ERROR_OUTPUT_FILE_NOT_DEFINED);
        }
    }

    private void validateNodesExecuted(List<NodeExecuteData> nodesExecuted) {
        if (nodesExecuted == null) {
            throw new IllegalArgumentException(ERROR_NODES_EXECUTED_NOT_DEFINED);
        }
    }

    private void validateTotalExecutionTime(Duration totalExecutionTime) {
        if (totalExecutionTime == null) {
            throw new IllegalArgumentException(ERROR_TOTAL_EXECUTION_TIME_NOT_DEFINED);
        }
    }

    private void validateNumberOfOperationExecuted(int numberOfOperationExecuted) {
        if (numberOfOperationExecuted < 0) {
            throw new IllegalArgumentException(ERROR_NUMBER_OF_OPERATION_EXECUTED_NOT_DEFINED);
        }
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (!(otherObject instanceof Execution)) {
            return false;
        }
        Execution that = (Execution) otherObject;
        return (status.equals(that.status)) &&
                Objects.equals(message, that.message) &&
                Objects.equals(outputFiles, that.outputFiles) &&
                Objects.equals(nodesExecuted, that.nodesExecuted) &&
                Objects.equals(totalExecutionTime, that.totalExecutionTime) &&
                (numberOfOperationExecuted == that.numberOfOperationExecuted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message, outputFiles, nodesExecuted, totalExecutionTime, numberOfOperationExecuted);
    }

    @Override
    public String toString() {
        return String.format(
                "ExecuteData={id=%s, workflowName=%s, creationDate=%s status=%s, message='%s', outputFiles=%s, nodesExecuted=%s, totalExecutionTime=%s, numberOfOperationExecuted=%d}",
                id, workFlowName, creationDate, status, message, outputFiles, nodesExecuted, totalExecutionTime, numberOfOperationExecuted);
    }
}
