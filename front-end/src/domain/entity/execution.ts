import { UllUUID } from "@ull-tfg/ull-tfg-typescript";
import { ExecuteStatus } from "../enumerate/ExecuteStatus";
import { File } from "../value-object/file";
import { NodeExecutedData } from "../value-object/node-execute-data";
import { WorkFlowName } from "../value-object/workflow-name";

export class Execution {
    private static readonly ERROR_STATUS_NOT_DEFINED = "Execute status not defined";
    private static readonly ERROR_MESSAGE_NOT_DEFINED = "Message not defined";
    private static readonly ERROR_OUTPUT_FILE_NOT_DEFINED = "Output files not defined";
    private static readonly ERROR_NODES_EXECUTED_NOT_DEFINED = "Executed nodes list not defined";
    private static readonly ERROR_TOTAL_EXECUTION_TIME_NOT_DEFINED = "Total execution time must be a positive number";
    private static readonly ERROR_ID_NOT_DEFINED = "ID not defined";
    private static readonly ERROR_ID_INVALID = "ID must be an instance of UllUUID";

    private static readonly ERROR_WORKFLOW_NAME_NOT_DEFINED = "Workflow name not defined";

    private readonly id: UllUUID;
    private readonly workFlowName: WorkFlowName;
    private readonly creationDate: Date;

    private readonly status: ExecuteStatus;
    private readonly message: string;
    private readonly outputFiles: File[];
    private readonly nodesExecuted: NodeExecutedData[];
    private readonly totalExecutionTimeMillis: number;
    private readonly numberOfOperationExecuted: number;

    constructor(
        workFlowName: WorkFlowName,
        status: ExecuteStatus,
        message: string,
        outputFiles: File[],
        nodesExecuted: NodeExecutedData[],
        totalExecutionTimeMillis: number,
        numberOfOperationExecuted: number,
        creationDate: Date | string = new Date(), id?: UllUUID
    ) {
        this.validateWorkFlowName(workFlowName);
        this.validateStatus(status);
        this.validateMessage(message);
        this.validateOutputFiles(outputFiles);
        this.validateNodesExecuted(nodesExecuted);
        this.validateTotalExecutionTime(totalExecutionTimeMillis);

        if (id) {
            this.validateId(id);
            this.id = id;
        } else {
            this.id = UllUUID.random();
        }

        this.workFlowName = workFlowName;
        this.creationDate = new Date(creationDate);
        this.status = status;
        this.message = message;
        this.outputFiles = outputFiles;
        this.nodesExecuted = nodesExecuted;
        this.totalExecutionTimeMillis = totalExecutionTimeMillis;
        this.numberOfOperationExecuted = numberOfOperationExecuted;
    }

    getId(): UllUUID {
        return this.id;
    }

    getWorkFlowName(): WorkFlowName {
        return this.workFlowName;
    }

    getCreationDate(): Date {
        return this.creationDate;
    }

    getStatus(): ExecuteStatus {
        return this.status;
    }

    getMessage(): string {
        return this.message;
    }

    getOutputFiles(): File[] {
        return this.outputFiles;
    }

    getNodesExecuted(): NodeExecutedData[] {
        return this.nodesExecuted;
    }

    getTotalExecutionTimeMillis(): number {
        return this.totalExecutionTimeMillis;
    }

    getNumberOfOperationExecuted(): number {
        return this.numberOfOperationExecuted;
    }

    setWorkFlowName(workFlowName: WorkFlowName): Execution {
        this.validateWorkFlowName(workFlowName);
        return new Execution(workFlowName, this.status, this.message, this.outputFiles, this.nodesExecuted, this.totalExecutionTimeMillis, this.numberOfOperationExecuted);
    }

    setStatus(status: ExecuteStatus): Execution {
        return new Execution(this.workFlowName, status, this.message, this.outputFiles, this.nodesExecuted, this.totalExecutionTimeMillis, this.numberOfOperationExecuted);
    }

    setMessage(message: string): Execution {
        return new Execution(this.workFlowName, this.status, message, this.outputFiles, this.nodesExecuted, this.totalExecutionTimeMillis, this.numberOfOperationExecuted);
    }

    setOutputFiles(outputFiles: File[]): Execution {
        return new Execution(this.workFlowName, this.status, this.message, outputFiles, this.nodesExecuted, this.totalExecutionTimeMillis, this.numberOfOperationExecuted);
    }

