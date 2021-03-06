package org.no.sw.prototype.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.no.sw.core.model.Source;
import org.no.sw.core.model.Target;
import org.no.sw.core.service.ContentService;
import org.no.sw.core.service.IdentyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class ResourceServiceDefault implements ResourceService {

    private IdentyService identyService;

    @Autowired
    public void setIdentyService(IdentyService identyService) {
        this.identyService = identyService;
    }

    private ContentService contentService;

    @Autowired
    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    private PrototypeService prototypeService;

    @Autowired
    public void setPrototypeService(PrototypeService prototypeService) {
        this.prototypeService = prototypeService;
    }

    @Override
    public Map<String, String> load(List<Map<String, String>> objects) {
        Map<String, String> result = new HashMap<>();

        // create real id
        objects.forEach(v -> {
            String id = v.get("id");
            Assert.hasText(id, () -> "'id' is not defined for object: " + v);
            result.put(id, identyService.getId());
        });

        // resolve dependencies
        objects.forEach(v -> {
            String prototypeId = v.get("prototype");
            Assert.hasText(prototypeId, () -> "'prototype' is not defined for object: " + v);
            Source prototype = prototypeService.getAll().get(prototypeId);
            Assert.notNull(prototype, () -> "prototype was not defined: " + prototypeId);

            Target object = Target.of();

            // add prototype properties
            object.addPropertiesExcluding(prototype, "id");

            // add object properties
            for (String key : v.keySet()) {
                String value = v.get(key);
                if (result.containsKey(value)) {
                    value = result.get(value);
                }
                object.setPropertyValue(key, value);
            }

            // store object
            contentService.save(object);
        });

        return result;
    }

}
