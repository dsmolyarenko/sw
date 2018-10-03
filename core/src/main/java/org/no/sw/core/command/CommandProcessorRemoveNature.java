package org.no.sw.core.command;

import org.no.sw.core.ai.Nature;
import org.no.sw.core.model.Source;
import org.no.sw.core.service.ContentService;
import org.no.sw.core.service.NatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandProcessorRemoveNature extends CommandProcessorAdaptor<CommandProcessorRemoveNature.Command> {

    @Autowired
    private ContentService contentService;

    @Autowired
    private NatureService natureService;

    @Override
    public void process(Command c) {
        Source source = contentService.getById(c.id);
        if (source == null) {
            throw new IllegalStateException("Unable to find object: " + c.id);
        }
        Nature nature = natureService.getNature(c.type);
        if (nature == null) {
            throw new IllegalStateException("Unregistered nature type");
        }

        boolean updated = nature.remove(source);
        if (updated) {
            contentService.save(source);
        }
    }

    public static class Command implements CommandProcessor.Command {

        private final String id;
        private final String type;

        public Command(String id, String type) {
            this.id = id;
            this.type = type;
        }
    }
}
