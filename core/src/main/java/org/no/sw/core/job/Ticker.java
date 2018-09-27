package org.no.sw.core.job;

import org.no.sw.core.event.EventTick;
import org.no.sw.core.service.EventProcessorService;
import org.no.sw.core.service.IdentyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Ticker {

    @Autowired
    private IdentyService identyService;

    @Autowired
    private EventProcessorService eventProcessorService;

    //@Scheduled(cron = "*/5 * * * * *")
    public void tick() {
        EventTick e = new EventTick(identyService.getId());
        eventProcessorService.submit(identyService.getRootId(), e);
    }

}
