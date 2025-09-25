export class NodeName {
    private static readonly ERROR_NAME_NOT_DEFINED = "Node name not defined";
    private readonly name: string;

    constructor(name: string) {
        this.validateName(name);
        this.name = name;
    }

    getName(): string {
        return this.name;
    }

    setName(name: string): NodeName {
        this.validateName(name);
        return new NodeName(name);
    }


    validateName(name: string): void {
        if (!name || name.trim().length === 0) {
            throw new Error(NodeName.ERROR_NAME_NOT_DEFINED);
        }
    }

    equals(other: NodeName): boolean {
        return other instanceof NodeName && this.name === other.name;
    }

    toString(): string {
        return `NodeName={${this.name}}`;
    }
}
