
import { WorkFlowRepository } from '@/application/repository/workflow-repository';
import { WorkFlowDeleteUseCase } from '@/application/usecase/workflow/workflow-delete-use-case';
import { WorkFlow } from '@/domain/entity/workflow';
import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError, UllUUID } from '@ull-tfg/ull-tfg-typescript';

export class WorkFlowDeleteService implements WorkFlowDeleteUseCase {

  constructor(private readonly flowRepository: WorkFlowRepository) {}

  async deleteWorkFlow(flowId: UllUUID): Promise<Either<DataError, WorkFlow>> {
    return this.flowRepository.deleteWorkFlow(flowId);
  }
}
