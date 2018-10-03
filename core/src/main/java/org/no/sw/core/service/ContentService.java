package org.no.sw.core.service;

import java.util.Collection;

import org.no.sw.core.model.Source;

public interface ContentService {

    Source getById(String id);

    Collection<Source> getAll(int limit);

    Collection<Source> find(String field, String value, int limit);

    void save(Source... sources);

    void save(Collection<Source> sources);
}
