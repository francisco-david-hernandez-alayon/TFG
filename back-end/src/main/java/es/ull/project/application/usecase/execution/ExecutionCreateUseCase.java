package es.ull.project.application.usecase.execution;

import java.time.Duration;
import java.util.List;

import es.ull.project.domain.entity.Execution;
import es.ull.project.domain.enumerate.ExecuteStatus;
import es.ull.project.domain.valueobject.File;
import es.ull.project.domain.valueobject.NodeExecuteData;
import es.ull.project.domain.valueobject.WorkFlowName;

public interface ExecutionCreateUseCase {

    /**
     * @brief Create an Execution
     * @param workFlowName
     * @param status
     * @param message
     * @param outputFiles
     * @param nodesExecuted
     * @param totalExecutionTime
     * @param numberOfOperationExecuted
     * @return
     */
    Execution createExecution(WorkFlowName workFlowName, ExecuteStatus status,
            String message, List<File> outputFiles, List<NodeExecuteData> nodesExecuted, Duration totalExecutionTime,
            int numberOfOperationExecuted);
}
