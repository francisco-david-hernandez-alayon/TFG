import { UllUUID } from '@ull-tfg/ull-tfg-typescript';
import { Operation } from '../../../domain/value-object/operation';
import { OperationName } from '../../../domain/value-object/operation-name';
import { UllDockerImageName } from '@ull-tfg/ull-tfg-typescript/dist/ull-docker-image-name';
import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError } from '@ull-tfg/ull-tfg-typescript';
import { NodeIcon } from '@/domain/enumerate/node-icon';

export interface OperationUpdateUseCase {
    /**
     * Update an operation
     * @param oldOperationName
     * @param operationName
     * @param dockerImage
     * @param icon
     * @returns operation updated
     */
    updateOperation(
        oldOperationName: OperationName,
        operationName: OperationName,
        dockerImage: UllDockerImageName,
    ): Promise<Either<DataError, Operation>>;
}
