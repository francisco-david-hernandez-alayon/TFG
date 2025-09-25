import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError, UllUUID } from '@ull-tfg/ull-tfg-typescript';
import type { OperationGetUseCase } from '../../usecase/operation/operation-get-use-case';
import { Operation } from '../../../domain/value-object/operation';
import type { OperationRepository } from '../../repository/operation-repository';
import { OperationName } from '@/domain/value-object/operation-name';

export class OperationGetService implements OperationGetUseCase {
    constructor(private readonly operationRepository: OperationRepository) {}
  
    async getOperation(operationName: OperationName): Promise<Either<DataError, Operation>> {
      return this.operationRepository.getOperation(operationName);
    }
  
    async getAllOperations(): Promise<Either<DataError, Operation[]>> {
      return this.operationRepository.getOperations();
    }
  }