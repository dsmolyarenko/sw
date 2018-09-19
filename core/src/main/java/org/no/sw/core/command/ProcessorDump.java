package org.no.sw.core.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.no.sw.core.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessorDump implements Processor<ProcessorDump.C> {

    @Autowired
    private ContentService contentService;

    @Override
    public void process(C command) {
        contentService.getAll().forEach(b -> log.info(b));
    }

    public static class C implements Command {

    }

    private final Log log = LogFactory.getLog(getClass());
}
