package es.ull.project.application.usecase.operation;

import java.util.Optional;

import es.ull.project.domain.valueobject.Operation;
import es.ull.project.domain.valueobject.OperationName;
import es.ull.utils.docker.UllDockerImageName;

public interface OperationUpdateUseCase {
    /**
     * Update an operation
     * 
     * @param operation
     * @param operationName
     * @param dockerName
     * @return operation updated
     */
    Optional<Operation> updateOperation(OperationName oldOperationName, OperationName newOperationName, UllDockerImageName dockerImage);
}
