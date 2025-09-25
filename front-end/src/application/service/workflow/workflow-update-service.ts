import type { WorkFlowUpdateUseCase } from '../../usecase/workflow/workflow-update-use-case';
import { WorkFlow } from '../../../domain/entity/workflow';
import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError, UllUUID } from '@ull-tfg/ull-tfg-typescript';
import type { WorkFlowRepository } from '../../repository/workflow-repository';
import { WorkFlowName } from '@/domain/value-object/workflow-name';
import { Node } from '@/domain/value-object/node';

export class WorkFlowUpdateService implements WorkFlowUpdateUseCase {

    constructor(private readonly flowRepository: WorkFlowRepository) { }

    async updateWorkFlow(
        uuid: UllUUID,
        workFlowName: WorkFlowName,
        nodes: Node[],
        creationDate: Date | string,
        enabled: boolean
    ): Promise<Either<DataError, WorkFlow>> {
        const workflow = new WorkFlow(workFlowName, nodes, enabled, creationDate);
        return this.flowRepository.updateWorkFlow(uuid, workflow);
    }
}
