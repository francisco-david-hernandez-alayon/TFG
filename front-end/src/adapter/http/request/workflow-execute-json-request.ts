import { NodeName } from "@/domain/value-object/node-name";

export class WorkFlowExecuteJsonRequest {
    public pendingUriFiles: Record<string, string>;

    constructor(pendingUriFiles: Record<string, string>) {
        this.pendingUriFiles = pendingUriFiles;
    }

    static toRequest(pendingUriFilesMap: Map<NodeName, string>): WorkFlowExecuteJsonRequest {
        const obj: Record<string, string> = {};

        pendingUriFilesMap.forEach((value, key) => {
            obj[key.getName()] = value;
        });

        return new WorkFlowExecuteJsonRequest(obj);
    }

    toString(): string {
        return JSON.stringify({ pendingUriFiles: this.pendingUriFiles }, null, 2);
    }
}
