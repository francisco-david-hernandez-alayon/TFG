package es.ull.project.application.service.execution;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import es.ull.project.application.repository.ExecutionRepository;
import es.ull.project.application.usecase.execution.ExecutionGetUseCase;
import es.ull.project.domain.entity.Execution;

public class ExecutionGetService implements ExecutionGetUseCase {

    @Autowired
    private ExecutionRepository repository;

    @Override
    public Optional<Execution> getExecution(UUID executionId) {
        return Optional.ofNullable(repository.fetchById(executionId));
    }

    @Override
    public List<Execution> getAllExecutions() {
        return repository.fetchAll();
    }
}
