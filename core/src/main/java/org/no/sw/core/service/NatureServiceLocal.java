package org.no.sw.core.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.no.sw.core.ai.Nature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NatureServiceLocal implements NatureService {

    private Map<String, Nature> natures;

    public NatureServiceLocal() {
        natures = new ConcurrentHashMap<>();
    }

    @Override
    public void register(Nature nature, String type) {
        Nature old = natures.put(type, nature);
        if (old != null) {
            log.info("Added type {} nature {}, replaced {}", type, nature, old);
        } else {
            log.info("Added type {} nature {}", type, nature);
        }
    }

    @Override
    public Nature getNature(String type) {
        return natures.get(type);
    }

    private final Logger log = LoggerFactory.getLogger(getClass());
}
