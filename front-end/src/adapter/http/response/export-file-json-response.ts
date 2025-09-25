import { ExportFile } from '@/domain/value-object/export-file';
import { WorkFlowJsonResponse } from './workflow-json-response';


export class ExportFileJsonResponse {
    public workflow: WorkFlowJsonResponse;

    // Constructor
    constructor(workflow: WorkFlowJsonResponse) {
        this.workflow = workflow;
    }

    static toExportFile(json: WorkFlowJsonResponse): ExportFile {
        const workflow = WorkFlowJsonResponse.toWorkFlow(json);
        return new ExportFile(workflow);
    }

    toString(): string {
        return `ExportFileJsonResponse={workflow=${this.workflow.toString()}}`;
    }
}
