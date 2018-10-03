package org.no.sw.core.model;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public interface Source extends Properties {

    @Override
    Map<String, String> getProperties();

    static Source of(Map<String, String> properties) {
        return () -> properties;
    }

    default String getId() {
        return getProperties().get("id");
    }

    default String getPropertyValue(String name) {
        return getProperties().get(name);
    }

    default boolean hasPropertyValue(String name, String value) {
        String v = getProperties().get(name);
        if (v == null) {
            getProperties().put(name, value);
            return true;
        }
        int s = 0;
        int e = v.indexOf(',');
        while (e != -1) {
            if (v.regionMatches(s, value, 0, e - s)) {
                return true;
            }
            s = e + 1;
            e = v.indexOf(',', s);
        }
        if (v.regionMatches(s, value, 0, v.length() - s)) {
            return true;
        }
        return false;
    }

    /**
     * Creates {@link Iterable} over string array represented by comma separated string
     */
    default Iterable<String> getPropertyValues(String name) {
        String value = getPropertyValue(name);
        return () -> new Iterator<String>() {

            int s;
            int e = -1;

            {
                tryNext();
            }

            private void tryNext() {
                s = e + 1;
                if (value == null) {
                    return;
                }
                e = value.indexOf(',', s);
                if (e == -1) {
                    e = value.length();
                }
            }

            @Override
            public boolean hasNext() {
                return s < e;
            }

            @Override
            public String next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                String result = value.substring(s, e);
                tryNext();
                return result;
            }
        };
    }
}
