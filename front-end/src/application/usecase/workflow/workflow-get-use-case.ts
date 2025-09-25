import { WorkFlow } from '@/domain/entity/workflow';
import { UllUUID } from '@ull-tfg/ull-tfg-typescript';
import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError } from '@ull-tfg/ull-tfg-typescript';

export interface WorkFlowGetUseCase {

    /**
     * Get a workflow by its ID
     * 
     * @param workflowId - The ID of the workflow
     * @returns The workflow if found, otherwise undefined
     */
    getWorkFlow(workflowId: UllUUID): Promise<Either<DataError, WorkFlow>>;

    /**
     * Get all flows
     * 
     * @returns An array of all flows
     */
    getAllWorkFlows(): Promise<Either<DataError, WorkFlow[]>>;
}
