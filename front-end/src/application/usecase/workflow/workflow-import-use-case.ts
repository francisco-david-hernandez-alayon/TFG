import { Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError } from '@ull-tfg/ull-tfg-typescript';
import { ExportFile } from '@/domain/value-object/export-file';
import { WorkFlow } from '@/domain/entity/workflow';

export interface WorkFlowImportUseCase {
  /**
   * Import a workflow from an ExportFile.
   * 
   * @param file - The ExportFile to import
   * @returns The imported workflow
   */
  importWorkFlow(file: ExportFile): Promise<Either<DataError, WorkFlow>>;
}
