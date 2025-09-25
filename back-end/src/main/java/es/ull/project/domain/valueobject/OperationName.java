package es.ull.project.domain.valueobject;

import java.io.Serializable;

import es.ull.utils.string.UllString;

/**
 * @brief represents a name for an operation
 */
public final class OperationName implements Serializable {

    private static final String ERROR_OPERATION_NAME_WRONG_FORMAT = "Invalid name for operation. Only alphanumeric characters are allowed.";
    private static final String ERROR_OPERATION_NAME_NOT_DEFINED = "Name for operation not defined";
    private static final String ERROR_OPERATION_NAME_EMPTY = "Name for operation is empty";
    private static final String ERROR_OPERATION_NAME_SIZE = "Size for name is too long";
    private static final int OPERATION_NAME_SIZE = 50;
    /**
     * Name for operation. It is a required attribute.
     */
    private final String name;

    public OperationName(String name) {
        this.validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException(ERROR_OPERATION_NAME_NOT_DEFINED);
        }
        name = name.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException(ERROR_OPERATION_NAME_EMPTY);
        }
        if (name.length() > OPERATION_NAME_SIZE) {
            throw new IllegalArgumentException(ERROR_OPERATION_NAME_SIZE);
        }

        // to permit import operations that have '_'
        // if (!UllString.isAlphanumeric(name)) {
        //     throw new IllegalArgumentException(ERROR_OPERATION_NAME_WRONG_FORMAT);
        // }
    }

    public String getName() {
        return this.name;
    }

    public OperationName setName(String newName) {
        return new OperationName(newName);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null) {
            return false;
        }
        if (this.getClass() != otherObject.getClass()) {
            return false;
        }
        final OperationName otherName = (OperationName) otherObject;
        return this.name.equals(otherName.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public String toString() {
        return String.format(
                "OperationName={%s}",
                this.name);
    }
}
