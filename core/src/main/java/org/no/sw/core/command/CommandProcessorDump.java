package org.no.sw.core.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.no.sw.core.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandProcessorDump extends CommandProcessorAdaptor<CommandProcessorDump.Command> {

    @Autowired
    private ContentService contentService;

    @Override
    public void process(Command command) {
        contentService.getAll().forEach(b -> log.info(b));
    }

    public static class Command implements CommandProcessor.Command {

    }

    private final Log log = LogFactory.getLog(getClass());
}
