import { UllDockerImageName } from "@ull-tfg/ull-tfg-typescript/dist/ull-docker-image-name";
import { OperationName } from "./operation-name";
import { NodeContent } from "./node-content";
import { NodeContentType } from "../enumerate/node-content-type";


export class Operation extends NodeContent {
  private static readonly ERROR_OPERATION_NAME_NOT_DEFINED = 'Name for Operation not defined';
  private static readonly ERROR_DOCKER_IMAGE_NOT_DEFINED = 'Docker Image not defined';


  private readonly name: OperationName;
  private readonly dockerImage: UllDockerImageName;


  constructor(name: OperationName, dockerImage: UllDockerImageName) {
    super();
    this.validateName(name);
    this.validateDockerImage(dockerImage);
    this.name = name;
    this.dockerImage = dockerImage;

  }

  public getName(): OperationName {
    return this.name;
  }

  public getDockerImage(): UllDockerImageName {
    return this.dockerImage;
  }



  public setName(name: OperationName): Operation {
    return new Operation(name, this.dockerImage);
  }

  public setDockerImage(dockerImage: UllDockerImageName): Operation {
    return new Operation(this.name, dockerImage);
  }



  public getType(): NodeContentType {
    return NodeContentType.OPERATION;
  }

  private validateName(name: OperationName | null): void {
    if (!name) {
      throw new Error(Operation.ERROR_OPERATION_NAME_NOT_DEFINED);
    }
  }

  private validateDockerImage(dockerImage: UllDockerImageName | null): void {
    if (!dockerImage) {
      throw new Error(Operation.ERROR_DOCKER_IMAGE_NOT_DEFINED);
    }
  }



  public toString(): string {
    return `Operation{name=${this.name.toString()}, dockerImage=${this.dockerImage.toString()}}`;
  }
}

