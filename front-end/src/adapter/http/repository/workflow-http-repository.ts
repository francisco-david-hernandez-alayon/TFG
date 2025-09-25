import {
    Either,
    type DataError,
    type ApiError,
    http,
    type UllUUID,
} from '@ull-tfg/ull-tfg-typescript';

import type { WorkFlowRepository } from '../../../application/repository/workflow-repository';
import { WorkFlowJsonResponse } from '../response/workflow-json-response';
import { ExportFileJsonResponse } from '../response/export-file-json-response';
import { ExportFile } from '@/domain/value-object/export-file';
import { ImportFileJsonResponse } from '../response/import-file-json-response';
import { WorkFlow } from '@/domain/entity/workflow';
import { WorkFlowImportJsonRequest } from '../request/workflow-import-json-request';
import { WorkFlowPutJsonRequest } from '../request/workflow-put-json-request';
import { WorkFlowPostJsonRequest } from '../request/workflow-post-json-request';
import { WorkFlowExecuteJsonRequest } from '../request/workflow-execute-json-request';
import { Execution } from '@/domain/entity/execution';
import { NodeName } from '@/domain/value-object/node-name';
import { ExecutionJsonResponse } from '../response/execution-json-response';

export class WorkFlowHttpRepository implements WorkFlowRepository {
    // if VITE_API_URL is not defined, default to localhost:9000/flows
    private readonly API_URL: string = (import.meta.env.VITE_API_URL && import.meta.env.VITE_API_URL.trim() !== '' ? import.meta.env.VITE_API_URL : 'http://localhost:9000/') + 'flows';
 

    private headers: Headers = new Headers();

    constructor() {
        this.headers.append('Content-Type', 'application/json');
    }

    // GET get
    public async getWorkFlows(): Promise<Either<DataError, WorkFlow[]>> {
        return new Promise((resolve) => {
            http
                .get(this.API_URL, this.headers)
                .then((response) => {
                    if (response.ok) {
                        response.json().then((data: WorkFlowJsonResponse[]) => {
                            const flows = data.map(WorkFlowJsonResponse.toWorkFlow);
                            resolve(Either.right(flows));
                        });
                    } else {
                        response.json().then((data: ApiError) => {
                            resolve(Either.left(data));
                        });
                    }
                })
                .catch((error) => {
                    resolve(Either.left({ kind: 'UnexpectedError', message: error }));
                });
        });
    }

    // GET getById
    public async getWorkFlow(id: UllUUID): Promise<Either<DataError, WorkFlow>> {
        const url = `${this.API_URL}/${id}`;
        return new Promise((resolve) => {
            http
                .get(url, this.headers)
                .then((response) => {
                    if (response.ok) {
                        response.json().then((data: WorkFlowJsonResponse) => {
                            resolve(Either.right(WorkFlowJsonResponse.toWorkFlow(data)));
                        });
                    } else {
                        response.json().then((data: ApiError) => {
                            resolve(Either.left(data));
                        });
                    }
                })
                .catch((error) => {
                    resolve(Either.left({ kind: 'UnexpectedError', message: error }));
                });
        });
    }

    // POST create
    public async saveWorkFlow(workflow: WorkFlow): Promise<Either<DataError, WorkFlow>> {
        const body = WorkFlowPostJsonRequest.toRequest(workflow);
        return new Promise((resolve) => {
            http
                .post(this.API_URL, body, this.headers)
                .then((response) => {
                    if (response.ok) {
                        response.json().then((data: WorkFlowJsonResponse) => {
                            resolve(Either.right(WorkFlowJsonResponse.toWorkFlow(data)));
                        });
                    } else {
                        response.json().then((data: ApiError) => {
                            resolve(Either.left(data));
                        });
                    }
                })
                .catch((error) => {
                    resolve(Either.left({ kind: 'UnexpectedError', message: error }));
                });
        });
    }

