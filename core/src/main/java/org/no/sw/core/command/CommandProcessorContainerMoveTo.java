package org.no.sw.core.command;

import org.no.sw.core.model.SWBase;
import org.no.sw.core.service.ContentService;
import org.no.sw.core.util.MapAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandProcessorContainerMoveTo extends CommandProcessorAdaptor<CommandProcessorContainerMoveTo.Command> {

    @Autowired
    private ContentService contentService;

    @Override
    public void process(Command c) {
        SWBase source = contentService.get(c.sourceId);
        if (source == null) {
            throw new IllegalStateException("Unable to find object: " + c.sourceId);
        }
        SWBase target = contentService.get(c.targetId);
        if (target == null) {
            throw new IllegalStateException("Unable to find object: " + c.targetId);
        }

        MapAccessor.of(source.getProperties(), "base").setProperty("parent", target.getId());
        MapAccessor.of(target.getProperties(), "container").addProperty("content", source.getId());

        contentService.update(source, target);
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
