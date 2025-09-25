package es.ull.project.adapter.rest.request;

import es.ull.project.domain.valueobject.Operation;
import es.ull.project.domain.valueobject.OperationName;
import es.ull.utils.docker.UllDockerImageName;

public class OperationPutRequestBody {

    private OperationName operationName;
    private UllDockerImageName dockerImageName;

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

    public Operation toOperation() {
        return new Operation(this.operationName, this.dockerImageName);
    }

    @Override
    public String toString() {
        return String.format(
                "OperationPutRequestBody={operationName=%s, dockerImageName=%s}",
                this.operationName, this.dockerImageName);
    }
}
