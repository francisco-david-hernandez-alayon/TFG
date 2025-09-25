import { UllUUID } from '@ull-tfg/ull-tfg-typescript';
import { Operation } from '../../../domain/value-object/operation';
import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError } from '@ull-tfg/ull-tfg-typescript';
import { OperationName } from '@/domain/value-object/operation-name';

export interface OperationGetUseCase {
    /**
     * Get an operation by name
     * @param operationName
     * @returns operation selected
     */
    getOperation(operationName: OperationName): Promise<Either<DataError, Operation>>;

    /**
     * Get all operations
     * @returns all operations
     */
    getAllOperations(): Promise<Either<DataError, Operation[]>>;
}
