package es.ull.project.adapter.rest.response;

import es.ull.project.domain.valueobject.Operation;
import es.ull.project.domain.valueobject.OperationName;
import es.ull.utils.docker.UllDockerImageName;

public class OperationResponseBody {

    private OperationName operationName;
    private UllDockerImageName dockerImageName;

    public static OperationResponseBody from(Operation operation) {
        OperationResponseBody responseBody = new OperationResponseBody();
        responseBody.operationName = operation.getName();
        responseBody.dockerImageName = operation.getDockerImage();
        return responseBody;
    }

    public OperationName getOperationName() {
        return operationName;
    }

    public void setOperationName(OperationName operationName) {
        this.operationName = operationName;
    }

    public UllDockerImageName getDockerImageName() {
        return dockerImageName;
    }

    public void setDockerImageName(UllDockerImageName dockerImageName) {
        this.dockerImageName = dockerImageName;
    }

    @Override
    public String toString() {
        return String.format(
            "OperationResponseBody={operationName=%s, dockerImageName=%s}",
            operationName, dockerImageName);
    }
}
