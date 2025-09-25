import { WorkFlowRepository } from '@/application/repository/workflow-repository';
import type { WorkFlowExecuteUseCase } from '../../usecase/workflow/workflow-execute-use-case';
import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError, UllUUID } from '@ull-tfg/ull-tfg-typescript';
import { NodeName } from '@/domain/value-object/node-name';
import { Execution } from '@/domain/entity/execution';


export class WorkFlowExecuteService implements WorkFlowExecuteUseCase {

  constructor(private readonly flowRepository: WorkFlowRepository) { }

  async executeFlow(workFlowId: UllUUID, pendingUriFiles: Map<NodeName, string>): Promise<Either<DataError, Execution>> {
    return this.flowRepository.executeWorkFlow(workFlowId, pendingUriFiles);
  }
}