    setNodesExecuted(nodesExecuted: NodeExecutedData[]): Execution {
        return new Execution(this.workFlowName, this.status, this.message, this.outputFiles, nodesExecuted, this.totalExecutionTimeMillis, this.numberOfOperationExecuted);
    }

    setTotalExecutionTimeMillis(totalExecutionTimeMillis: number): Execution {
        return new Execution(this.workFlowName, this.status, this.message, this.outputFiles, this.nodesExecuted, totalExecutionTimeMillis, this.numberOfOperationExecuted);
    }

    setNumberOfOperationExecuted(numberOfOperationExecuted: number): Execution {
        return new Execution(this.workFlowName, this.status, this.message, this.outputFiles, this.nodesExecuted, this.totalExecutionTimeMillis, numberOfOperationExecuted);
    }

    private validateId(id: UllUUID): void {
        if (!id) throw new Error(Execution.ERROR_ID_NOT_DEFINED);
        if (!(id instanceof UllUUID)) throw new Error(Execution.ERROR_ID_INVALID);
    }

    private validateWorkFlowName(workFlowName: WorkFlowName): void {
        if (!workFlowName || workFlowName.toString().trim() === "") {
            throw new Error(Execution.ERROR_WORKFLOW_NAME_NOT_DEFINED);
        }
    }
    private validateStatus(status: ExecuteStatus): void {
        if (status === null || status === undefined) {
            throw new Error(Execution.ERROR_STATUS_NOT_DEFINED);
        }
    }

    private validateMessage(message: string): void {
        if (!message || message.trim() === "") {
            throw new Error(Execution.ERROR_MESSAGE_NOT_DEFINED);
        }
    }

    private validateOutputFiles(outputFile: File[]): void {
        if (!outputFile) {
            throw new Error(Execution.ERROR_OUTPUT_FILE_NOT_DEFINED);
        }
    }

    private validateNodesExecuted(nodesExecuted: NodeExecutedData[]): void {
        if (!nodesExecuted) {
            throw new Error(Execution.ERROR_NODES_EXECUTED_NOT_DEFINED);
        }
    }

    private validateTotalExecutionTime(totalExecutionTimeMillis: number): void {
        if (totalExecutionTimeMillis == null || totalExecutionTimeMillis < 0) {
            throw new Error(Execution.ERROR_TOTAL_EXECUTION_TIME_NOT_DEFINED);
        }
    }

    toString(): string {
        return `Execution{id='${this.id}', workFlowName='${this.workFlowName}', creationDate='${this.creationDate}', status='${this.status}', message='${this.message}', outputFiles=${this.outputFiles.toString()}, nodesExecuted=[${this.nodesExecuted.map(n => n.toString()).join(", ")}], totalExecutionTimeMillis=${this.totalExecutionTimeMillis}, numberOfOperationExecuted=${this.numberOfOperationExecuted}}`;
    }

    equals(other: unknown): boolean {
        if (!(other instanceof Execution)) return false;
        return (
            this.workFlowName.equals(other.workFlowName) &&
            this.id.equals(other.id) &&
            this.creationDate.getTime() === other.creationDate.getTime() &&
            this.status === other.status &&
            this.message === other.message &&
            this.arraysEqualFiles(this.outputFiles, other.outputFiles) &&
            this.arraysEqualNodeName(this.nodesExecuted, other.nodesExecuted) &&
            this.totalExecutionTimeMillis === other.totalExecutionTimeMillis
        );
    }

    private arraysEqualNodeName(arr1: NodeExecutedData[], arr2: NodeExecutedData[]): boolean {
        if (arr1.length !== arr2.length) return false;
        for (let i = 0; i < arr1.length; i++) {
            if (!arr1[i].equals(arr2[i])) return false;
        }
        return true;
    }

    private arraysEqualFiles(arr1: File[], arr2: File[]): boolean {
        if (arr1.length !== arr2.length) return false;
        for (let i = 0; i < arr1.length; i++) {
            if (!arr1[i].equals(arr2[i])) return false;
        }
        return true;
    }

    // Simple hash code based on JSON string (not perfect but illustrative)
    hashCode(): number {
        let str = JSON.stringify({
            status: this.status,
            message: this.message,
            outputFile: this.outputFiles.toString(),
            nodesExecuted: this.nodesExecuted.map(n => n.toString()),
            totalExecutionTimeMillis: this.totalExecutionTimeMillis,
        });
        let hash = 0;
        for (let i = 0; i < str.length; i++) {
            const chr = str.charCodeAt(i);
            hash = (hash << 5) - hash + chr;
            hash |= 0;
        }
        return hash;
    }
}
