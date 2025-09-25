import { WorkFlow } from "../entity/workflow";

/**
 * Represents an exported workflow file with a name and workflow data.
 */
export class ExportFile {

    private static readonly ERROR_FLOW_NOT_DEFINED = 'File data not defined';

    private readonly workflow: WorkFlow;

    constructor(workflow: WorkFlow) {
        this.validateFlow(workflow);
        this.workflow = workflow;
    }

    private validateFlow(workflow: WorkFlow): void {
        if (!workflow) {
            throw new Error(ExportFile.ERROR_FLOW_NOT_DEFINED);
        }
    }

    public getWorkFlow(): WorkFlow {
        return this.workflow;
    }

    public setWorkFlow(workflow: WorkFlow): ExportFile {
        return new ExportFile(workflow);
    }


    public toString(): string {
        return `ExportFile={workflowName='${this.workflow.getName().getName()}'}`;
    }

    public equals(other: ExportFile): boolean {
        return other instanceof ExportFile && this.workflow === other.workflow;
    }
}
