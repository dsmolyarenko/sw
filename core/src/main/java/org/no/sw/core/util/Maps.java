package org.no.sw.core.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class Maps {

    public static <K, V> Builder<K, V> of() {
        return new Builder<K, V>();
    }

    public static class Builder<K, V> {

        private Map<K, V> map;

        public Builder() {
            map = new LinkedHashMap<>();
        }

        public Builder<K, V> put(K k, V v) {
            map.put(k, v);
            return this;
        }

        public Builder<K, V> put(K k) {
            return put(k, null);
        }

        public Map<K, V> build() {
            return map;
        }
    }
}
