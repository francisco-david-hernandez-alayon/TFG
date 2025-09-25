package es.ull.project.application.usecase.operation;

import java.util.List;
import java.util.Optional;

import es.ull.project.domain.valueobject.Operation;
import es.ull.project.domain.valueobject.OperationName;

public interface OperationGetUseCase {
    /**
     * Get an operation by Name
     * 
     * @param operation
     * @return operation selected
     */
    Optional<Operation> getOperation(OperationName operationName);

    /**
     * Get all operations
     * 
     * @return all operations
     */
    List<Operation> getAllOperations();
}
