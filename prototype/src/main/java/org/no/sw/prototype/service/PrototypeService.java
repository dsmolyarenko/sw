package org.no.sw.prototype.service;

import java.util.List;
import java.util.Map;

import org.no.sw.core.util.MapAccessor;

public interface PrototypeService {

    void collect(Map<String, String> prototype);

    Map<String, MapAccessor> getAll() throws DependencyUnesolvedException;

    public class DependencyUnesolvedException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        private final List<String> ids;

        public DependencyUnesolvedException(List<String> ids) {
            super("Unresolved items: " + ids);
            this.ids = ids;
        }

        public List<String> getIds() {
            return ids;
        }
    }
}
