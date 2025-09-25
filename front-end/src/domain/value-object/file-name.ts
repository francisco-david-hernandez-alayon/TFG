export class FileName {
    private static readonly ERROR_NAME_NOT_DEFINED = 'File Name cannot be null or empty';
    private readonly name: string;

    constructor(name: string) {
        this.validateName(name);
        this.name = name;
    }

    getName(): string {
        return this.name;
    }

    setName(name: string): FileName {
        return new FileName(name);
    }


    validateName(name: string): void {
        if (!name || name.trim() === '') {
            throw new Error(FileName.ERROR_NAME_NOT_DEFINED);
        }
    }

    equals(other: FileName): boolean {
        return other instanceof FileName && this.name === other.name;
    }

    toString(): string {
        return `FileName={${this.name}}`;
    }
}
