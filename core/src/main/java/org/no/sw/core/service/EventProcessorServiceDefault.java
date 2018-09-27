package org.no.sw.core.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.no.sw.core.ai.Nature;
import org.no.sw.core.event.Event;
import org.no.sw.core.model.SWBase;
import org.no.sw.core.util.MapAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EventProcessorServiceDefault implements EventProcessorService {

    @Autowired
    private ContentService contentService;

    @Autowired
    private NatureService natureService;

    private final BlockingQueue<EventTuple> queue;

    public EventProcessorServiceDefault() {
        queue = new ArrayBlockingQueue<>(20);
    }

    @Override
    public void submit(String id, Event e) {
        queue.offer(new EventTuple(id, e));
        logger.debug("Event submitted: {}", e);
    }

    public void process(String id, Event e) {
        logger.debug("Event process started {}", e);
        try {
            SWBase base = contentService.get(id);
            if (base == null) {
                throw new IllegalStateException("Root element does not exist");
            }
            MapAccessor mapAccessor = MapAccessor.of(base.getProperties());
            mapAccessor.getPropertyIterator("type", t -> {
                Nature nature = natureService.getNature(t);
                if (nature == null) {
                    logger.error("Nature was not bound for the type: {}", t);
                    return;
                }
                nature.process(base, e);
            });
        } catch (Throwable t) {
            logger.error("Event process failed " + e, t);
        }
        logger.debug("Event process finished {}", e);
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 100)
    public void process() {
        while (true) {
            EventTuple r;
            try {
                r = queue.take();
            } catch (InterruptedException ee) {
                return;
            }
            if (r == null) {
                return;
            }
            try {
                process(r.getId(), r.getEvent());
            } catch (Throwable e) {
                logger.error("Exception during command processing", e);
            }
        }
    }

    private class EventTuple {

        private final String id;
        private final Event event;

        public EventTuple(String id, Event event) {
            this.id = id;
            this.event = event;
        }

        public String getId() {
            return id;
        }

        public Event getEvent() {
            return event;
        }
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

}
