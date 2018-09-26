package org.no.sw.core.model;

import java.util.Map;
import java.util.TreeMap;

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
            properties = new TreeMap<>();
        }
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append("@{");
        sb.append("\n");
        getProperties().forEach((k, v) -> {
            sb.append("  ");
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
