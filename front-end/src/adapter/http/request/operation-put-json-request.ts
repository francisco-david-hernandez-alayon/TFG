import { Operation } from "../../../domain/value-object/operation";

export class OperationPutJsonRequest {
    public operationName: string;
    public dockerImageName: string;

    // Constructor
    constructor(operationName: string, dockerImageName: string) {
        this.operationName = operationName;
        this.dockerImageName = dockerImageName;
    }

    static toRequest(operation: Operation): OperationPutJsonRequest {
        return new OperationPutJsonRequest(operation.getName().getName(), operation.getDockerImage().getValue());
    }

    // toString
    toString(): string {
        return `OperationPutJsonRequest={operationName=${this.operationName}, dockerImageName=${this.dockerImageName}}`;
    }
}