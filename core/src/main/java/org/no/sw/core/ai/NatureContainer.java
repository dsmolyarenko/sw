package org.no.sw.core.ai;

import java.util.Map;

import org.no.sw.core.util.Maps;
import org.springframework.stereotype.Component;

@Component
public class NatureContainer extends Nature {

    @Override
    protected Map<String, String> getTypeProperties() {
        return Maps.<String, String>of()
                .put("content")
                .put("capacityWeight")
                .put("capacitySize")
                .build();
    }
    
    
}
