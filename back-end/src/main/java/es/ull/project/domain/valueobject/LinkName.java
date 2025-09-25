package es.ull.project.domain.valueobject;

import es.ull.utils.string.UllString;

public class LinkName {

    private static final String ERROR_LINK_NAME_NOT_DEFINED = "Name not defined";
    private static final String ERROR_LINK_NAME_EMPTY = "Name is empty";
    private static final String ERROR_LINK_NAME_SIZE = "Name is too long";
    private static final String ERROR_LINK_NAME_WRONG_FORMAT = "Name must be alphanumeric";
    private static final int LINK_NAME_SIZE = 30;
    /**
     * Name for the link. It is a required attribute.
     */
    private final String value;

    public LinkName(String value) {
        validateName(value);
        this.value = value;
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException(ERROR_LINK_NAME_NOT_DEFINED);
        }
        name = name.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException(ERROR_LINK_NAME_EMPTY);
        }
        if (name.length() > LINK_NAME_SIZE) {
            throw new IllegalArgumentException(ERROR_LINK_NAME_SIZE);
        }
        if (!UllString.isAlphanumeric(name)) {
            throw new IllegalArgumentException(ERROR_LINK_NAME_WRONG_FORMAT);
        }
    }

    public String getValue() {
        return this.value;
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
        final LinkName otherName = (LinkName) otherObject;
        return this.value.equals(otherName.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public String toString() {
        return String.format(
                "LinkName={%s}",
                this.value);
    }
}
