package org.no.sw.core;

import org.no.sw.core.command.CommandProcessorAddNature;
import org.no.sw.core.command.CommandProcessorDump;
import org.no.sw.core.model.SWBase;
import org.no.sw.core.service.CommandProcessorService;
import org.no.sw.core.service.ContentService;
import org.no.sw.core.service.IdentyService;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Bootstrap {
    public static void main(String[] arguments) {


        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring-core.xml");
        context.registerShutdownHook();

        IdentyService identyService = context.getBean(IdentyService.class);

        ContentService contentService = context.getBean(ContentService.class);

        CommandProcessorService commandProcessorService = context.getBean(CommandProcessorService.class);

        String swWorldId = identyService.getId();

        SWBase swWorld = contentService.create(swWorldId);

        commandProcessorService.submit(new CommandProcessorAddNature.Command(swWorldId, "base"));
        commandProcessorService.submit(new CommandProcessorAddNature.Command(swWorldId, "container"));
        commandProcessorService.submit(new CommandProcessorDump.Command());

    }
}
