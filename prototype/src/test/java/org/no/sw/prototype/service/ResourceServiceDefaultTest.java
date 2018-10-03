package org.no.sw.prototype.service;

import java.util.Arrays;
import java.util.Map;

import org.apache.lucene.store.RAMDirectory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.no.sw.core.model.Target;
import org.no.sw.core.service.ContentService;
import org.no.sw.core.service.ContentServiceDirectory;
import org.no.sw.core.service.IdentyService;
import org.no.sw.core.service.IdentyServiceDefault;



public class ResourceServiceDefaultTest {

    private IdentyService identyService;

    private ContentService contentService;

    private PrototypeService prototypeService;

    private ResourceService resourceService;

    @BeforeEach
    public void setup() {
        this.identyService = new IdentyServiceDefault();
        this.prototypeService = new PrototypeServiceDefault();
        this.contentService = new ContentServiceDirectory(new RAMDirectory());

        ResourceServiceDefault resourceServiceDefault = new ResourceServiceDefault();
        resourceServiceDefault.setIdentyService(identyService);
        resourceServiceDefault.setContentService(contentService);
        resourceServiceDefault.setPrototypeService(prototypeService);
        this.resourceService = resourceServiceDefault;
    }

    @Test
    public void testDefaults() {

        Target prototype1 = Target.of();
        prototype1.setPropertyValue("id", "p_prototype");
        prototype1.setPropertyValue("type", "type");
        prototype1.setPropertyValue("type:property", "value");
        prototypeService.collect(prototype1.getProperties());

        Target o1 = Target.of();
        o1.setPropertyValue("id", "o1");
        o1.setPropertyValue("prototype", "p_prototype");
        o1.setPropertyValue("b:parent", "o0");

        Target o2 = Target.of();
        o2.setPropertyValue("id", "o2");
        o2.setPropertyValue("prototype", "p_prototype");
        o2.setPropertyValue("b:parent", "o0");

        Target o0 = Target.of();
        o0.setPropertyValue("id", "o0");
        o0.setPropertyValue("prototype", "p_prototype");

        Map<String, String> result = resourceService.load(Arrays.asList(o1.getProperties(), o2.getProperties(), o0.getProperties()));

        String id = result.get("o0");
        Assertions.assertNotNull(id);
        Assertions.assertNotNull(contentService.getById(id));
    }
}
