import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError } from '@ull-tfg/ull-tfg-typescript';
import type { OperationCreateUseCase } from '../../usecase/operation/operation-create-use-case';
import { Operation } from '../../../domain/value-object/operation';
import { OperationName } from '../../../domain/value-object/operation-name';
import { UllDockerImageName } from '@ull-tfg/ull-tfg-typescript/dist/ull-docker-image-name';
import type { OperationRepository } from '../../repository/operation-repository';
import { NodeIcon } from '@/domain/enumerate/node-icon';

export class OperationCreateService implements OperationCreateUseCase {

    constructor(private readonly operationRepository: OperationRepository) { }

    async createOperation(name: OperationName, dockerImage: UllDockerImageName): Promise<Either<DataError, Operation>> {
        const operation = new Operation(name, dockerImage); 
        return this.operationRepository.saveOperation(operation); 
    }
}