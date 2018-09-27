package org.no.sw.prototype.context;

import java.util.Map;

public interface DependencyFactory {

    <T> T create(Map<String, Object> properties, Class<T> type, DependencyContext context);

}