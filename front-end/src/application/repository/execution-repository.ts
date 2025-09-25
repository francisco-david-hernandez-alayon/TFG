import { UllUUID } from '@ull-tfg/ull-tfg-typescript';
import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError } from '@ull-tfg/ull-tfg-typescript';
import { Execution } from '@/domain/entity/execution';

export interface ExecutionRepository {
    getExecutions(): Promise<Either<DataError, Execution[]>>;
    getExecution(id: UllUUID): Promise<Either<DataError, Execution>>;
}