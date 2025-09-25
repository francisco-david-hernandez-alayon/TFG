import { UllUUID } from '@ull-tfg/ull-tfg-typescript';
import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError } from '@ull-tfg/ull-tfg-typescript';
import { NodeName } from '@/domain/value-object/node-name';
import { Execution } from '@/domain/entity/execution';

export interface WorkFlowExecuteUseCase {
  /**
   * Executes the workflow
   * and returns the output of the last node.
   *
   * @param workflowId - The UUID of the workflow to execute
   * @returns The resulting File of the last node
   */
  executeFlow(workFlowId: UllUUID, pendingUriFiles: Map<NodeName, string>): Promise<Either<DataError, Execution>>;
}
