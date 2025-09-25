package es.ull.project.domain.valueobject;

import es.ull.project.domain.enumerate.NodeColor;
import es.ull.project.domain.enumerate.NodeContentType;
import es.ull.project.domain.enumerate.NodeIcon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @brief Represents a node in the workflow.
 */
public class Node {

    private static final String ERROR_COLOR_NOT_DEFINED = "Node color not defined.";
    private static final String ERROR_NODE_NAME_NOT_DEFINED = "Node name not defined.";
    private static final String ERROR_CONTENT_NOT_DEFINED = "Node content not defined.";
    private static final String ERROR_LINKS_NOT_DEFINED = "Node links not defined.";
    private static final String ERROR_POSITION_INVALID = " position is not defined";
    private static final String ERROR_ICON_NOT_DEFINED = "Node icon not defined";
    private static final String ERROR_ADD_NULL_LINK = "Cannot add null link.";
    private static final String ERROR_LINK_INITIAL_NODE_MISMATCH = "Link's initial node name must match this node's name.";
    private static final String ERROR_REMOVE_NULL_LINK = "Cannot remove null link.";
    private final NodeName name;
    private final XPosition xPosition;
    private final YPosition yPosition;
    private final NodeColor color;
    private final NodeIcon icon;
    private final NodeContent content;
    private final List<Link> links;

    public Node(
            NodeName name,
            XPosition x,
            YPosition y,
            NodeColor color,
            NodeIcon icon,
            NodeContent content,
            List<Link> links) {
        validateNodeName(name);
        validatePositionX(x);
        validatePositionY(y);
        validateColor(color);
        validateIcon(icon);
        validateContent(content);
        validateLinks(links);
        this.name = name;
        this.xPosition = x;
        this.yPosition = y;
        this.color = color;
        this.content = content;
        this.icon = icon;
        this.links = new ArrayList<>(links);
    }

    private void validatePositionX(XPosition coordinate) {
        if (coordinate == null) {
            throw new IllegalArgumentException("X" + ERROR_POSITION_INVALID);
        }
    }

    private void validatePositionY(YPosition coordinate) {
        if (coordinate == null) {
            throw new IllegalArgumentException("Y" + ERROR_POSITION_INVALID);
        }
    }

    private void validateColor(NodeColor color) {
        if (color == null) {
            throw new IllegalArgumentException(ERROR_COLOR_NOT_DEFINED);
        }
    }

    private void validateIcon(NodeIcon icon) {
        if (icon == null) {
            throw new IllegalArgumentException(ERROR_ICON_NOT_DEFINED);
        }
    }

    private void validateNodeName(NodeName name) {
        if (name == null) {
            throw new IllegalArgumentException(ERROR_NODE_NAME_NOT_DEFINED);
        }
    }

    private void validateContent(NodeContent content) {
        if (content == null) {
            throw new IllegalArgumentException(ERROR_CONTENT_NOT_DEFINED);
        }
    }

    private void validateLinks(List<Link> links) {
        if (links == null) {
            throw new IllegalArgumentException(ERROR_LINKS_NOT_DEFINED);
        }
    }

    public NodeName getName() {
        return name;
    }

    public XPosition getXPosition() {
        return xPosition;
    }

    public YPosition getYPosition() {
        return yPosition;
    }

    public NodeColor getColor() {
        return color;
    }

    public NodeContent getContent() {
        return content;
    }

    public NodeIcon getIcon() {
        return icon;
    }

    public NodeContentType getType() {
        return content.getType();
    }

    public List<Link> getLinks() {
        return Collections.unmodifiableList(links);
    }

    public Node setXPosition(XPosition x) {
        return new Node(this.name, x, this.yPosition, this.color, this.icon, this.content, this.links);
    }

    public Node setYPosition(YPosition y) {
        return new Node(this.name, this.xPosition, y, this.color, this.icon, this.content, this.links);
    }

    public Node setColor(NodeColor color) {
        return new Node(this.name, this.xPosition, this.yPosition, color, this.icon, this.content, this.links);
    }

    public Node setIcon(NodeIcon icon) {
        return new Node(this.name, this.xPosition, this.yPosition, this.color, icon, this.content, this.links);
    }

    public Node setLinks(List<Link> links) {
        return new Node(this.name, this.xPosition, this.yPosition, this.color, this.icon, this.content, links);
    }

    public Node addLink(Link link) {
        if (link == null) {
            throw new IllegalArgumentException(ERROR_ADD_NULL_LINK);
        }
        if (!this.name.equals(link.getInitialNode())) {
            throw new IllegalArgumentException(ERROR_LINK_INITIAL_NODE_MISMATCH);
        }
        final List<Link> updatedLinks = new ArrayList<>(links);
        updatedLinks.add(link);
        return new Node(this.name, this.xPosition, this.yPosition, this.color, this.icon, this.content, updatedLinks);
    }

    public Node removeLink(Link link) {
        if (link == null) {
            throw new IllegalArgumentException(ERROR_REMOVE_NULL_LINK);
        }
        final List<Link> updatedLinks = new ArrayList<>(links);
        updatedLinks.remove(link);
        return new Node(this.name, this.xPosition, this.yPosition, this.color, this.icon, this.content, updatedLinks);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (!(otherObject instanceof Node)) {
            return false;
        }
        final Node otherNode = (Node) otherObject;
        return this.name.equals(otherNode.name) &&
                this.xPosition.equals(otherNode.xPosition) &&
                this.yPosition.equals(otherNode.yPosition) &&
                this.color == otherNode.color &&
                this.icon == otherNode.icon &&
                this.content.equals(otherNode.content) &&
                this.links.equals(otherNode.links);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + xPosition.hashCode();
        result = 31 * result + yPosition.hashCode();
        result = 31 * result + color.hashCode();
        result = 31 * result + icon.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + links.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "Node={name=%s, x=%.2f, y=%.2f, color=%s, icon=%s type=%s, links=%d}",
                this.name, this.xPosition.getXPosition(), this.yPosition.getYPosition(), this.color, this.icon,
                this.getType(), this.links.size());
    }
}
