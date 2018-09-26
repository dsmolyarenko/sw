package org.no.sw.core.command;

import org.no.sw.core.ai.Nature;
import org.no.sw.core.model.SWBase;
import org.no.sw.core.service.ContentService;
import org.no.sw.core.service.NatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandProcessorAddNature extends CommandProcessorAdaptor<CommandProcessorAddNature.Command> {

    @Autowired
    private ContentService contentService;

    @Autowired
    private NatureService natureService;

    @Override
    public void process(Command c) {
        SWBase base = contentService.get(c.id);
        if (base == null) {
            throw new IllegalStateException("Unable to find object: " + c.id);
        }
        Nature nature = natureService.getNature(c.type);
        if (nature == null) {
            throw new IllegalStateException("Unregistered nature type: " + c.type);
        }

        boolean updated = nature.add(base);
        if (updated) {
            contentService.update(base);
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
