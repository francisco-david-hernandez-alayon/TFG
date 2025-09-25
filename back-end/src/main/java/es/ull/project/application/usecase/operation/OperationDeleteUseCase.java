package es.ull.project.application.usecase.operation;

import java.util.Optional;

import es.ull.project.domain.valueobject.Operation;
import es.ull.project.domain.valueobject.OperationName;

public interface OperationDeleteUseCase {
    
    /**
     * Delete an operation
     * 
     * @param operation
     * @return operation deleted
     */
    Optional<Operation> deleteOperation(OperationName operationName);
}
