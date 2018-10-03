package org.no.sw.core.command;

import org.no.sw.core.ai.NatureBase;
import org.no.sw.core.model.Source;
import org.no.sw.core.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandProcessorContainerMoveTo extends CommandProcessorAdaptor<CommandProcessorContainerMoveTo.Command> {

    @Autowired
    private ContentService contentService;

    @Override
    public void process(Command c) {
        Source source = contentService.getById(c.sourceId);
        if (source == null) {
            throw new IllegalStateException("Unable to find object: " + c.sourceId);
        }
        Source parent = contentService.getById(c.targetId);
        if (parent == null) {
            throw new IllegalStateException("Unable to find object: " + c.targetId);
        }

        boolean updated = NatureBase.setParent(source, parent);
        if (updated) {
            contentService.save(source);
        }
    }

    public static class Command implements CommandProcessor.Command {

        private final String sourceId;
        private final String targetId;

        public Command(String sourceId, String targetId) {
            this.sourceId = sourceId;
            this.targetId = targetId;
        }
    }
}
