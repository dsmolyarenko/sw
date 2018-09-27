package org.no.sw.core;

import org.no.sw.core.command.CommandProcessorAddNature;
import org.no.sw.core.command.CommandProcessorContainerMoveTo;
import org.no.sw.core.command.CommandProcessorDump;
import org.no.sw.core.model.SWBase;
import org.no.sw.core.service.CommandProcessorService;
import org.no.sw.core.service.ContentService;
import org.no.sw.core.service.IdentyService;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Bootstrap {
    public static void main(String[] arguments) throws Throwable {


        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring-core.xml");
        context.registerShutdownHook();

        IdentyService identyService = context.getBean(IdentyService.class);

        ContentService contentService = context.getBean(ContentService.class);

        CommandProcessorService commandProcessorService = context.getBean(CommandProcessorService.class);

        String swWorldId = identyService.getRootId();

        SWBase swWorld = contentService.create(swWorldId);
        SWBase swItem1 = contentService.create(identyService.getId());
        SWBase swItem2 = contentService.create(identyService.getId());
        SWBase swItem3 = contentService.create(identyService.getId());

        commandProcessorService.submit(new CommandProcessorAddNature.Command(swWorld.getId(), "base"));
        commandProcessorService.submit(new CommandProcessorAddNature.Command(swWorld.getId(), "container"));
        commandProcessorService.submit(new CommandProcessorAddNature.Command(swItem1.getId(), "base"));
        commandProcessorService.submit(new CommandProcessorAddNature.Command(swItem2.getId(), "base"));
        commandProcessorService.submit(new CommandProcessorAddNature.Command(swItem3.getId(), "base"));
        commandProcessorService.submit(new CommandProcessorContainerMoveTo.Command(swItem1.getId(), swWorld.getId()));
        commandProcessorService.submit(new CommandProcessorContainerMoveTo.Command(swItem2.getId(), swWorld.getId()));
        commandProcessorService.submit(new CommandProcessorContainerMoveTo.Command(swItem3.getId(), swWorld.getId()));
        commandProcessorService.submit(new CommandProcessorDump.Command());
    }
}
