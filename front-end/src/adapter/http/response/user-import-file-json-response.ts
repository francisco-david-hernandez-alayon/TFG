import { WorkFlowJsonResponse } from './workflow-json-response';
import { ExportFile } from '@/domain/value-object/export-file';

export class UserImportFileJsonResponse {

    public workflow: WorkFlowJsonResponse;

    constructor(workflow: WorkFlowJsonResponse) {
        this.workflow = workflow;
    }

    static toExportFile(json: WorkFlowJsonResponse): ExportFile {
        const workflow = WorkFlowJsonResponse.toWorkFlow(json);
        return new ExportFile(workflow);
    }

    toString(): string {
        return `UserImportFileJsonResponse={workflow=${this.workflow.toString()}}`;
    }
}
