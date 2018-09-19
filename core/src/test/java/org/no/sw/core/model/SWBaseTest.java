package org.no.sw.core.model;

import org.junit.Assert;
import org.junit.Test;

public class SWBaseTest {

    @Test
    public void testDefault() {
        SWBase swBase = new SWBase();

        swBase.setProperty("test", "v0");
        Assert.assertEquals("v0", swBase.getProperty("test"));

        swBase.addProperty("test", "v0");
        Assert.assertEquals("v0", swBase.getProperty("test"));

        swBase.addProperty("test", "v1");
        Assert.assertEquals("v0,v1", swBase.getProperty("test"));

        swBase.setProperty("test", "v0,v1,v2");
        swBase.delProperty("test", "v0");
        Assert.assertEquals("v1,v2", swBase.getProperty("test"));

        swBase.setProperty("test", "v0,v1,v2");
        swBase.delProperty("test", "v1");
        Assert.assertEquals("v0,v2", swBase.getProperty("test"));

        swBase.setProperty("test", "v0,v1,v2");
        swBase.delProperty("test", "v2");
        Assert.assertEquals("v0,v1", swBase.getProperty("test"));
    }
}
