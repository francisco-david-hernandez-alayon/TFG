package es.ull.project.application.usecase.execution;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import es.ull.project.domain.entity.Execution;
import es.ull.project.domain.enumerate.ExecuteStatus;
import es.ull.project.domain.valueobject.File;
import es.ull.project.domain.valueobject.NodeExecuteData;
import es.ull.project.domain.valueobject.WorkFlowName;

public interface ExecutionUpdateUseCase {

    /**
     * @brief Update an Execution
     * @param executionId
     * @param workFlowName
     * @param status
     * @param message
     * @param outputFiles
     * @param nodesExecuted
     * @param totalExecutionTime
     * @param numberOfOperationExecuted
     * @return
     */
    Optional<Execution> updateExecution(UUID executionId, WorkFlowName workFlowName, ExecuteStatus status,
            String message, List<File> outputFiles, List<NodeExecuteData> nodesExecuted, Duration totalExecutionTime,
            int numberOfOperationExecuted);
}
