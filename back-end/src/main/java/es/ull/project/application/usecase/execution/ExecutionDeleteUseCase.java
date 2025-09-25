package es.ull.project.application.usecase.execution;

import java.util.Optional;
import java.util.UUID;

import es.ull.project.domain.entity.Execution;

public interface ExecutionDeleteUseCase {
    

    Optional<Execution> deleteExecution(UUID executionId);
}
