package org.no.sw.core.command;

import org.no.sw.core.model.SWBase;
import org.no.sw.core.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessorCreateWorld implements Processor<ProcessorCreateWorld.C> {

    @Autowired
    private ContentService contentService;

    @Override
    public void process(C command) {
        SWBase swBase = contentService.create(command.getId());
        swBase.addProperty("type", "base");
        swBase.setProperty("name", command.getName());

        swBase.addProperty("type", "container");

        contentService.update(swBase);
    }

    public static class C implements Command {

        private final String id;

        private final String name;

        public C(String id, String name) {
            super();
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
