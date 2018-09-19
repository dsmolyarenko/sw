package org.no.sw.core.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.no.sw.core.event.EventTick;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class WatcherConsole implements Watcher {

	public WatcherConsole() {
		// TODO Auto-generated constructor stub
	}

	@EventListener
	protected void logTicker(EventTick e) {
		log.info("Tick happened " + e);
	}
	
	private final Log log = LogFactory.getLog(getClass());
}
