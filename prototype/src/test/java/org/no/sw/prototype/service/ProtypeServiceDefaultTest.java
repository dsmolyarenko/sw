package org.no.sw.prototype.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.no.sw.core.model.Source;
import org.no.sw.core.model.Target;
import org.no.sw.prototype.service.PrototypeService.DependencyUnesolvedException;


public class ProtypeServiceDefaultTest {

    @Test
    public void testDefaults() {
        PrototypeServiceDefault protypeServiceDefault = new PrototypeServiceDefault();

        Target dep1 = Target.of();
        dep1.setPropertyValue("id", "dep1");
        dep1.setPropertyValue("type", "t1");
        dep1.setPropertyValue("t1:p1", "t1p1v");
        dep1.setPropertyValue("t1:p2", "t1p2v");

        Target dep2 = Target.of();
        dep2.setPropertyValue("id", "dep2");
        dep2.setPropertyValue("type", "t2");
        dep2.setPropertyValue("t2:p1", "t2p1v");
        dep2.setPropertyValue("t2:p2", "t2p2v");

        Target resolvable = Target.of();
        resolvable.setPropertyValue("id", "resolvable");
        resolvable.setPropertyValue("parent", "dep1,dep2");
        resolvable.setPropertyValue("t2:p1", "t2p1v");
        resolvable.setPropertyValue("t2:p2", "t2p2v");

        protypeServiceDefault.collect(dep1.getProperties());
        protypeServiceDefault.collect(dep2.getProperties());
        protypeServiceDefault.collect(resolvable.getProperties());

        Source r = protypeServiceDefault.getAll().get("resolvable");

        Assertions.assertEquals("t1,t2", r.getPropertyValue("type"));
        Assertions.assertEquals("t1p1v", r.getPropertyValue("t1:p1"));
        Assertions.assertEquals("t1p2v", r.getPropertyValue("t1:p2"));
        Assertions.assertEquals("t2p1v", r.getPropertyValue("t2:p1"));
        Assertions.assertEquals("t2p2v", r.getPropertyValue("t2:p2"));
    }

    @Test
    public void testDeferedResolved() {
        PrototypeServiceDefault protypeServiceDefault = new PrototypeServiceDefault();

        Target dep1 = Target.of();
        dep1.setPropertyValue("id", "dep1");
        dep1.setPropertyValue("type", "t1");

        Target dep2 = Target.of();
        dep2.setPropertyValue("id", "dep2");
        dep2.setPropertyValue("type", "t2");

        Target resolvable = Target.of();
        resolvable.setPropertyValue("id", "resolvable");
        resolvable.setPropertyValue("parent", "dep1,dep2");

        protypeServiceDefault.collect(resolvable.getProperties());
        protypeServiceDefault.collect(dep1.getProperties());
        protypeServiceDefault.collect(dep2.getProperties());

        Source r = protypeServiceDefault.getAll().get("resolvable");

        Assertions.assertEquals("t1,t2", r.getPropertyValue("type"));
    }

    @Test
    public void testDeferedResolvedWorst() {
        PrototypeServiceDefault protypeServiceDefault = new PrototypeServiceDefault();

        Target dep1 = Target.of();
        dep1.setPropertyValue("id", "dep1");
        dep1.setPropertyValue("type", "t1");

        Target dep2 = Target.of();
        dep2.setPropertyValue("id", "dep2");
        dep2.setPropertyValue("type", "t2");

        Target dep3 = Target.of();
        dep3.setPropertyValue("id", "dep3");
        dep3.setPropertyValue("type", "t3");

        Target intermediate = Target.of();
        intermediate.setPropertyValue("id", "intermediate");
        intermediate.setPropertyValue("parent", "dep1,dep2,dep3");

        Target resolvable = Target.of();
        resolvable.setPropertyValue("id", "resolvable");
        resolvable.setPropertyValue("parent", "intermediate");

        protypeServiceDefault.collect(resolvable.getProperties());
        protypeServiceDefault.collect(intermediate.getProperties());
        protypeServiceDefault.collect(dep1.getProperties());
        protypeServiceDefault.collect(dep2.getProperties());
        protypeServiceDefault.collect(dep3.getProperties());

        Source r = protypeServiceDefault.getAll().get("resolvable");

        Assertions.assertEquals("t1,t2,t3", r.getPropertyValue("type"));
    }

    @Test
    public void testDeferedUnresolved() {
        PrototypeServiceDefault protypeServiceDefault = new PrototypeServiceDefault();

        Target dep1 = Target.of();
        dep1.setPropertyValue("id", "dep1");
        dep1.setPropertyValue("type", "t1");
        dep1.setPropertyValue("parent", "dep2");

        Target dep2 = Target.of();
        dep2.setPropertyValue("id", "dep2");
        dep2.setPropertyValue("type", "t2");
        dep2.setPropertyValue("parent", "dep3");

        Target dep3 = Target.of();
        dep3.setPropertyValue("id", "dep3");
        dep3.setPropertyValue("type", "t3");
        dep3.setPropertyValue("parent", "dep1");

        protypeServiceDefault.collect(dep1.getProperties());
        protypeServiceDefault.collect(dep2.getProperties());
        protypeServiceDefault.collect(dep3.getProperties());

        try {
            protypeServiceDefault.getAll().get("dep1");
            Assertions.fail("Circular dependency was not detected");
        } catch (DependencyUnesolvedException e) {
            Assertions.assertEquals(3, e.getIds().size());
        }
    }
}
