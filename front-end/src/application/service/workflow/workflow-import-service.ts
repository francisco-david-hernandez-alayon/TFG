import { WorkFlowImportUseCase } from '../../usecase/workflow/workflow-import-use-case';
import type { WorkFlowRepository } from '../../repository/workflow-repository';
import { ExportFile } from '@/domain/value-object/export-file';
import { WorkFlow } from '@/domain/entity/workflow';
import type { Either, DataError } from '@ull-tfg/ull-tfg-typescript';

export class WorkFlowImportService implements WorkFlowImportUseCase {
  constructor(private readonly flowRepository: WorkFlowRepository) {}

  async importWorkFlow(file: ExportFile): Promise<Either<DataError, WorkFlow>> {
    return this.flowRepository.importWorkFlow(file);
  }
}
