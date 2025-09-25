package es.ull.project.domain.valueobject;

/**
 * @brief Class that allows to link a node with another node.
 */
public final class Link {

    private static final String ERROR_INITIAL_NODE_NOT_DEFINED = "Initial node not defined";
    private static final String ERROR_FINAL_NODE_NOT_DEFINED = "Final node not defined";
    private static final String ERROR_LINK_NAME_NOT_DEFINED = "Name not defined";
    private final LinkName name;
    private final NodeName initialNode;
    private final NodeName finalNode;
    private final Boolean conditionLink;

    public Link(LinkName name, NodeName initialNode, NodeName finalNode, Boolean conditionLink) {
        validateName(name);
        validateInitialNode(initialNode);
        validateFinalNode(finalNode);
        this.name = name;
        this.initialNode = initialNode;
        this.finalNode = finalNode;
        this.conditionLink = conditionLink;
    }

    private void validateName(LinkName name) {
        if (name == null) {
            throw new IllegalArgumentException(ERROR_LINK_NAME_NOT_DEFINED);
        }
    }

    private void validateInitialNode(NodeName node) {
        if (node == null) {
            throw new IllegalArgumentException(ERROR_INITIAL_NODE_NOT_DEFINED);
        }
    }

    private void validateFinalNode(NodeName node) {
        if (node == null) {
            throw new IllegalArgumentException(ERROR_FINAL_NODE_NOT_DEFINED);
        }
    }

    public LinkName getName() {
        return this.name;
    }

    public NodeName getInitialNode() {
        return this.initialNode;
    }

    public NodeName getFinalNode() {
        return this.finalNode;
    }

    public Boolean getCondition() {
        return this.conditionLink;
    }

    public Link setName(LinkName name) {
        return new Link(name, this.initialNode, this.finalNode, this.conditionLink);
    }

    public Link setInitialNode(NodeName initialNode) {
        return new Link(name, initialNode, this.finalNode, this.conditionLink);
    }

    public Link setFinalNode(NodeName finalNode) {
        return new Link(name, this.initialNode, finalNode, conditionLink);
    }

    public Link setCondition(Boolean conditionLink) {
        return new Link(name, this.initialNode, this.finalNode, conditionLink);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (!(otherObject instanceof Link)) {
            return false;
        }
        Link link = (Link) otherObject;
        return name.equals(link.name) &&
                initialNode.equals(link.initialNode) &&
                finalNode.equals(link.finalNode) &&
                conditionLink.equals(link.conditionLink);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + initialNode.hashCode();
        result = 31 * result + finalNode.hashCode();
        result = 31 * result + conditionLink.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "Link={initialNode=%s, finalNode=%s, name=%s}",
                this.initialNode.getName(), this.finalNode.getName(), name);
    }
}
