import { WorkFlow } from "@/domain/entity/workflow";
import { WorkFlowJsonResponse } from "./workflow-json-response";



export class ImportFileJsonResponse {
    public workflow: WorkFlowJsonResponse;

    // Constructor
    constructor(workflow: WorkFlowJsonResponse) {
        this.workflow = workflow;
    }

    static toFlow(json: WorkFlowJsonResponse): WorkFlow {
        const workflow = WorkFlowJsonResponse.toWorkFlow(json);
        return workflow;
    }

    toString(): string {
        return `ExportFileJsonResponse={workflow=${this.workflow.toString()}}`;
    }
}
