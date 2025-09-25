import { WorkFlowRepository } from '@/application/repository/workflow-repository';
import { WorkFlowCreateUseCase } from '@/application/usecase/workflow/workflow-create-use-case';
import { WorkFlow } from '@/domain/entity/workflow';
import { Node } from '@/domain/value-object/node';
import { WorkFlowName } from '@/domain/value-object/workflow-name';
import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError } from '@ull-tfg/ull-tfg-typescript';


export class WorkFlowCreateService implements WorkFlowCreateUseCase {

  constructor(private readonly flowRepository: WorkFlowRepository) { }

  async createWorkFlow(
    workFlowName: WorkFlowName,
    nodes: Node[],
    creationDate: Date | string,
    enabled: boolean
  ): Promise<Either<DataError, WorkFlow>> {
    
    const workflow = new WorkFlow(workFlowName, nodes, enabled, creationDate);
    return this.flowRepository.saveWorkFlow(workflow);
  }
}
