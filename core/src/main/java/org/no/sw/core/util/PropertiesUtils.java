package org.no.sw.core.util;

import org.no.sw.core.model.Properties;

public class PropertiesUtils {

    public static String toString(Properties properties) {
        StringBuilder sb = new StringBuilder();
        sb.append("@{");
        sb.append("\n");
        properties.getProperties().forEach((k, v) -> {
            sb.append("  ");
            sb.append(k);
            sb.append("=");
            sb.append(v);
            sb.append("\n");
        });
        sb.append("}");
        return sb.toString();
    }
}
