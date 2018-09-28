package org.no.sw.prototype.service;

import java.util.TreeMap;

import org.junit.Test;
import org.no.sw.core.util.MapAccessor;

import junit.framework.Assert;

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
        resolvable.addProperty("parent", "t1,t2");
        resolvable.addProperty("t2:p1", "t2p1v");
        resolvable.addProperty("t2:p2", "t2p2v");

        protypeServiceDefault.collect(dep1);
        protypeServiceDefault.collect(dep2);
        protypeServiceDefault.collect(resolvable);

        resolvable = protypeServiceDefault.getAll().get("resolvable");

        Assert.assertEquals("t1,t2", resolvable.getProperty("type"));
        Assert.assertEquals("t1p1v", resolvable.getProperty("t1:p1"));
        Assert.assertEquals("t1p2v", resolvable.getProperty("t1:p2"));
        Assert.assertEquals("t2p1v", resolvable.getProperty("t2:p1"));
        Assert.assertEquals("t2p2v", resolvable.getProperty("t2:p2"));
    }
}
