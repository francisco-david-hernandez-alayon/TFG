import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError } from '@ull-tfg/ull-tfg-typescript';
import { ExportFile } from '@/domain/value-object/export-file';
import { UllUUID } from '@ull-tfg/ull-tfg-typescript';

export interface WorkFlowExportUseCase {
    
    /**
     * Export a workflow by its ID.
     * 
     * @param id - Id
     * @returns ExportFile that represents the exported workflow.
     */
    exportWorkFlow(id: UllUUID): Promise<Either<DataError, ExportFile>>;
}

