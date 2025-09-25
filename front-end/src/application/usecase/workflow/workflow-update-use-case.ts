import { UllUUID } from '@ull-tfg/ull-tfg-typescript';
import { Operation } from '../../../domain/value-object/operation';
import { Link } from '../../../domain/value-object/link';
import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError } from '@ull-tfg/ull-tfg-typescript';
import { WorkFlowName } from '@/domain/value-object/workflow-name';
import { WorkFlow } from '@/domain/entity/workflow';
import { Node } from '@/domain/value-object/node';

export interface WorkFlowUpdateUseCase {
  /**
   * Update a workflow
   * 
   * @param uuid - The uuid of the workflow
   * @param workFlowName - The new name of the workflow
   * @param nodes - The nodes of the workflow, which are operations and their corresponding predicates
   * @param creationDate - The date of creation of the workflow
   * @returns The updated workflow, if successful
   */
  updateWorkFlow(
    uuid: UllUUID,
    workFlowName: WorkFlowName,
    nodes: Node[],
    creationDate: Date | string,
    enabled: boolean
  ): Promise<Either<DataError, WorkFlow>>;
}
