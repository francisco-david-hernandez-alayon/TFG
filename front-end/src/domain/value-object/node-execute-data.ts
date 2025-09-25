import { validate } from "uuid";
import { NodeName } from "./node-name";

export class NodeExecutedData {
  private static readonly ERROR_NODE_NAME_NOT_DEFINED = "NodeName is required";
  private static readonly ERROR_NODE_TIME_EXECUTION_MILLIS_NOT_DEFINED = "Invalid nodeTime";

  private readonly name: NodeName;
  private readonly nodeTimeExecutionMillis: number;

  constructor(name: NodeName, nodeTimeExecutionMillis: number) {
    this.validateName(name);
    this.validateNodeTimeExecutionMillis(nodeTimeExecutionMillis);
    this.name = name;
    this.nodeTimeExecutionMillis = nodeTimeExecutionMillis;
  }

  getName(): NodeName {
    return this.name;
  }

  getNodeTimeExecutionMillis(): number {
    return this.nodeTimeExecutionMillis;
  }

  setName(name: NodeName): NodeExecutedData {
    this.validateName(name);
    return new NodeExecutedData(name, this.nodeTimeExecutionMillis);
  }

  setNodeTimeExecutionMillis(nodeTimeExecutionMillis: number): NodeExecutedData {
    this.validateNodeTimeExecutionMillis(nodeTimeExecutionMillis);
    return new NodeExecutedData(this.name, nodeTimeExecutionMillis);
  }

  validateNodeTimeExecutionMillis(nodeTimeExecutionMillis: number): void {
    if (nodeTimeExecutionMillis == null || nodeTimeExecutionMillis < 0) {
      throw new Error(NodeExecutedData.ERROR_NODE_TIME_EXECUTION_MILLIS_NOT_DEFINED);
    }
  }

  validateName(name: NodeName): void {
    if (!name) {
      throw new Error(NodeExecutedData.ERROR_NODE_NAME_NOT_DEFINED);
    }
  }

  toString(): string {
    return `NodeExecutedData{name=${this.name.toString()}, nodeTimeExecutionMillis=${this.nodeTimeExecutionMillis}}`;
  }

  equals(other: unknown): boolean {
    if (!(other instanceof NodeExecutedData)) return false;
    return this.name.equals(other.name) && this.nodeTimeExecutionMillis === other.nodeTimeExecutionMillis;
  }
}
