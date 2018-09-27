package org.no.sw.core.util;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;

import org.springframework.util.StringUtils;

public class MapAccessor {

    private static final char PREFIX_DELIMITER = ':';

    private final Map<String, String> map;

    private final String prefix;

    public static MapAccessor of(Map<String, String> map) {
        return new MapAccessor(map);
    }

    public static MapAccessor of(Map<String, String> map, String prefix) {
        return new MapAccessor(map, prefix);
    }

    private MapAccessor(Map<String, String> map) {
        this(map, null);
    }

    public MapAccessor(Map<String, String> map, String prefix) {
        this.prefix = prefix;
        if (map instanceof NavigableMap) {
            NavigableMap navigableMap =  (NavigableMap<String, String>) map;
            if (prefix != null) {
                this.map = navigableMap.subMap(prefix + (PREFIX_DELIMITER), prefix + (char) (PREFIX_DELIMITER + 1));
            } else {
                this.map = navigableMap;
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public String getProperty(String name) {
        return map.get(getKey(name));
    }

    public int getPropertySize(String name) {
        String v = map.get(getKey(name));
        if (StringUtils.isEmpty(v)) {
            return 0;
        }
        int count = 1;
        int index = v.indexOf(',');
        while (index != -1) {
            index = v.indexOf(',', ++index);
            count++;
        }
        return count;
    }

    public <T extends Throwable> int getPropertyIterator(String name, Consumer<String, T> c) throws T {
        String v = map.get(getKey(name));
        if (StringUtils.isEmpty(v)) {
            return 0;
        }
        int count = 1;

        int s = 0;
        int e = v.indexOf(',');
        while (e != -1) {
            c.accept(v.substring(s, e));
            s = e + 1;
            e = v.indexOf(',', s);
            count++;
        }
        c.accept(v.substring(s));
        return count;
    }

    public boolean setProperty(String name, String value) {
        return !Objects.equals(value, map.put(getKey(name), value != null ? value : ""));
    }

    public void setProperties(Map<String, String> properties) {
        properties.forEach(this::setProperty);
    }

    public String addProperty(String name, String value) {
        return map.compute(getKey(name), (k, v) -> StringUtils.isEmpty(v) ? value : (v.contains(value) ? v : v + "," + value));
    }

    public void addProperties(Map<String, String> properties) {
        properties.forEach(this::addProperty);
    }

    public String delProperty(String name, String value) {
        return map.compute(getKey(name), (k, v) -> {
            if (v == null) {
                return null;
            }
            int index = v.indexOf(value);
            if (index == -1) {
                return v;
            }
            String start;
            if (index > 0) {
                start = v.substring(0, index - 1 /* delimiter */);
            } else {
                start = "";
            }
            String end;
            if (index + value.length() < v.length() - 1) {
                end = v.substring(index + value.length() + 1 /* delimiter */);
            } else {
                end = "";
            }
            String delimiter;
            if (!start.isEmpty() && !end.isEmpty()) {
                delimiter = ",";
            } else {
                delimiter = "";
            }
            String full = start + delimiter + end;
            if (full.isEmpty()) {
                return null;
            }
            return full;
        });
    }

    public boolean hasProperty(String name) {
        String v = map.get(getKey(name));
        if (v == null) {
            return false;
        }
        return true;
    }

    public boolean hasProperty(String name, String value) {
        String v = map.get(getKey(name));
        if (v == null) {
            return false;
        }
        int index = v.indexOf(value);
        if (index == -1) {
            return false;
        }
        return true;
    }

    public void clear() {
        map.clear();
    }

    private String getKey(String name) {
        return prefix != null ? prefix + (PREFIX_DELIMITER) + name : name;
    }

    public interface Consumer<E, T extends Throwable> {
        void accept(E e) throws T;
    }

}
