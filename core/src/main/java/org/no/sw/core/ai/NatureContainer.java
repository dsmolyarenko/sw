package org.no.sw.core.ai;

import java.util.Set;

import org.no.sw.core.util.Maps;
import org.springframework.stereotype.Component;

@Component
public class NatureContainer extends Nature {

    @Override
    protected Set<String> getTypeProperties() {
        return Maps.<String, String>of()
                .put("capacityWeight")
                .put("capacitySize")
                .build().keySet();
    }

}