    // DELETE delete
    public async deleteWorkFlow(id: UllUUID): Promise<Either<DataError, WorkFlow>> {
        const url = `${this.API_URL}/${id}`;
        return new Promise((resolve) => {
            http
                .delete(url, this.headers)
                .then((response) => {
                    if (response.ok) {
                        response.json().then((data: WorkFlowJsonResponse) => {
                            resolve(Either.right(WorkFlowJsonResponse.toWorkFlow(data)));
                        });
                    } else {
                        response.json().then((data: ApiError) => {
                            resolve(Either.left(data));
                        });
                    }
                })
                .catch((error) => {
                    resolve(Either.left({ kind: 'UnexpectedError', message: error }));
                });
        });
    }

    // PUT update
    public async updateWorkFlow(
        id: UllUUID,
        workflow: WorkFlow
    ): Promise<Either<DataError, WorkFlow>> {
        const url = `${this.API_URL}/${id}`;
        const body = WorkFlowPutJsonRequest.toRequest(workflow);

        console.log(JSON.stringify(body, null, 2));


        return new Promise((resolve) => {
            http
                .put(url, body, this.headers)
                .then((response) => {
                    if (response.ok) {
                        response.json().then((data: WorkFlowJsonResponse) => {
                            resolve(Either.right(WorkFlowJsonResponse.toWorkFlow(data)));
                        });
                    } else {
                        response.json().then((data: ApiError) => {
                            resolve(Either.left(data));
                        });
                    }
                })
                .catch((error) => {
                    resolve(Either.left({ kind: 'UnexpectedError', message: error }));
                });
        });
    }

    // POST execute
    public async executeWorkFlow(
        id: UllUUID, pendingUriFilesMap: Map<NodeName, string>
    ): Promise<Either<DataError, Execution>> {
        const url = `${this.API_URL}/${id}/execute`;
        const body = WorkFlowExecuteJsonRequest.toRequest(pendingUriFilesMap);

        return new Promise((resolve) => {
            http
                .post(url, body, this.headers)
                .then((response) => {
                    if (response.ok) {
                        response.json().then((data: ExecutionJsonResponse) => {
                            resolve(Either.right(ExecutionJsonResponse.toExecution(data)));
                        });
                    } else {
                        response.json().then((data: ApiError) => {
                            resolve(Either.left(data));
                        });
                    }
                })
                .catch((error) => {
                    resolve(Either.left({ kind: 'UnexpectedError', message: error }));
                });
        });
    }


    // GET export
    public async exportWorkFlow(id: UllUUID): Promise<Either<DataError, ExportFile>> {
        const url = `${this.API_URL}/${id}/export`;
        return new Promise((resolve) => {
            http
                .get(url, this.headers)
                .then((response) => {
                    if (response.ok) {
                        response.json().then((data: WorkFlowJsonResponse) => {
                            const exportFile = ExportFileJsonResponse.toExportFile(data);
                            resolve(Either.right(exportFile));
                        });
                    } else {
                        response.json().then((data: ApiError) => {
                            resolve(Either.left(data));
                        });
                    }
                })
                .catch((error) => {
                    resolve(Either.left({ kind: 'UnexpectedError', message: error }));
                });
        });
    }

    // POST import
    public async importWorkFlow(file: ExportFile): Promise<Either<DataError, WorkFlow>> {
        const url = `${this.API_URL}/import`;
        const body = WorkFlowImportJsonRequest.toRequest(file);

        return new Promise((resolve) => {
            http
                .post(url, body, this.headers)
                .then((response) => {
                    if (response.ok) {
                        response.json().then((data: WorkFlowJsonResponse) => {
                            resolve(Either.right(ImportFileJsonResponse.toFlow(data)));
                        });
                    } else {
                        response.json().then((data: ApiError) => {
                            resolve(Either.left(data));
                        });
                    }
                })
                .catch((error) => {
                    resolve(Either.left({ kind: 'UnexpectedError', message: error }));
                });
        });
    }


}
