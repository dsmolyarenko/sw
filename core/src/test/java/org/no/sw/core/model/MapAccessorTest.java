package org.no.sw.core.model;

import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;
import org.no.sw.core.util.MapAccessor;

public class MapAccessorTest {

    @Test
    public void testDefault() {
        MapAccessor accessor = MapAccessor.of(new TreeMap<>());

        accessor.setProperty("test", "v0");
        Assert.assertEquals("v0", accessor.getProperty("test"));

        accessor.addProperty("test", "v0");
        Assert.assertEquals("v0", accessor.getProperty("test"));

        accessor.addProperty("test", "v1");
        Assert.assertEquals("v0,v1", accessor.getProperty("test"));

        accessor.setProperty("test", "v0,v1,v2");
        accessor.delProperty("test", "v0");
        Assert.assertEquals("v1,v2", accessor.getProperty("test"));

        accessor.setProperty("test", "v0,v1,v2");
        accessor.delProperty("test", "v1");
        Assert.assertEquals("v0,v2", accessor.getProperty("test"));

        accessor.setProperty("test", "v0,v1,v2");
        accessor.delProperty("test", "v2");
        Assert.assertEquals("v0,v1", accessor.getProperty("test"));
    }

    @Test
    public void testPropertySets() {
        MapAccessor accessor = MapAccessor.of(new TreeMap<>());

        accessor.setProperty("test", "v0");
        Assert.assertEquals(1, accessor.getPropertySize("test"));

        accessor.setProperty("test", "v0,v2");
        Assert.assertEquals(2, accessor.getPropertySize("test"));
    }
}
