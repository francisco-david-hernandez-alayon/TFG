import { Either } from '@ull-tfg/ull-tfg-typescript';
import { Operation } from '../../domain/value-object/operation';
import type { DataError } from '@ull-tfg/ull-tfg-typescript';
import { OperationName } from '@/domain/value-object/operation-name';

export interface OperationRepository {
    getOperations(): Promise<Either<DataError, Operation[]>>;
    getOperation(name: OperationName): Promise<Either<DataError, Operation>>;
    saveOperation(operation: Operation): Promise<Either<DataError, Operation>>;
    deleteOperation(name: OperationName): Promise<Either<DataError, Operation>>;
    updateOperation(name: OperationName, operation: Operation): Promise<Either<DataError, Operation>>;
}