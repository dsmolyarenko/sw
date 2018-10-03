package org.no.sw.core.ai;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.no.sw.core.event.Event;
import org.no.sw.core.model.Source;
import org.no.sw.core.model.Target;
import org.no.sw.core.service.ContentService;
import org.no.sw.core.service.NatureService;
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

    public boolean process(Source base, Event event) {
        return false;
    }

    public boolean add(Source source) {
        return Target.of(source).addPropertyValue("type", getType());
    }

    public boolean remove(Source base) {
        throw new UnsupportedOperationException();
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

    protected abstract Set<String> getTypeProperties();

    protected final Logger logger = LoggerFactory.getLogger(getClass());
}
