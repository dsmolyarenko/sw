package org.no.sw.prototype.context;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.no.generator.configuration.util.ObjectUtils;

public abstract class DependencyContextBase implements DependencyContext {

    private Map<Class<?>, Map<String, Object>> dependencies;

    protected <T> void set(Class<T> type, String id, Object o) {
        if (dependencies == null) {
            dependencies = new LinkedHashMap<>();
        }
        Map<String, Object> map = dependencies.get(type);
        if (map == null) {
            dependencies.put(type, map = new TreeMap<>());
        }
        map.put(id, o);
    }

    protected <T> T get(Class<T> type, String id) {
        if (dependencies == null) {
            return null;
        }
        Map<String, Object> map = dependencies.get(type);
        if (map == null) {
            return null;
        }
        return ObjectUtils.safe(map.get(id));
    }

}