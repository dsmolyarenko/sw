package org.no.sw.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.no.sw.core.model.Target;

public class PropertiesTest {

    @Test
    public void testDefault() {
        Target accessor = Target.of();

        Assertions.assertFalse(accessor.setPropertyValue("test", null));
        Assertions.assertEquals(null, accessor.getPropertyValue("test"));

        Assertions.assertTrue(accessor.addPropertyValue("test", "v0"));
        Assertions.assertEquals("v0", accessor.getPropertyValue("test"));

        Assertions.assertTrue(accessor.addPropertyValue("test", "v1"));
        Assertions.assertEquals("v0,v1", accessor.getPropertyValue("test"));

        Assertions.assertTrue(accessor.setPropertyValue("test", "v0,v1,v2"));
        Assertions.assertTrue(accessor.delPropertyValue("test", "v0"));
        Assertions.assertEquals("v1,v2", accessor.getPropertyValue("test"));

        Assertions.assertTrue(accessor.setPropertyValue("test", "v0,v1,v2"));
        Assertions.assertTrue(accessor.delPropertyValue("test", "v1"));
        Assertions.assertEquals("v0,v2", accessor.getPropertyValue("test"));

        Assertions.assertTrue(accessor.setPropertyValue("test", "v0,v1,v2"));
        Assertions.assertTrue(accessor.delPropertyValue("test", "v2"));
        Assertions.assertEquals("v0,v1", accessor.getPropertyValue("test"));

        Assertions.assertTrue(accessor.setPropertyValue("test", "v0"));
        Assertions.assertTrue(accessor.delPropertyValue("test", "v0"));
        Assertions.assertEquals(null, accessor.getPropertyValue("test"));

        accessor.setPropertyValue("test", "ul");
        Assertions.assertFalse(accessor.addPropertyValue("test", "ul"));
        Assertions.assertFalse(accessor.delPropertyValue("test", "v1"));

        accessor.setPropertyValue("test", null);
        Assertions.assertFalse(accessor.delPropertyValue("test", "v1"));
    }

    @Test
    public void testPropertySetNavigation() {
        Target accessor = Target.of();

        List<String> result = new ArrayList<>();

        result.clear();
        accessor.setPropertyValue("test", "");
        accessor.getPropertyValues("test").forEach(v -> result.add(v));
        Assertions.assertEquals(0, result.size());

        result.clear();
        accessor.setPropertyValue("test", "v0");
        accessor.getPropertyValues("test").forEach(v -> result.add(v));
        Assertions.assertEquals(1, result.size());

        result.clear();
        accessor.setPropertyValue("test", "v0,v1");
        accessor.getPropertyValues("test").forEach(v -> result.add(v));
        Assertions.assertEquals(Arrays.asList("v0", "v1"), result);

        result.clear();
        accessor.setPropertyValue("test", "v0,v1,v2");
        accessor.getPropertyValues("test").forEach(v -> result.add(v));
        Assertions.assertEquals(Arrays.asList("v0", "v1", "v2"), result);
    }
}
