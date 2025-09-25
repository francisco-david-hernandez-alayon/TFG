package es.ull.project.domain.valueobject;

import java.io.Serializable;

import es.ull.utils.string.UllString;

/**
 * @brief Represents the name of a Conditional. Must be alphanumeric, non-null, and under max size.
 */
public final class ConditionalName implements Serializable {

    private static final String ERROR_NAME_NULL = "Name for conditional is not defined";
    private static final String ERROR_NAME_EMPTY = "Name for conditional is empty";
    private static final String ERROR_NAME_FORMAT = "Invalid name for conditional. Only alphanumeric characters are allowed.";
    private static final String ERROR_NAME_SIZE = "Size for conditional name is too long";

    private static final int MAX_NAME_SIZE = 50;

    private final String name;

    public ConditionalName(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException(ERROR_NAME_NULL);
        }
        name = name.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException(ERROR_NAME_EMPTY);
        }
        if (name.length() > MAX_NAME_SIZE) {
            throw new IllegalArgumentException(ERROR_NAME_SIZE);
        }
        if (!UllString.isAlphanumeric(name)) {
            throw new IllegalArgumentException(ERROR_NAME_FORMAT);
        }
    }

    public String getName() {
        return this.name;
    }

    public ConditionalName setName(String newName) {
        return new ConditionalName(newName);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (!(otherObject instanceof ConditionalName)) return false;
        final ConditionalName that = (ConditionalName) otherObject;
        return this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public String toString() {
        return String.format("ConditionalName={%s}", this.name);
    }
}
