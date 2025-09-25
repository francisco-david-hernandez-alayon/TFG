export class WorkFlowName {
  private static readonly MAX_NAME_LENGTH = 40;

  private static readonly ERROR_FLOW_NAME_WRONG_FORMAT = 'Invalid name for workflow. Only alphanumeric characters are allowed.';
  private static readonly ERROR_FLOW_NAME_NOT_DEFINED = 'Name for workflow not defined';
  private static readonly ERROR_FLOW_NAME_EMPTY = 'Name for workflow is empty';
  private static readonly ERROR_FLOW_NAME_SIZE = 'Size for name is too long';

  private readonly name: string;

  constructor(name: string) {
    this.validateName(name);
    this.name = name;
  }

  private validateName(name: string): void {
    if (name == null) {
      throw new Error(WorkFlowName.ERROR_FLOW_NAME_NOT_DEFINED);
    }
    if (name.trim().length === 0) {
      throw new Error(WorkFlowName.ERROR_FLOW_NAME_EMPTY);
    }
    if (name.length > WorkFlowName.MAX_NAME_LENGTH) {
      throw new Error(WorkFlowName.ERROR_FLOW_NAME_SIZE);
    }
    if (!WorkFlowName.isAlphanumeric(name)) {
      throw new Error(WorkFlowName.ERROR_FLOW_NAME_WRONG_FORMAT);
    }
  }

  private static isAlphanumeric(str: string): boolean {
    return /^[a-zA-Z0-9]+$/.test(str);
  }

  getName(): string {
    return this.name;
  }

  setName(newName: string): WorkFlowName {
    return new WorkFlowName(newName);
  }

  equals(other: WorkFlowName): boolean {
    return other instanceof WorkFlowName && this.name === other.name;
  }

  toString(): string {
    return `FlowName={${this.name}}`;
  }

}
