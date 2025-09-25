import { Operation } from '../../../domain/value-object/operation';
import { OperationName } from '../../../domain/value-object/operation-name';
import { UllDockerImageName } from '@ull-tfg/ull-tfg-typescript/dist/ull-docker-image-name';
import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError } from '@ull-tfg/ull-tfg-typescript';
import { NodeIcon } from '@/domain/enumerate/node-icon';

export interface OperationCreateUseCase {
    /**
     * Create an Operation
     * @param name
     * @param dockerImage
     * @param icon
     * @returns operation created
     */
    createOperation(name: OperationName, dockerImage: UllDockerImageName): Promise<Either<DataError, Operation>>;
}
