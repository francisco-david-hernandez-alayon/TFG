package es.ull.project.domain.valueobject;

import java.net.URI;
import java.util.Optional;
import es.ull.project.domain.enumerate.NodeContentType;

/**
 * @brief Value object representing a file with a name and optional URI.
 */
public class File extends NodeContent {

    private static final String ERROR_FILENAME_NOT_DEFINED = "File name not defined";
    /**
     * Name of the file. It is a required attribute.
     */
    private final FileName name;
    /**
     * URI of the file. It is an optional attribute.
     */
    private final URI uri;

    public File(FileName name, URI uri) {
        validateName(name);
        this.name = name;
        this.uri = uri;
    }

    public File(FileName name) {
        this(name, null);
    }

    private void validateName(FileName name) {
        if (name == null) {
            throw new IllegalArgumentException(ERROR_FILENAME_NOT_DEFINED);
        }
    }

    public FileName getName() {
        return this.name;
    }

    /**
     * @return Optional containing the URI if present, or empty otherwise.
     */
    public Optional<URI> getUri() {
        return Optional.ofNullable(this.uri);
    }

    /**
     * @return true if the file has a URI.
     */
    public boolean hasUri() {
        return uri != null;
    }

    public File setName(FileName name) {
        return new File(name, this.uri);
    }

    public File setUri(URI uri) {
        return new File(this.name, uri);
    }

    @Override
    public NodeContentType getType() {
        return NodeContentType.FILE;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (!(otherObject instanceof File)) {
            return false;
        }
        File that = (File) otherObject;
        return this.name.equals(that.name) &&
                (this.uri == null ? that.uri == null : this.uri.equals(that.uri));
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (uri != null ? uri.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "File{name=%s, uri=%s}",
                this.name, this.uri);
    }
}
