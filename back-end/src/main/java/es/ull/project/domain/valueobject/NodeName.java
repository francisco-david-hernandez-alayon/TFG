package es.ull.project.domain.valueobject;

import java.util.Objects;

/**
 * @brief Value object representing the name of a Node.
 */
public final class NodeName {

    private static final String ERROR_NAME_NOT_DEFINED = "Node name not defined";
    private final String name;

    public NodeName(String name) {
        this.validateName(name);
        this.name = name.trim();
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(ERROR_NAME_NOT_DEFINED);
        }
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (!(otherObject instanceof NodeName)) {
            return false;
        }
        final NodeName other = (NodeName) otherObject;
        return this.name.equalsIgnoreCase(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    @Override
    public String toString() {
        return this.name;
    }
}
