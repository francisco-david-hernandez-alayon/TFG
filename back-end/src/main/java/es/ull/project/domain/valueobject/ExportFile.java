package es.ull.project.domain.valueobject;

import java.util.Objects;

import es.ull.project.domain.entity.WorkFlow;

/**
 * @brief Represents an exported file(JSON) with name and binary data.
 */
public class ExportFile {

    private static final String ERROR_DATA_NOT_DEFINED = "File data not defined";
    private final WorkFlow data;

    /**
     * Constructor for ExportFile.
     * 
     * @param fileName
     * @param data
     */
    public ExportFile(WorkFlow data) {
        this.validateData(data);
        this.data = data;
    }

    private void validateData(WorkFlow data) {
        if (data == null) {
            throw new IllegalArgumentException(ERROR_DATA_NOT_DEFINED);
        }
    }

    public WorkFlow getData() {
        return this.data;
    }

    public ExportFile setData(WorkFlow data) {
        return new ExportFile(data);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (!(otherObject instanceof ExportFile)) {
            return false;
        }
        final ExportFile that = (ExportFile) otherObject;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        return String.format(
                "ExportFile={data=%s}",
                this.data);
    }
}
