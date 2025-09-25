import { NodeContentType } from "../enumerate/node-content-type";
import { FileName } from "./file-name";
import { NodeContent } from "./node-content";

export class File extends NodeContent {
    private static readonly ERROR_FILENAME_NOT_DEFINED = "File name not defined";

    private readonly name: FileName;
    private readonly uri?: string; // optional

    constructor(name: FileName, uri?: string) {
        super();
        this.validateName(name);
        this.name = name;
        this.uri = uri;
    }

    getName(): FileName {
        return this.name;
    }

    getUri(): string | undefined {
        return this.uri;
    }

    hasUri(): boolean {
        return this.uri !== undefined && this.uri.trim() !== "";
    }

    setName(name: FileName): File {
        this.validateName(name);
        return new File(name, this.uri);
    }

    setUri(uri?: string): File {
        return new File(this.name, uri);
    }


    private validateName(name: FileName): void {
        if (!name) throw new Error(File.ERROR_FILENAME_NOT_DEFINED);
    }

    getType(): NodeContentType {
        return NodeContentType.FILE;
    }

    equals(other: unknown): boolean {
        if (!(other instanceof File)) return false;
        const otherFile = other as File;

        const nameEquals = this.name.equals(otherFile.getName());
        const uriEquals = this.uri === otherFile.getUri();

        return nameEquals && uriEquals;
    }

    toString(): string {
        return `File{name=${this.name.toString()}, uri=${this.uri ?? "undefined"}}`;
    }
}
