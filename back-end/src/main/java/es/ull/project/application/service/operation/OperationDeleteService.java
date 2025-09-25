package es.ull.project.application.service.operation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import es.ull.project.application.repository.OperationRepository;
import es.ull.project.application.usecase.operation.OperationDeleteUseCase;
import es.ull.project.domain.valueobject.Operation;
import es.ull.project.domain.valueobject.OperationName;

public class OperationDeleteService implements OperationDeleteUseCase {

    @Autowired
    private OperationRepository repository;

    @Override
    public Optional<Operation> deleteOperation(OperationName operationName) {
        return Optional.ofNullable(repository.delete(operationName));
    }
}
