package org.no.sw.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MapAccessorTest {

    @Test
    public void testDefault() {
        MapAccessor accessor = MapAccessor.of(new TreeMap<>());

        accessor.setProperty("test", "v0");
        Assertions.assertEquals("v0", accessor.getProperty("test"));

        accessor.addProperty("test", "v0");
        Assertions.assertEquals("v0", accessor.getProperty("test"));

        accessor.addProperty("test", "v1");
        Assertions.assertEquals("v0,v1", accessor.getProperty("test"));

        accessor.setProperty("test", "v0,v1,v2");
        accessor.delProperty("test", "v0");
        Assertions.assertEquals("v1,v2", accessor.getProperty("test"));

        accessor.setProperty("test", "v0,v1,v2");
        accessor.delProperty("test", "v1");
        Assertions.assertEquals("v0,v2", accessor.getProperty("test"));

        accessor.setProperty("test", "v0,v1,v2");
        accessor.delProperty("test", "v2");
        Assertions.assertEquals("v0,v1", accessor.getProperty("test"));
    }

    @Test
    public void testPropertySets() {
        MapAccessor accessor = MapAccessor.of(new TreeMap<>());

        accessor.setProperty("test", "");
        Assertions.assertEquals(0, accessor.getPropertySize("test"));

        accessor.setProperty("test", "v0");
        Assertions.assertEquals(1, accessor.getPropertySize("test"));

        accessor.setProperty("test", "v0,v1");
        Assertions.assertEquals(2, accessor.getPropertySize("test"));

        accessor.setProperty("test", "v0,v1,v2");
        Assertions.assertEquals(3, accessor.getPropertySize("test"));
    }

    @Test
    public void testPropertySetNavigation() {
        MapAccessor accessor = MapAccessor.of(new TreeMap<>());

        List<String> result = new ArrayList<>();

        result.clear();
        accessor.setProperty("test", "");
        accessor.getPropertyIterator("test", v -> result.add(v));
        Assertions.assertEquals(0, result.size());

        result.clear();
        accessor.setProperty("test", "v0");
        accessor.getPropertyIterator("test", v -> result.add(v));
        Assertions.assertEquals(1, result.size());

        result.clear();
        accessor.setProperty("test", "v0,v1");
        accessor.getPropertyIterator("test", v -> result.add(v));
        Assertions.assertEquals(Arrays.asList("v0", "v1"), result);

        result.clear();
        accessor.setProperty("test", "v0,v1,v2");
        accessor.getPropertyIterator("test", v -> result.add(v));
        Assertions.assertEquals(Arrays.asList("v0", "v1", "v2"), result);

    }

    @Test
    public void testPropertyStack() {
        MapAccessor accessor = MapAccessor.of(new TreeMap<>());

        accessor.setProperty("test", "v0,v1,v2");
        Assertions.assertEquals("v0,v1,v2", accessor.getProperty("test"));

        accessor.addProperty("test", "v0");
        accessor.addProperty("test", "v1");
        accessor.addProperty("test", "v2");
        Assertions.assertEquals("v0,v1,v2", accessor.getProperty("test"));

        Assertions.assertEquals("v2", accessor.popProperty("test"));
        Assertions.assertEquals("v0,v1", accessor.getProperty("test"));

        Assertions.assertEquals("v1", accessor.popProperty("test"));
        Assertions.assertEquals("v0", accessor.getProperty("test"));

        Assertions.assertEquals("v0", accessor.popProperty("test"));
        Assertions.assertEquals(null, accessor.getProperty("test"));
    }
}
