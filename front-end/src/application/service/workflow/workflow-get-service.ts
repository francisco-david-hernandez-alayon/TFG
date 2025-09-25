import type { WorkFlowGetUseCase } from '../../usecase/workflow/workflow-get-use-case';
import { WorkFlow } from '../../../domain/entity/workflow';
import type { WorkFlowRepository } from '../../repository/workflow-repository';
import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError, UllUUID } from '@ull-tfg/ull-tfg-typescript';

export class FlowGetService implements WorkFlowGetUseCase {

  constructor(private readonly flowRepository: WorkFlowRepository) {}

  async getWorkFlow(flowId: UllUUID): Promise<Either<DataError, WorkFlow>> {
    return this.flowRepository.getWorkFlow(flowId);
  }

  async getAllWorkFlows(): Promise<Either<DataError, WorkFlow[]>> {
    return this.flowRepository.getWorkFlows();
  }
}
