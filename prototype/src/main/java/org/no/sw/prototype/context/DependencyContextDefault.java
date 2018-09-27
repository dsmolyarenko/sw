package org.no.sw.prototype.context;

import static org.no.generator.configuration.util.ObjectUtils.merge;
import static org.no.generator.configuration.util.ObjectUtils.safe;

import java.util.Map;

public class DependencyContextDefault extends DependencyContextBase {

    private final DependencyContext dependencyParentContext;

    private final DependencyFactory dependencyFactory;

    public DependencyContextDefault(DependencyContext parentContext, DependencyFactory factory) {
        this.dependencyParentContext = parentContext;
        this.dependencyFactory = factory;
    }

    @Override
    public <T> T get(Class<T> type, Object o, DependencyContext context) {

        if (o == null) {
            try {
                o = type.getDeclaredField("DEFAULT").get(null);
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                throw new IllegalArgumentException(e);
            }
        }

        if (o instanceof String) {
            String id = (String) o;

            T dependency = get(type, id);
            if (dependency == null) {
                if (dependencyParentContext != null) {
                    dependency = dependencyParentContext.get(type, o);
                }
            }

            if (dependency == null) {
                throw new DependencyUnesolvedException(id);
            }

            return dependency;
        }

        if (o instanceof Map) {
            Map<String, Object> properties = safe(o);

            String id = (String) properties.get("id");

            // merge parent properties
            String parent = (String) properties.get("parent");
            if (parent != null) {
                properties = merge(get(Properties.class, parent).properties, properties);
            }

            T dependency = dependencyFactory.create(properties, type, context);
            if (id != null) {
                set(type, id, dependency);

                // keep configuration for the parent references resolving
                set(Properties.class, id, new Properties(properties));
            }

            return dependency;
        }

        throw new IllegalArgumentException("Unsupported object type: " + o);
    }

    private class Properties {

        private Map<String, Object> properties;

        private Properties(Map<String, Object> properties) {
            this.properties = properties;
        }

    }
}
