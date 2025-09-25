import { UllUUID } from '@ull-tfg/ull-tfg-typescript';
import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError } from '@ull-tfg/ull-tfg-typescript';
import { ExportFile } from '@/domain/value-object/export-file';
import { WorkFlow } from '@/domain/entity/workflow';
import { NodeName } from '@/domain/value-object/node-name';
import { Execution } from '@/domain/entity/execution';

export interface WorkFlowRepository {
    getWorkFlows(): Promise<Either<DataError, WorkFlow[]>>;
    getWorkFlow(id: UllUUID): Promise<Either<DataError, WorkFlow>>;
    saveWorkFlow(workFlow: WorkFlow): Promise<Either<DataError, WorkFlow>>;
    deleteWorkFlow(id: UllUUID): Promise<Either<DataError, WorkFlow>>;
    updateWorkFlow(id: UllUUID, workFlow: WorkFlow): Promise<Either<DataError, WorkFlow>>;
    executeWorkFlow(workFlowId: UllUUID, pendingUriFiles: Map<NodeName, string>) : Promise<Either<DataError, Execution>>;
    exportWorkFlow(id: UllUUID): Promise<Either<DataError, ExportFile>>;
    importWorkFlow(file: ExportFile): Promise<Either<DataError, WorkFlow>>;
}