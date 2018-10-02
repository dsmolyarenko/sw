package org.no.sw.prototype.service;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.no.sw.core.service.ContentService;
import org.no.sw.core.service.ContentServiceDirectory;
import org.no.sw.core.service.IdentyService;
import org.no.sw.core.service.IdentyServiceDefault;
import org.no.sw.core.util.MapAccessor;


public class ResourceServiceDefaultTest {

    private IdentyService identyService;

    private ContentService contentService;

    private PrototypeService prototypeService;

    private ResourceService resourceService;

    @BeforeEach
    public void setup() {
        this.identyService = new IdentyServiceDefault();
        this.prototypeService = new ProtypeServiceDefault();
        this.contentService = new ContentServiceDirectory();

        ResourceServiceDefault resourceServiceDefault = new ResourceServiceDefault();
        resourceServiceDefault.setIdentyService(identyService);
        resourceServiceDefault.setContentService(contentService);
        resourceServiceDefault.setPrototypeService(prototypeService);
        this.resourceService = resourceServiceDefault;
    }

    @Test
    public void testDefaults() {

        MapAccessor prototype1 = MapAccessor.of(new TreeMap<>());
        prototype1.setProperty("id", "p_prototype");
        prototype1.setProperty("type", "type");
        prototype1.setProperty("type:property", "value");
        prototypeService.collect(prototype1.getMap());

        MapAccessor o1 = MapAccessor.of(new TreeMap<>());
        o1.setProperty("id", "o1");
        o1.setProperty("prototype", "p_prototype");
        o1.setProperty("b:parent", "o0");

        MapAccessor o2 = MapAccessor.of(new TreeMap<>());
        o2.setProperty("id", "o2");
        o2.setProperty("prototype", "p_prototype");
        o2.setProperty("b:parent", "o0");

        MapAccessor o0 = MapAccessor.of(new TreeMap<>());
        o0.setProperty("id", "o0");
        o0.setProperty("prototype", "p_prototype");

        Map<String, String> result = resourceService.load(Arrays.asList(o1.getMap(), o2.getMap(), o0.getMap()));

        String id = result.get("o0");
        Assertions.assertNotNull(id);
        System.out.println(contentService.getAll());
        Assertions.assertNotNull(contentService.get(id));
    }
}
