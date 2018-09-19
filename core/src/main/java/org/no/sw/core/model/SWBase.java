package org.no.sw.core.model;

import java.util.HashMap;
import java.util.Map;

public class SWBase {

    private String id;

    private Map<String, String> properties;

    public SWBase() {
        super();
    }

    public SWBase(String id) {
        super();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getProperties() {
        if (properties == null) {
            properties = new HashMap<>();
        }
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getProperty(String name) {
        return getProperties().get(name);
    }

    public String setProperty(String name, String value) {
        return getProperties().put(name, value);
    }

    public String addProperty(String name, String value) {
        return getProperties().compute(name, (k, v) -> (v == null) ? value : (v.contains(value) ? v : v + "," + value));
    }

    public String delProperty(String name, String value) {
        return getProperties().compute(name, (k, v) -> {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append("@{");
        getProperties().forEach((k, v) -> {
            sb.append(k);
            sb.append(":");
            sb.append(v);
            sb.append(",");
        });
        if (!getProperties().isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("}");
        return sb.toString();
    }
}
