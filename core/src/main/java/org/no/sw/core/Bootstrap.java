package org.no.sw.core;

import org.no.sw.core.command.ProcessorCreateWorld;
import org.no.sw.core.command.ProcessorDump;
import org.no.sw.core.service.ContentService;
import org.no.sw.core.service.IdentyService;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Bootstrap {
    public static void main(String[] arguments) {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring-core.xml");
        context.registerShutdownHook();

        IdentyService identyService = context.getBean(IdentyService.class);

        ContentService contentService = context.getBean(ContentService.class);

        ProcessorCreateWorld processorCreateWorld = context.getBean(ProcessorCreateWorld.class);

        String swId = identyService.getId();
        processorCreateWorld.process(new ProcessorCreateWorld.C(swId, "SW"));

        context.getBean(ProcessorDump.class).process(new ProcessorDump.C());


    }
}
