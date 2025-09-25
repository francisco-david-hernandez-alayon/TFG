import { WorkFlow } from '@/domain/entity/workflow';
import { UllUUID } from '@ull-tfg/ull-tfg-typescript';
import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError } from '@ull-tfg/ull-tfg-typescript';

export interface WorkFlowDeleteUseCase {

    /**
     * Delete a workflow
     * 
     * @param workflowId - The UUID of the workflow to delete
     * @returns The deleted workflow, if found
     */
    deleteWorkFlow(workflowId: UllUUID): Promise<Either<DataError, WorkFlow>>;
}
