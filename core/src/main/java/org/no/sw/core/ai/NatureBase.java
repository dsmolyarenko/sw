package org.no.sw.core.ai;

import java.util.Map;

import org.no.sw.core.util.Maps;
import org.springframework.stereotype.Component;

@Component
public class NatureBase extends Nature {

    @Override
    protected Map<String, String> getTypeProperties() {
        return Maps.of()
                .put("name")
                .put("weight")
                .put("size")
                .build();
    }
}
