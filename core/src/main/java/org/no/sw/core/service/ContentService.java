package org.no.sw.core.service;

import java.util.List;

import org.no.sw.core.model.SWBase;
import org.no.sw.core.util.MapAccessor;

public interface ContentService {

    MapAccessor getProperties(String id);

    SWBase get(String id);

    List<SWBase> getAll();

    SWBase create(String id);

    void update(SWBase... bases);
}
