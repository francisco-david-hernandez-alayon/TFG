package es.ull.project.application.service.execution;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import es.ull.project.application.repository.ExecutionRepository;
import es.ull.project.application.usecase.execution.ExecutionCreateUseCase;
import es.ull.project.domain.entity.Execution;
import es.ull.project.domain.enumerate.ExecuteStatus;
import es.ull.project.domain.valueobject.File;
import es.ull.project.domain.valueobject.NodeExecuteData;
import es.ull.project.domain.valueobject.WorkFlowName;

public class ExecutionCreateService implements ExecutionCreateUseCase {

    @Autowired
    private ExecutionRepository repository;

    @Override
    public Execution createExecution(WorkFlowName workFlowName, ExecuteStatus status,
            String message, List<File> outputFiles, List<NodeExecuteData> nodesExecuted, Duration totalExecutionTime,
            int numberOfOperationExecuted) {
        Execution execution = new Execution(workFlowName, status, message, outputFiles, nodesExecuted,
                totalExecutionTime, numberOfOperationExecuted);
        return repository.save(execution);
    }
}
