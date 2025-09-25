import {
    Either,
    type DataError,
    type ApiError,
    http,
    type UllUUID,
} from '@ull-tfg/ull-tfg-typescript';


import { Execution } from '@/domain/entity/execution';
import { ExecutionRepository } from '@/application/repository/execution-repository';
import { ExecutionJsonResponse } from '../response/execution-json-response';

export class ExecutionHttpRepository implements ExecutionRepository {
    // if VITE_API_URL is not defined, default to localhost:9000/executions
    private readonly API_URL: string = (import.meta.env.VITE_API_URL && import.meta.env.VITE_API_URL.trim() !== '' ? import.meta.env.VITE_API_URL : 'http://localhost:9000/') + 'executions';
 

    private headers: Headers = new Headers();

    constructor() {
        this.headers.append('Content-Type', 'application/json');
    }

    // GET get
    public async getExecutions(): Promise<Either<DataError, Execution[]>> {
        return new Promise((resolve) => {
            http
                .get(this.API_URL, this.headers)
                .then((response) => {
                    if (response.ok) {
                        response.json().then((data: ExecutionJsonResponse[]) => {
                            const executions = data.map(ExecutionJsonResponse.toExecution);
                            resolve(Either.right(executions));
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
    public async getExecution(id: UllUUID): Promise<Either<DataError, Execution>> {
        const url = `${this.API_URL}/${id}`;
        return new Promise((resolve) => {
            http
                .get(url, this.headers)
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

    // DELETE delete
    public async deleteExecution(id: UllUUID): Promise<Either<DataError, Execution>> {
            const url = `${this.API_URL}/${id}`;
            return new Promise((resolve) => {
                http
                    .delete(url, this.headers)
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


}
