import { UllUUID } from '@ull-tfg/ull-tfg-typescript';
import { Operation } from '../../../domain/value-object/operation';
import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError } from '@ull-tfg/ull-tfg-typescript';
import { OperationName } from '@/domain/value-object/operation-name';

export interface OperationDeleteUseCase {
    /**
     * Delete an operation
     * @param operationId
     * @returns operation deleted
     */
    deleteOperation(name: OperationName): Promise<Either<DataError, Operation>>;
}
