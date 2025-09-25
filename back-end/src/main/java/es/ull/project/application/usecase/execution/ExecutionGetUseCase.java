package es.ull.project.application.usecase.execution;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import es.ull.project.domain.entity.Execution;

public interface ExecutionGetUseCase {
    /**
     * Get an Execution by ID
     * 
     * @param UUID
     * @return Execution selected
     */
    Optional<Execution> getExecution(UUID executionId);

    /**
     * Get all Execution
     * 
     * @return all Execution
     */
    List<Execution> getAllExecutions();
}
