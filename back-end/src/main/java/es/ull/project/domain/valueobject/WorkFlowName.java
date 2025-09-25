package es.ull.project.domain.valueobject;

import es.ull.utils.string.UllString;

/**
 * @brief This class represents the name of a workflow.
 *        It is a required attribute and must be alphanumeric.
 */
public class WorkFlowName {

    private static final String ERROR_FLOW_NAME_WRONG_FORMAT = "Invalid name for workflow. Only alphanumeric characters are allowed.";
    private static final String ERROR_FLOW_NAME_NOT_DEFINED = "Name for workflow not defined";
    private static final String ERROR_FLOW_NAME_EMPTY = "Name for workflow is empty";
    private static final String ERROR_FLOW_NAME_SIZE = "Size for name is too long";
    private static final int FLOW_NAME_SIZE = 40;
    /**
     * Name for workflow. It is a required attribute.
     */
    private final String name;

    public WorkFlowName(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException(ERROR_FLOW_NAME_NOT_DEFINED);
        }
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException(ERROR_FLOW_NAME_EMPTY);
        }
        if (name.length() > FLOW_NAME_SIZE) {
            throw new IllegalArgumentException(ERROR_FLOW_NAME_SIZE);
        }
        if (!UllString.isAlphanumeric(name)) {
            throw new IllegalArgumentException(ERROR_FLOW_NAME_WRONG_FORMAT);
        }
    }

    public String getName() {
        return this.name;
    }

    public WorkFlowName setName(String newName) {
        return new WorkFlowName(newName);
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
        final WorkFlowName otherName = (WorkFlowName) otherObject;
        return this.name.equals(otherName.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return String.format(
                "WorkflowName={%s}",
                this.name);
    }
}
