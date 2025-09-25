package es.ull.project.domain.valueobject;

import java.util.Objects;

/**
 * @brief Value object representing a file name.
 */
public class FileName {

    private static final String ERROR_FILE_NAME_NULL_OR_EMPTY = "File Name cannot be null or empty";
    private final String name;

    public FileName(String name) {
        this.validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(ERROR_FILE_NAME_NULL_OR_EMPTY);
        }
    }

    public String getName() {
        return this.name;
    }

    public FileName setName(String name) {
        return new FileName(name);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (!(otherObject instanceof FileName)) {
            return false;
        }
        final FileName fileName = (FileName) otherObject;
        return Objects.equals(name, fileName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
