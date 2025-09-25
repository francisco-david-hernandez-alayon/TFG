import {
  Either,
  type DataError,
  type ApiError,
  http,
  type UllUUID,
} from '@ull-tfg/ull-tfg-typescript';

import type { OperationRepository } from '../../../application/repository/operation-repository';
import type { Operation } from '../../../domain/value-object/operation';
import { OperationPostJsonRequest } from '../request/operation-post-json-request';
import { OperationPutJsonRequest } from '../request/operation-put-json-request';
import { OperationJsonResponse } from '../response/operation-json-response';
import { OperationName } from '@/domain/value-object/operation-name';


export class OperationHttpRepository implements OperationRepository {
  private readonly API_URL: string = (import.meta.env.VITE_API_URL && import.meta.env.VITE_API_URL.trim() !== '' ? import.meta.env.VITE_API_URL : 'http://localhost:9000/') + 'operations';
  private headers: Headers = new Headers();

  constructor() {
    this.headers.append('Content-Type', 'application/json');
  }

  // GET Get
  public async getOperations(): Promise<Either<DataError, Operation[]>> {
    return new Promise((resolve) => {
      http
        .get(this.API_URL, this.headers)
        .then(response => {
          if (response.ok) {
            response.json().then((data: OperationJsonResponse[]) => {
              const operations = data.map(OperationJsonResponse.toOperation);
              resolve(Either.right(operations));
            });
          } else {
            response.json().then((data: ApiError) => {
              resolve(Either.left(data));
            });
          }
        })
        .catch(error => {
          resolve(Either.left({ kind: 'UnexpectedError', message: error }));
        });
    });
  }

  // GET getById
  public async getOperation(name: OperationName): Promise<Either<DataError, Operation>> {
    const url = `${this.API_URL}/${name.getName()}`;
    return new Promise((resolve) => {
      http
        .get(url, this.headers)
        .then(response => {
          if (response.ok) {
            response.json().then((data: OperationJsonResponse) => {
              resolve(Either.right(OperationJsonResponse.toOperation(data)));
            });
          } else {
            response.json().then((data: ApiError) => {
              resolve(Either.left(data));
            });
          }
        })
        .catch(error => {
          resolve(Either.left({ kind: 'UnexpectedError', message: error }));
        });
    });
  }

  // POST create
  public async saveOperation(operation: Operation): Promise<Either<DataError, Operation>> {
    const body = OperationPostJsonRequest.toRequest(operation);
    return new Promise((resolve) => {
      http
        .post(this.API_URL, body, this.headers)
        .then(response => {
          if (response.ok) {
            response.json().then((data: OperationJsonResponse) => {
              resolve(Either.right(OperationJsonResponse.toOperation(data)));
            });
          } else {
            response.json().then((data: ApiError) => {
              resolve(Either.left(data));
            });
          }
        })
        .catch(error => {
          resolve(Either.left({ kind: 'UnexpectedError', message: error }));
        });
    });
  }

  // DELETE delete
  public async deleteOperation(name: OperationName): Promise<Either<DataError, Operation>> {
    const url = `${this.API_URL}/${name.getName()}`;
    return new Promise((resolve) => {
      http
        .delete(url, this.headers)
        .then(response => {
          if (response.status === 204) {  // 204 No Content ERROR
            throw new Error('No content on delete operation');
          }

          if (response.ok) {
            response.json().then((data: OperationJsonResponse) => {
              resolve(Either.right(OperationJsonResponse.toOperation(data)));
            });

            
          } else {
            response.json().then((data: ApiError) => {
              resolve(Either.left(data));
            });
          }
        })
        .catch(error => {
          resolve(Either.left({ kind: 'UnexpectedError', message: error }));
        });
    });
  }

  // PUT update
  public async updateOperation(name: OperationName, operation: Operation): Promise<Either<DataError, Operation>> {
    const url = `${this.API_URL}/${name.getName()}`;
    const body = OperationPutJsonRequest.toRequest(operation);

    return new Promise((resolve) => {
      http
        .put(url, body, this.headers)
        .then(response => {
          if (response.status === 204) {  // 204 No Content ERROR
            throw new Error('No content on update operation');
          }

          if (response.ok) {
            response.json().then((data: OperationJsonResponse) => {
              resolve(Either.right(OperationJsonResponse.toOperation(data)));
            });
          } else {
            response.json().then((data: ApiError) => {
              resolve(Either.left(data));
            });
          }
        })
        .catch(error => {
          resolve(Either.left({ kind: 'UnexpectedError', message: error }));
        });
    });
  }
}
