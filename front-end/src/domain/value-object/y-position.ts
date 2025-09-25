export class YPosition {
    private static readonly ERROR_Y_POSITION_INVALID = 'Y position must be a valid number (not NaN)';

    private readonly yPosition: number;

    constructor(yPosition: number) {
        YPosition.validate(yPosition);
        this.yPosition = yPosition;
    }

    private static validate(yPosition: number): void {
        if (Number.isNaN(yPosition)) {
            throw new Error(YPosition.ERROR_Y_POSITION_INVALID);
        }
    }

    public getYPosition(): number {
        return this.yPosition;
    }

    public setYPosition(yPosition: number): YPosition {
        return new YPosition(yPosition);
    }

    public toString(): string {
        return this.yPosition.toString();
    }

    public equals(other: unknown): boolean {
        return (
            other instanceof YPosition &&
            this.yPosition === other.yPosition
        );
    }

    public hashCode(): number {
        const buffer = new ArrayBuffer(8);
        new Float64Array(buffer)[0] = this.yPosition;
        return new Int32Array(buffer)[0];
    }
}
