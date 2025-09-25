package es.ull.project.domain.valueobject;

import java.time.Duration;
import java.util.Objects;

/**
 * @brief Value object representing the execution data of a node.
 */
public class NodeExecuteData {

    private final NodeName nodeName;
    private final Duration duration;

    public NodeExecuteData(NodeName nodeName, Duration duration) {
        this.nodeName = nodeName;
        this.duration = duration;
    }

    public NodeName getNodeName() {
        return this.nodeName;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public NodeExecuteData setNodeName(NodeName nodeName) {
        return new NodeExecuteData(nodeName, this.duration);
    }

    public NodeExecuteData setDuration(Duration duration) {
        return new NodeExecuteData(this.nodeName, duration);
    }

    @Override
    public String toString() {
        return String.format(
                "NodeExecuteData={node=%s, duration=%dms}",
                this.nodeName.getName(), this.duration.toMillis());
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (!(otherObject instanceof NodeExecuteData)) {
            return false;
        }
        final NodeExecuteData that = (NodeExecuteData) otherObject;
        return Objects.equals(nodeName, that.nodeName) &&
                Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeName, duration);
    }
}