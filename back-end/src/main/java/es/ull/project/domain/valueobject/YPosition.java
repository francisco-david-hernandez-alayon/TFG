package es.ull.project.domain.valueobject;

import java.math.BigDecimal;
import java.util.Objects;

/*
 * @brief Represents the Y position of a node in a workflow.
 */
public class YPosition {

    private static final String ERROR_Y_POSITION_INVALID = "Y position must be a valid non-null BigDecimal";
    private final BigDecimal position;

    public YPosition(BigDecimal yPosition) {
        validate(yPosition);
        this.position = yPosition;
    }

    private static void validate(BigDecimal yPosition) {
        if (yPosition == null) {
            throw new IllegalArgumentException(ERROR_Y_POSITION_INVALID);
        }
    }

    public BigDecimal getYPosition() {
        return this.position;
    }

    public YPosition setYPosition(BigDecimal yPosition) {
        return new YPosition(yPosition);
    }

    @Override
    public String toString() {
        return position.toString();
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (!(otherObject instanceof YPosition)) {
            return false;
        }
        YPosition that = (YPosition) otherObject;
        return this.position.compareTo(that.position) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }
}
