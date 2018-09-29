package org.no.sw.prototype.service;

import java.util.TreeMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.no.sw.core.util.MapAccessor;
import org.no.sw.prototype.service.PrototypeService.DependencyUnesolvedException;


public class ProtypeServiceDefaultTest {

    @Test
    public void testDefaults() {
        ProtypeServiceDefault protypeServiceDefault = new ProtypeServiceDefault();

        MapAccessor dep1 = MapAccessor.of(new TreeMap<>());
        dep1.addProperty("id", "dep1");
        dep1.addProperty("type", "t1");
        dep1.addProperty("t1:p1", "t1p1v");
        dep1.addProperty("t1:p2", "t1p2v");

        MapAccessor dep2 = MapAccessor.of(new TreeMap<>());
        dep2.addProperty("id", "dep2");
        dep2.addProperty("type", "t2");
        dep2.addProperty("t2:p1", "t2p1v");
        dep2.addProperty("t2:p2", "t2p2v");

        MapAccessor resolvable = MapAccessor.of(new TreeMap<>());
        resolvable.addProperty("id", "resolvable");
        resolvable.addProperty("parent", "dep1,dep2");
        resolvable.addProperty("t2:p1", "t2p1v");
        resolvable.addProperty("t2:p2", "t2p2v");

        protypeServiceDefault.collect(dep1.getMap());
        protypeServiceDefault.collect(dep2.getMap());
        protypeServiceDefault.collect(resolvable.getMap());

        resolvable = protypeServiceDefault.getAll().get("resolvable");

        Assertions.assertEquals("t1,t2", resolvable.getProperty("type"));
        Assertions.assertEquals("t1p1v", resolvable.getProperty("t1:p1"));
        Assertions.assertEquals("t1p2v", resolvable.getProperty("t1:p2"));
        Assertions.assertEquals("t2p1v", resolvable.getProperty("t2:p1"));
        Assertions.assertEquals("t2p2v", resolvable.getProperty("t2:p2"));
    }

    @Test
    public void testDeferedResolved() {
        ProtypeServiceDefault protypeServiceDefault = new ProtypeServiceDefault();

        MapAccessor dep1 = MapAccessor.of(new TreeMap<>());
        dep1.setProperty("id", "dep1");
        dep1.setProperty("type", "t1");

        MapAccessor dep2 = MapAccessor.of(new TreeMap<>());
        dep2.setProperty("id", "dep2");
        dep2.setProperty("type", "t2");

        MapAccessor resolvable = MapAccessor.of(new TreeMap<>());
        resolvable.setProperty("id", "resolvable");
        resolvable.setProperty("parent", "dep1,dep2");

        protypeServiceDefault.collect(resolvable.getMap());
        protypeServiceDefault.collect(dep1.getMap());
        protypeServiceDefault.collect(dep2.getMap());

        resolvable = protypeServiceDefault.getAll().get("resolvable");

        Assertions.assertEquals("t1,t2", resolvable.getProperty("type"));
    }

    @Test
    public void testDeferedResolvedWorst() {
        ProtypeServiceDefault protypeServiceDefault = new ProtypeServiceDefault();

        MapAccessor dep1 = MapAccessor.of(new TreeMap<>());
        dep1.setProperty("id", "dep1");
        dep1.setProperty("type", "t1");

        MapAccessor dep2 = MapAccessor.of(new TreeMap<>());
        dep2.setProperty("id", "dep2");
        dep2.setProperty("type", "t2");

        MapAccessor dep3 = MapAccessor.of(new TreeMap<>());
        dep3.setProperty("id", "dep3");
        dep3.setProperty("type", "t3");

        MapAccessor intermediate = MapAccessor.of(new TreeMap<>());
        intermediate.setProperty("id", "intermediate");
        intermediate.setProperty("parent", "dep1,dep2,dep3");

        MapAccessor resolvable = MapAccessor.of(new TreeMap<>());
        resolvable.setProperty("id", "resolvable");
        resolvable.setProperty("parent", "intermediate");

        protypeServiceDefault.collect(resolvable.getMap());
        protypeServiceDefault.collect(intermediate.getMap());
        protypeServiceDefault.collect(dep1.getMap());
        protypeServiceDefault.collect(dep2.getMap());
        protypeServiceDefault.collect(dep3.getMap());

        resolvable = protypeServiceDefault.getAll().get("resolvable");

        Assertions.assertEquals("t1,t2,t3", resolvable.getProperty("type"));
    }

    @Test
    public void testDeferedUnresolved() {
        ProtypeServiceDefault protypeServiceDefault = new ProtypeServiceDefault();

        MapAccessor dep1 = MapAccessor.of(new TreeMap<>());
        dep1.setProperty("id", "dep1");
        dep1.setProperty("type", "t1");
        dep1.setProperty("parent", "dep2");

        MapAccessor dep2 = MapAccessor.of(new TreeMap<>());
        dep2.setProperty("id", "dep2");
        dep2.setProperty("type", "t2");
        dep2.setProperty("parent", "dep3");

        MapAccessor dep3 = MapAccessor.of(new TreeMap<>());
        dep3.setProperty("id", "dep3");
        dep3.setProperty("type", "t3");
        dep3.setProperty("parent", "dep1");

        protypeServiceDefault.collect(dep1.getMap());
        protypeServiceDefault.collect(dep2.getMap());
        protypeServiceDefault.collect(dep3.getMap());

        try {
            protypeServiceDefault.getAll().get("dep1");
            Assertions.fail("Circular dependency was not detected");
        } catch (DependencyUnesolvedException e) {
            Assertions.assertEquals(3, e.getIds().size());
        }
    }
}
