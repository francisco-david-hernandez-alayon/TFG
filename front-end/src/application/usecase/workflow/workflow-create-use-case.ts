import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError } from '@ull-tfg/ull-tfg-typescript';
import { WorkFlow } from '@/domain/entity/workflow';
import { WorkFlowName } from '@/domain/value-object/workflow-name';
import { Node } from '@/domain/value-object/node';

export interface WorkFlowCreateUseCase {
    
    /**
     * Create a workflow
     * 
     * @param workFlowName - Name of the workflow
     * @param nodes - Map of operations and their corresponding predicates
     * @param creationDate - Date of creation of the workflow
     * @returns The created workflow, if successful
     */
    createWorkFlow(
        workFlowName: WorkFlowName,
        nodes: Node[],
        creationDate: Date | string,
        enabled: boolean
    ): Promise<Either<DataError, WorkFlow>>;
}
