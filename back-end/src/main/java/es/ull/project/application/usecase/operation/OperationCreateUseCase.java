package es.ull.project.application.usecase.operation;

import es.ull.project.domain.valueobject.Operation;
import es.ull.project.domain.valueobject.OperationName;
import es.ull.utils.docker.UllDockerImageName;

public interface OperationCreateUseCase {

    /**
     * Create an Operation
     * 
     * @param name
     * @param operationName
     * @param dockerName
     * @return operation created
     */
    Operation createOperation(OperationName name, UllDockerImageName dockerImage);
}
