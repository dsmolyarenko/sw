package org.no.sw.core.service;

import java.util.List;

import org.no.sw.core.model.SWBase;

public interface ContentService {

    SWBase get(String id);

    List<SWBase> getAll();

    SWBase create(String id);

    SWBase update(SWBase t);
}
