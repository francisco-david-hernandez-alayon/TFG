package es.ull.project.application.service.execution;

import es.ull.project.application.repository.ExecutionRepository;
import es.ull.project.application.usecase.execution.ExecutionUpdateUseCase;
import es.ull.project.domain.entity.Execution;
import es.ull.project.domain.enumerate.ExecuteStatus;
import es.ull.project.domain.valueobject.File;
import es.ull.project.domain.valueobject.NodeExecuteData;
import es.ull.project.domain.valueobject.WorkFlowName;

import java.util.List;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

public class ExecutionUpdateService implements ExecutionUpdateUseCase {

    @Autowired
    private ExecutionRepository repository;

    @Override
    public Optional<Execution> updateExecution(UUID executionId, WorkFlowName workFlowName, ExecuteStatus status,
            String message, List<File> outputFiles, List<NodeExecuteData> nodesExecuted, Duration totalExecutionTime,
            int numberOfOperationExecuted) {
                
        Execution existingExecution = repository.fetchById(executionId);
        final Execution updatedExecution = new Execution(
                executionId,
                workFlowName,
                existingExecution.getCreationDate(),
                status,
                message,
                outputFiles,
                nodesExecuted,
                totalExecutionTime,
                numberOfOperationExecuted);
        return Optional.ofNullable(repository.update(executionId, updatedExecution));
    }

}
