package es.ull.project.application.service.operation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import es.ull.project.application.repository.OperationRepository;
import es.ull.project.application.usecase.operation.OperationGetUseCase;
import es.ull.project.domain.valueobject.Operation;
import es.ull.project.domain.valueobject.OperationName;

public class OperationGetService implements OperationGetUseCase {

    @Autowired
    private OperationRepository repository;

    @Override
    public Optional<Operation> getOperation(OperationName operationName) {
        return Optional.ofNullable(repository.fetchByName(operationName));
    }

    @Override
    public List<Operation> getAllOperations() {
        return repository.fetchAll();
    }
}
