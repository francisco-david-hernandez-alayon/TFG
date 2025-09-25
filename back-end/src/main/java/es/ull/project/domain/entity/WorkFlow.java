package es.ull.project.domain.entity;

import es.ull.project.domain.valueobject.WorkFlowName;
import es.ull.project.domain.valueobject.Link;
import es.ull.project.domain.valueobject.Node;
import es.ull.project.domain.valueobject.NodeName;

import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @brief Represents a workflow with a list of nodes.
 */
public class WorkFlow {

    private static final String ERROR_NAME_NOT_DEFINED = "Workflow name not defined";
    private static final String ERROR_NODES_NOT_DEFINED = "Nodes not defined";
    private static final String ERROR_NODES_EMPTY = "Nodes cannot be empty";
    private static final String ERROR_DATE_NOT_DEFINED = "Date not defined";
    private static final String ERROR_FIRST_NODE_NOT_FOUND = "No initial node found: every node is a destination of at least one link.";
    private static final String ERROR_MULTIPLE_FIRST_NODES = "Multiple initial nodes found: more than one node has no incoming links.";

    private final UUID id;
    private final WorkFlowName name;
    private final LocalDateTime creationDate;
    private final List<Node> nodes;
    private final boolean enabled;
    /**
     * computed atribute
     */
    private Node firstNode;

    public WorkFlow(
            WorkFlowName name,
            List<Node> nodes, boolean enabled) {
        validateName(name);
        validateNodes(nodes);

        this.id = UUID.randomUUID();
        this.name = name;
        this.nodes = new ArrayList<>(nodes);
        this.creationDate = LocalDateTime.now();
        this.firstNode = computeFirstNode();
        this.enabled = enabled;
    }

    public WorkFlow(
            UUID id,
            WorkFlowName name,
            List<Node> nodes,
            LocalDateTime creationDate, boolean enabled) {

        validateName(name);
        validateNodes(nodes);
        validateDate(creationDate);
        this.id = id;
        this.name = name;
        this.nodes = new ArrayList<>(nodes);
        this.creationDate = creationDate;
        this.firstNode = computeFirstNode();
        this.enabled = enabled;
    }

    private void validateName(WorkFlowName name) {
        if (name == null) {
            throw new IllegalArgumentException(ERROR_NAME_NOT_DEFINED);
        }
    }

    private void validateNodes(List<Node> nodes) {
        if (nodes == null) {
            throw new IllegalArgumentException(ERROR_NODES_NOT_DEFINED);
        }
        if (nodes.isEmpty()) {
            throw new IllegalArgumentException(ERROR_NODES_EMPTY);
        }
    }

    private void validateDate(LocalDateTime date) {
        if (date == null) {
            throw new IllegalArgumentException(ERROR_DATE_NOT_DEFINED);
        }
    }

    /**
     * @brief computes the first node of the workflow.
     * @return
     */
    private Node computeFirstNode() {
        Set<NodeName> allNodes = nodes.stream()
                .map(Node::getName)
                .collect(Collectors.toSet());
        Set<NodeName> destinationNodes = new HashSet<>();
        for (Node node : nodes) {
            for (Link link : node.getLinks()) {
                destinationNodes.add(link.getFinalNode());
            }
        }
        // Remove all destination nodes from the set of all nodes
        allNodes.removeAll(destinationNodes);
        if (allNodes.isEmpty()) {
            throw new IllegalStateException(ERROR_FIRST_NODE_NOT_FOUND);
        } else if (allNodes.size() > 1) {
            throw new IllegalStateException(ERROR_MULTIPLE_FIRST_NODES);
        }
        NodeName firstNodeName = allNodes.iterator().next();
        return nodes.stream()
                .filter(node -> node.getName().equals(firstNodeName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(ERROR_FIRST_NODE_NOT_FOUND));
    }

    public Node getFirstNode() {
        return this.firstNode;
    }

    public UUID getId() {
        return this.id;
    }

    public WorkFlowName getName() {
        return this.name;
    }

    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(this.nodes);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public WorkFlow setName(WorkFlowName name) {
        return new WorkFlow(this.id, name, this.nodes, this.creationDate, this.enabled);
    }

    public WorkFlow setCreationDate(LocalDateTime creationDate) {
        return new WorkFlow(this.id, this.name, this.nodes, creationDate, this.enabled);
    }

    public WorkFlow setNodes(List<Node> nodes) {
        return new WorkFlow(this.id, this.name, nodes, this.creationDate, this.enabled);
    }

    public WorkFlow setEnabled(boolean enabled) {
        return new WorkFlow(this.id, this.name, this.nodes, this.creationDate, enabled);
    }

    public WorkFlow activate() {
        return new WorkFlow(this.id, this.name, this.nodes, this.creationDate, true);
    }

    public WorkFlow disactivate() {
        return new WorkFlow(this.id, this.name, this.nodes, this.creationDate, false);
    }

    @Override
    public String toString() {
        return String.format(
                "Workflow={id=%s, name=%s, creationDate=%s, enabled=%s, nodes=%s, firstNode=%s}",
                this.id, this.name, this.creationDate, this.enabled, this.nodes, this.firstNode);
    }
}
