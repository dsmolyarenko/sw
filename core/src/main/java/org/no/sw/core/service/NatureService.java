package org.no.sw.core.service;

import org.no.sw.core.ai.Nature;

public interface NatureService {

    void register(Nature nature, String type);

    Nature getNature(String type);

}
