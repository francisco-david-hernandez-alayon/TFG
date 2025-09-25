package es.ull.project.application.service.execution;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import es.ull.project.application.repository.ExecutionRepository;
import es.ull.project.application.usecase.execution.ExecutionDeleteUseCase;
import es.ull.project.domain.entity.Execution;

public class ExecutionDeleteService implements ExecutionDeleteUseCase {

    @Autowired
    private ExecutionRepository repository;

    @Override
    public Optional<Execution> deleteExecution(UUID executionId) {
        return Optional.ofNullable(repository.delete(executionId));
    }
}
