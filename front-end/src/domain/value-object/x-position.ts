export class XPosition {
    private static readonly ERROR_X_POSITION_INVALID = 'X position must be a valid number (not NaN)';

    private readonly xPosition: number;

    constructor(xPosition: number) {
        XPosition.validate(xPosition);
        this.xPosition = xPosition;
    }

    private static validate(xPosition: number): void {
        if (Number.isNaN(xPosition)) {
            throw new Error(XPosition.ERROR_X_POSITION_INVALID);
        }
    }

    public getXPosition(): number {
        return this.xPosition;
    }

    public setXPosition(xPosition: number): XPosition {
        return new XPosition(xPosition);
    }

    public toString(): string {
        return this.xPosition.toString();
    }

    public equals(other: unknown): boolean {
        return (
            other instanceof XPosition &&
            this.xPosition === other.xPosition
        );
    }

    public hashCode(): number {
        const buffer = new ArrayBuffer(8);
        new Float64Array(buffer)[0] = this.xPosition;
        return new Int32Array(buffer)[0];
    }
}
