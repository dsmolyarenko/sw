package org.no.sw.prototype.context;

public class DependencyUnesolvedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String id;

    public DependencyUnesolvedException(String id) {
        super(id);
        this.id = id;
    }

    public String getId() {
        return id;
    }

}