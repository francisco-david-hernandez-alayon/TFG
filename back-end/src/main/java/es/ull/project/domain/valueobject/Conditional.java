package es.ull.project.domain.valueobject;

import es.ull.project.domain.enumerate.NodeContentType;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * @brief Represents a Conditional node with a name, execution code, and a map of node link conditions
 */
public class Conditional extends NodeContent {

    private static final String ERROR_NAME_NOT_DEFINED = "Conditional name is not defined";
    private static final String ERROR_CODE_NOT_DEFINED = "Execution code for conditional is not defined";
    private static final String ERROR_LINKS_NOT_DEFINED = "Links conditions map is not defined";

    private final ConditionalName name;
    private final String executionCode;

    /**
     * @brief for each destination node, indicates whether the link condition is true or false
     */
    private final Map<NodeName, Boolean> linksConditions;

    public Conditional(ConditionalName name, String executionCode, Map<NodeName, Boolean> linksConditions) {
        validateName(name);
        validateCode(executionCode);
        validateLinks(linksConditions);
        this.name = name;
        this.executionCode = executionCode;
        this.linksConditions = Collections.unmodifiableMap(linksConditions);
    }

    private void validateName(ConditionalName name) {
        if (name == null) {
            throw new IllegalArgumentException(ERROR_NAME_NOT_DEFINED);
        }
    }

    private void validateCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException(ERROR_CODE_NOT_DEFINED);
        }
    }

    private void validateLinks(Map<NodeName, Boolean> links) {
        if (links == null) {
            throw new IllegalArgumentException(ERROR_LINKS_NOT_DEFINED);
        }
    }

    public ConditionalName getName() {
        return this.name;
    }

    public String getExecutionCode() {
        return this.executionCode;
    }

    public Map<NodeName, Boolean> getLinksConditions() {
        return this.linksConditions;
    }

    public Conditional setName(ConditionalName name) {
        return new Conditional(name, this.executionCode, this.linksConditions);
    }

    public Conditional setExecutionCode(String code) {
        return new Conditional(this.name, code, this.linksConditions);
    }

    public Conditional setLinksConditions(Map<NodeName, Boolean> linksConditions) {
        return new Conditional(this.name, this.executionCode, linksConditions);
    }

    @Override
    public NodeContentType getType() {
        return NodeContentType.CONDITIONAL;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (!(otherObject instanceof Conditional)) return false;
        final Conditional that = (Conditional) otherObject;
        return name.equals(that.name)
                && executionCode.equals(that.executionCode)
                && linksConditions.equals(that.linksConditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, executionCode, linksConditions);
    }

    @Override
    public String toString() {
        return String.format(
                "Conditional={name=%s, executionCode=%s, linksConditions=%s}",
                name, executionCode, linksConditions
        );
    }
}
