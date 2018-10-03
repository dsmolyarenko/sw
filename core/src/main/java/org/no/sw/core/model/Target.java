package org.no.sw.core.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import org.no.sw.core.util.PropertiesUtils;

public interface Target extends Source {

    static Target of(Properties properties) {
        if (properties == null) {
            return null;
        }
        return properties::getProperties;
    }

    static Target of() {
        Map<String, String> map = new TreeMap<>();
        return new Target() {
            @Override
            public Map<String, String> getProperties() {
                return map;
            }

            @Override
            public String toString() {
                return PropertiesUtils.toString(this);
            }
        };
    }

    default boolean addPropertyValue(String name, String value) {
        String v = getProperties().get(name);
        if (v == null || v.isEmpty()) {
            getProperties().put(name, value);
            return true;
        }
        if (hasPropertyValue(name, value)) {
            return false;
        }
        setPropertyValue(name, v + "," + value);
        return true;
    }

    default boolean setPropertyValue(String name, String value) {
        return !Objects.equals(getProperties().put(name, value), value);
    }

    default boolean addPropertiesExcluding(Properties properties, String... excludes) {
        Set<String> excludedKeys = new HashSet<>(Arrays.asList(excludes));

        boolean changed = false;
        for (String k : properties.getProperties().keySet()) {
            if (!excludedKeys.contains(k)) {
                changed |= addPropertyValue(k, properties.getProperties().get(k));
            }
        }
        return changed;
    }

    default boolean setPropertiesExcluding(Properties properties, String... excludes) {
        Set<String> excludedKeys = new HashSet<>(Arrays.asList(excludes));

        boolean changed = false;
        for (String k : properties.getProperties().keySet()) {
            if (!excludedKeys.contains(k)) {
                changed |= setPropertyValue(k, properties.getProperties().get(k));
            }
        }
        return changed;
    }

    default boolean delPropertyValue(String name, String value) {
        String v = getProperties().get(name);
        if (v == null) {
            return false;
        }
        int s = 0;
        while (s < v.length()) {
            int e = v.indexOf(',', s);
            if (e == -1) {
                e = v.length();
            }
            if (v.regionMatches(s, value, 0, e - s)) {
                if (s == 0) {
                    if (e == v.length()) {
                        v = null;
                    } else {
                        v = v.substring(e + 1);
                    }
                } else {
                    if (e == v.length()) {
                        v = v.substring(0, s - 1);
                    } else {
                        v = v.substring(0, s - 1) + v.substring(e);
                    }
                }
                setPropertyValue(name, v);
                return true;
            }
            s = e + 1;
        }
        return false;
    }

}
