import { UllDockerImageName } from '@ull-tfg/ull-tfg-typescript/dist/ull-docker-image-name';
import { OperationName } from '../../../domain/value-object/operation-name';
import { Operation } from '../../../domain/value-object/operation';


export class OperationJsonResponse {
    public operationName: string;
    public dockerImageName: string;

    // Constructor
    constructor(operationName: string, dockerImageName: string) {
        this.operationName = operationName;
        this.dockerImageName = dockerImageName;
    }

    static toOperation(json: OperationJsonResponse): Operation {
        const name = new OperationName(json.operationName);
        const image = new UllDockerImageName(json.dockerImageName);
        return new Operation(name, image);
    }

    // ToString
    toString(): string {
        return `OperationJsonResponse={operationName=${this.operationName}, dockerImageName=${this.dockerImageName}}`;
    }
}
