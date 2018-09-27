package org.no.sw.core.service;

import org.no.sw.core.event.Event;

public interface EventProcessorService {

    public void submit(String id, Event e);

}
