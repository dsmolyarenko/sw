package org.no.sw.core.ai;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.no.sw.core.event.Event;
import org.no.sw.core.model.SWBase;
import org.no.sw.core.service.ContentService;
import org.no.sw.core.service.NatureService;
import org.no.sw.core.util.MapAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Nature {

    @Autowired
    protected NatureService natureService;

    @Autowired
    protected ContentService contentService;

    @PostConstruct
    public final void setup() {
        natureService.register(this, getType());
    }

    public boolean process(SWBase base, Event event) {
        return false;
    }

    public boolean add(SWBase base) {
        MapAccessor.of(base.getProperties()).addProperty("type", getType());
        MapAccessor accessor = MapAccessor.of(base.getProperties(), getType());
        accessor.setProperties(getTypeProperties());
        return true;
    }

    public boolean remove(SWBase base) {
        MapAccessor.of(base.getProperties()).delProperty("type", getType());
        MapAccessor accessor = MapAccessor.of(base.getProperties(), getType());
        accessor.clear();
        return true;
    }

    protected String getType() {

        //
        // Build a lower case based type from the class suffix.
        //

        String className = getClass().getSimpleName();
        String superName = Nature.class.getSimpleName();
        if (className.startsWith(superName)) {
            return className.substring(superName.length()).toLowerCase();
        }
        throw new IllegalArgumentException("Class name is unconventional " + className + " to " + superName);
    }

    protected abstract Map<String, String> getTypeProperties();

    protected final Logger logger = LoggerFactory.getLogger(getClass());
}
