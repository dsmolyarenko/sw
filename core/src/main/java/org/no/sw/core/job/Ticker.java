package org.no.sw.core.job;

import org.no.sw.core.event.EventTick;
import org.no.sw.core.service.IdentyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Ticker {

    private final ApplicationEventPublisher publisher;

    private final IdentyService identyService;

    @Autowired
    public Ticker(ApplicationEventPublisher publisher,
            IdentyService identyService) {
        this.publisher = publisher;
        this.identyService = identyService;
    }

    @Scheduled(cron = "*/5 * * * * *")
    public void tickerMain() {
        publisher.publishEvent(new EventTick(identyService.getId()));
    }
}
