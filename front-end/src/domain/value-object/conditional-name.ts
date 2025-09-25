/**
 * Represents the name of a Conditional.
 */
export class ConditionalName {
  private static readonly ERROR_NAME_NULL = 'Name for conditional is not defined';
  private static readonly ERROR_NAME_EMPTY = 'Name for conditional is empty';
  private static readonly ERROR_NAME_FORMAT = 'Invalid name for conditional. Only alphanumeric characters are allowed.';
  private static readonly ERROR_NAME_TOO_LONG = 'Size for conditional name is too long';

  private static readonly MAX_LENGTH = 50;

  private readonly name: string;

  constructor(name: string) {
    this.validateName(name);
    this.name = name;
  }

  private validateName(name: string): void {
    if (name === null || name === undefined) {
      throw new Error(ConditionalName.ERROR_NAME_NULL);
    }

    const trimmed = name.trim();

    if (trimmed.length === 0) {
      throw new Error(ConditionalName.ERROR_NAME_EMPTY);
    }

    if (trimmed.length > ConditionalName.MAX_LENGTH) {
      throw new Error(ConditionalName.ERROR_NAME_TOO_LONG);
    }

    if (!/^[a-zA-Z0-9]+$/.test(trimmed)) {
      throw new Error(ConditionalName.ERROR_NAME_FORMAT);
    }
  }

  public getName(): string {
    return this.name;
  }

  public setName(newName: string): ConditionalName {
    return new ConditionalName(newName);
  }

  public toString(): string {
    return `ConditionalName={${this.name}}`;
  }

  public equals(other: ConditionalName): boolean {
    return this.name === other.getName();
  }
}
