import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError, UllUUID } from '@ull-tfg/ull-tfg-typescript';
import type { OperationUpdateUseCase } from '../../usecase/operation/operation-update-use-case';
import { Operation } from '../../../domain/value-object/operation';
import { OperationName } from '../../../domain/value-object/operation-name';
import { UllDockerImageName } from '@ull-tfg/ull-tfg-typescript/dist/ull-docker-image-name';
import type { OperationRepository } from '../../repository/operation-repository';
import { NodeIcon } from '@/domain/enumerate/node-icon';

export class OperationUpdateService implements OperationUpdateUseCase {
    constructor(private readonly operationRepository: OperationRepository) {}
  
    async updateOperation(oldOperationName: OperationName, operationName: OperationName, dockerImage: UllDockerImageName): Promise<Either<DataError, Operation>> {
      const operation = new Operation(operationName, dockerImage);
      return this.operationRepository.updateOperation(oldOperationName, operation);
    }
  }