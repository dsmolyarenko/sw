package org.no.sw.prototype.task;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.no.sw.prototype.service.PrototypeService;
import org.no.sw.prototype.service.PrototypeServiceDefault;

public class PrototypeLoaderTest {

    private PrototypeService prototypeService;

    private PrototypeLoader prototypeLoader;

    @BeforeEach
    public void setup() {
        this.prototypeService = new PrototypeServiceDefault();
        this.prototypeLoader = new PrototypeLoader();
        prototypeLoader.setPrototypeService(prototypeService);
    }

    @Test
    public void testDefault() throws IOException {
        prototypeLoader.load();
        prototypeService.getAll().get("test");
    }
}