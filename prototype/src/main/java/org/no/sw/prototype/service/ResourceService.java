package org.no.sw.prototype.service;

import java.util.List;
import java.util.Map;

public interface ResourceService {

    /**
     * Links objects assigning real storage ids.
     * @param objects the list of linking objects
     * @return the map of object alias to object id
     */
    Map<String, String> load(List<Map<String, String>> objects);

}
