import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError, UllUUID } from '@ull-tfg/ull-tfg-typescript';
import type { OperationDeleteUseCase } from '../../usecase/operation/operation-delete-use-case';
import { Operation } from '../../../domain/value-object/operation';
import type { OperationRepository } from '../../repository/operation-repository';
import { OperationName } from '@/domain/value-object/operation-name';

export class OperationDeleteService implements OperationDeleteUseCase {

    constructor(private readonly operationRepository: OperationRepository) { }

    async deleteOperation(name: OperationName): Promise<Either<DataError, Operation>> {
        return this.operationRepository.deleteOperation(name); 
    }
}