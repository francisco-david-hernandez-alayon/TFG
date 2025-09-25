package es.ull.project.domain.valueobject;

import es.ull.project.domain.enumerate.NodeContentType;
import es.ull.utils.docker.UllDockerImageName;

public class Operation extends NodeContent {

    private static final String ERROR_OPERATION_NAME_NOT_DEFINED = "Name for Operation not defined";
    private static final String ERROR_DOCKER_IMAGE_NOT_DEFINED = "Docker Image not defined";
    private final OperationName name;
    private final UllDockerImageName dockerImage;

    public Operation(OperationName name, UllDockerImageName dockerImage) {
        validateName(name);
        validateDockerImage(dockerImage);
        this.name = name;
        this.dockerImage = dockerImage;
    }

    private void validateName(OperationName name) {
        if (name == null) {
            throw new IllegalArgumentException(ERROR_OPERATION_NAME_NOT_DEFINED);
        }
    }

    private void validateDockerImage(UllDockerImageName dockerImage) {
        if (dockerImage == null) {
            throw new IllegalArgumentException(ERROR_DOCKER_IMAGE_NOT_DEFINED);
        }
    }

    public OperationName getName() {
        return this.name;
    }

    public UllDockerImageName getDockerImage() {
        return this.dockerImage;
    }

    public Operation setName(OperationName name) {
        return new Operation(name, this.dockerImage);
    }

    public Operation setDockerImage(UllDockerImageName dockerImage) {
        return new Operation(this.name, dockerImage);
    }

    @Override
    public NodeContentType getType() {
        return NodeContentType.OPERATION;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (!(otherObject instanceof Operation)) {
            return false;
        }
        final Operation that = (Operation) otherObject;
        return this.name.equals(that.name) && this.dockerImage.equals(that.dockerImage);
    }

    @Override
    public int hashCode() {
        return 31 * name.hashCode() + dockerImage.hashCode();
    }

    @Override
    public String toString() {
        return String.format(
                "Operation={name=%s, dockerImage=%s}",
                this.name, this.dockerImage);
    }
}
