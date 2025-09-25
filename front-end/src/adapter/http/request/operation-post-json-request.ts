import { Operation } from "../../../domain/value-object/operation";

export class OperationPostJsonRequest {
    public operationName: string;
    public dockerImageName: string;

    // Constructor
    constructor(operationName: string, dockerImageName: string) {
        this.operationName = operationName;
        this.dockerImageName = dockerImageName;
    }

    static toRequest(operation: Operation): OperationPostJsonRequest {
        return new OperationPostJsonRequest(operation.getName().getName(), operation.getDockerImage().getValue());
    }

    // toString
    toString(): string {
        return `OperationPostJsonRequest={operationName=${this.operationName}, dockerImageName=${this.dockerImageName}}`;
    }
}
