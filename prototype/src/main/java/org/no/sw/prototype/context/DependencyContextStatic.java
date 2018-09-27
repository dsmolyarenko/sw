package org.no.sw.prototype.context;

public class DependencyContextStatic extends DependencyContextBase {

    @Override
    public <T> T get(Class<T> type, Object o, DependencyContext context) {

        if (o instanceof String) {
            String id = (String) o;

            T dependency = get(type, id);

            if (dependency == null) {
                throw new DependencyUnesolvedException(id);
            }

            return dependency;
        }

        throw new IllegalArgumentException("Unsupported object type: " + o);
    }

    public <T> DependencyContextStatic register(Class<T> type, String id, Object o) {
        super.set(type, id, o);
        return this;
    }

}
