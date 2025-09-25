export class OperationName {
    private static readonly ERROR_OPERATION_NAME_WRONG_FORMAT = "Invalid name for operation. Only alphanumeric characters are allowed.";
    private static readonly ERROR_OPERATION_NAME_NOT_DEFINED = "Name for operation not defined";
    private static readonly ERROR_OPERATION_NAME_EMPTY = "Name for operation is empty";
    private static readonly ERROR_OPERATION_NAME_SIZE = "Size for name is too long";
    private static readonly OPERATION_NAME_SIZE = 50;

    private readonly name: string;

    constructor(name: string) {
        this.validateName(name);
        this.name = name;
    }

    private validateName(name: string): void {
        if (!name) {
            throw new Error(OperationName.ERROR_OPERATION_NAME_NOT_DEFINED);
        }
        if (name.trim().length === 0) {
            throw new Error(OperationName.ERROR_OPERATION_NAME_EMPTY);
        }
        if (name.length > OperationName.OPERATION_NAME_SIZE) {
            throw new Error(OperationName.ERROR_OPERATION_NAME_SIZE);
        }

        // to permit import operations that have '_'
        // if (!/^[a-zA-Z0-9]+$/.test(name)) {
        //     throw new Error(OperationName.ERROR_OPERATION_NAME_WRONG_FORMAT);
        // }
    }

    public getName(): string {
        return this.name;
    }

    public setName(newName: string): OperationName {
        return new OperationName(newName);
    }

    public equals(otherObject: any): boolean {
        if (this === otherObject) {
            return true;
        }
        if (!(otherObject instanceof OperationName)) {
            return false;
        }
        return this.name === otherObject.name;
    }

    public toString(): string {
        return `OperationName={${this.name}}`;
    }
}
