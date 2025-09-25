package es.ull.project.domain.valueobject;

import es.ull.project.domain.enumerate.NodeContentType;

public abstract class NodeContent {

    public abstract NodeContentType getType();

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (!(otherObject instanceof NodeContent)) {
            return false;
        }
        final NodeContent that = (NodeContent) otherObject;
        return this.getType().equals(that.getType());
    }

    @Override
    public int hashCode() {
        return getType().hashCode();
    }
}
