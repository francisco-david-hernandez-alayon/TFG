import {  WorkFlowExportUseCase } from '../../usecase/workflow/workflow-export-use-case';
import type { WorkFlowRepository } from '../../repository/workflow-repository';
import { UllUUID, type Either } from '@ull-tfg/ull-tfg-typescript';
import type { DataError } from '@ull-tfg/ull-tfg-typescript';
import { ExportFile } from '@/domain/value-object/export-file';

export class WorkFlowExportService implements WorkFlowExportUseCase {

  constructor(private readonly flowRepository: WorkFlowRepository) {}

  async exportWorkFlow(id: UllUUID): Promise<Either<DataError, ExportFile>> {
    return this.flowRepository.exportWorkFlow(id);
  }
}
