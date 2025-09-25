export class LinkName {
    private static readonly ERROR_LINK_NAME_NOT_DEFINED = "Name not defined";
    private static readonly ERROR_LINK_NAME_EMPTY = "Name is empty";
    private static readonly ERROR_LINK_NAME_SIZE = "Name is too long";
    private static readonly ERROR_LINK_NAME_WRONG_FORMAT = "Name must be alphanumeric";
    private static readonly LINK_NAME_SIZE = 30;

    private readonly value: string;

    constructor(value: string) {
        this.validateName(value);
        this.value = value;
    }

    private validateName(name: string): void {
        if (!name) {
            throw new Error(LinkName.ERROR_LINK_NAME_NOT_DEFINED);
        }
        if (name.trim().length === 0) {
            throw new Error(LinkName.ERROR_LINK_NAME_EMPTY);
        }
        if (name.length > LinkName.LINK_NAME_SIZE) {
            throw new Error(LinkName.ERROR_LINK_NAME_SIZE);
        }
        if (!/^[a-zA-Z0-9]+$/.test(name)) {
            throw new Error(LinkName.ERROR_LINK_NAME_WRONG_FORMAT);
        }
    }

    getName(): string {
        return this.value;
    }

    setName(newName: string): LinkName {
        return new LinkName(newName);
    }


    equals(otherObject: any): boolean {
        if (this === otherObject) {
            return true;
        }
        if (!(otherObject instanceof LinkName)) {
            return false;
        }
        return this.value === otherObject.value;
    }

    toString(): string {
        return `LinkName={${this.value}}`;
    }
}
