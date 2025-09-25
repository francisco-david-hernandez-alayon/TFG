import { ExecuteStatus } from "@/domain/enumerate/ExecuteStatus";
import { Execution } from "@/domain/entity/execution";
import { File } from "@/domain/value-object/file";
import { FileName } from "@/domain/value-object/file-name";
import { NodeExecutedData } from "@/domain/value-object/node-execute-data";
import { NodeName } from "@/domain/value-object/node-name";
import { WorkFlowName } from "@/domain/value-object/workflow-name";
import { UllUUID } from "@ull-tfg/ull-tfg-typescript";

export class ExecutionJsonResponse {
    public uuid: string;
    public workFlowName: string;
    public executionDate: Date; 
    public status: string;
    public message: string;
    public outputFiles: File[];
    public nodesExecuted: { name: string; nodeTimeExecution: number }[];
    public totalExecutionTimeMillis: number;
    public numberOfOperationExecuted: number;

    constructor(
        uuid: string,
        workFlowName: string,
        executionDate: Date,
        status: ExecuteStatus,
        message: string,
        outputFiles: File[],
        nodesExecuted: { name: string; nodeTimeExecution: number }[],
        totalExecutionTimeMillis: number,
        numberOfOperationExecuted: number
    ) {
        this.uuid = uuid;
        this.workFlowName = workFlowName;
        this.executionDate = executionDate;
        this.status = status;
        this.message = message;
        this.outputFiles = outputFiles;
        this.nodesExecuted = nodesExecuted;
        this.totalExecutionTimeMillis = totalExecutionTimeMillis;
        this.numberOfOperationExecuted = numberOfOperationExecuted;
    }

    static toExecution(json: ExecutionJsonResponse): Execution {
        if (!json) {
            throw new Error("Invalid JSON for ExecutionJsonResponse");
        }

        const uuid = json.uuid;
        const workFlowName = new WorkFlowName(json.workFlowName);
        const executionDate = json.executionDate;

        const status = ExecuteStatus[json.status as keyof typeof ExecuteStatus];
        if (!status) {
            throw new Error(`Invalid status value: ${json.status}`);
        }

        const message = json.message;
        if (!message || message.trim() === "") {
            throw new Error("Message is missing or empty");
        }

        const outputFiles: File[] = ExecutionJsonResponse.toFileArray(json.outputFiles);

        const nodesExecuted: NodeExecutedData[] = [];
        if (Array.isArray(json.nodesExecuted)) {
            json.nodesExecuted.forEach((nodeObj) => {
                if (!nodeObj.name || nodeObj.nodeTimeExecution == null) {
                    throw new Error("Invalid node executed data");
                }
                nodesExecuted.push(new NodeExecutedData(new NodeName(nodeObj.name), nodeObj.nodeTimeExecution));
            });
        } else {
            throw new Error("nodesExecuted must be an array");
        }

        const totalExecutionTimeMillis = json.totalExecutionTimeMillis ?? 0;
        if (totalExecutionTimeMillis == null || totalExecutionTimeMillis < 0) {
            throw new Error("Total execution time must be a positive number");
        }

        const numberOfOperationExecuted = json.numberOfOperationExecuted ?? 0;
        if (numberOfOperationExecuted == null || numberOfOperationExecuted < 0) {
            throw new Error("Number of operations executed must be a positive number");
        }

        return new Execution(workFlowName, status, message, outputFiles, nodesExecuted, totalExecutionTimeMillis, numberOfOperationExecuted, executionDate, new UllUUID(uuid));
    }


    static toFileArray(jsonArray: any[]): File[] {
        if (!Array.isArray(jsonArray)) {
            throw new Error("outputFiles must be an array");
        }

        return jsonArray.map((jsonOutputFile: any) => {
            if (!jsonOutputFile || !jsonOutputFile.fileName) {
                throw new Error("Each output file must have a fileName");
            }
            const fileName = new FileName(jsonOutputFile.fileName as string);
            const uri = jsonOutputFile.uri as string;
            return new File(fileName, uri);
        });
    }

    toString(): string {
        return JSON.stringify(
            {
                status: this.status,
                message: this.message,
                outputFiles: this.outputFiles.map((file) => ({
                    fileName: file.getName().getName(),
                    uri: file.getUri(),
                })),
                nodesExecuted: this.nodesExecuted,
            },
            null,
            2
        );
    }


}
