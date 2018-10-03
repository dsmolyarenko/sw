package org.no.sw.core.ai;

import java.util.Set;

import org.no.sw.core.model.Source;
import org.no.sw.core.model.Target;
import org.no.sw.core.util.Maps;
import org.springframework.stereotype.Component;

@Component
public class NatureBase extends Nature {

    @Override
    protected Set<String> getTypeProperties() {
        return Maps.<String, String>of()
                .put("name")
                .put("description")
                .put("weight")
                .put("size")
                .put("parent")
                .build().keySet();
    }

    public static boolean setParent(Source source, Source newParent) {
        return Target.of(source).setPropertyValue("base:parent", newParent.getId());
    }
}
