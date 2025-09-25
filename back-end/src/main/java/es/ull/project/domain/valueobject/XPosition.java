package es.ull.project.domain.valueobject;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @brief Represents the X position of a node in a workflow.
 */
public class XPosition {

    private static final String ERROR_X_POSITION_INVALID = "X position must be a valid non-null BigDecimal";
    private final BigDecimal position;

    public XPosition(BigDecimal xPosition) {
        this.validate(xPosition);
        this.position = xPosition;
    }

    private void validate(BigDecimal xPosition) {
        if (xPosition == null) {
            throw new IllegalArgumentException(ERROR_X_POSITION_INVALID);
        }
    }

    public BigDecimal getXPosition() {
        return this.position;
    }

    public XPosition setXPosition(BigDecimal xPosition) {
        return new XPosition(xPosition);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (!(otherObject instanceof XPosition)) {
            return false;
        }
        final XPosition that = (XPosition) otherObject;
        return this.position.compareTo(that.position) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.position);
    }

    @Override
    public String toString() {
        return this.position.toString();
    }
}