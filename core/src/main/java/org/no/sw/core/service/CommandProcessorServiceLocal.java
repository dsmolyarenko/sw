package org.no.sw.core.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.no.sw.core.command.CommandProcessor;
import org.no.sw.core.command.CommandProcessor.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CommandProcessorServiceLocal implements CommandProcessorService {

    @Autowired
    private CommandProcessorRegistryService commandProcessorRegistryService;

    private final BlockingQueue<Runnable> commands;

    public CommandProcessorServiceLocal() {
        commands = new ArrayBlockingQueue<>(20);
    }

    @Override
    public <C extends Command> void submit(C command) {
        CommandProcessor<C> commandProcessor = commandProcessorRegistryService.getCommandProcessor(command);
        if (commandProcessor == null) {
            log.warn("Command processor not found: {}" + command);
        } else {
            commands.offer(() -> {
                commandProcessor.process(command);
            });
        }
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 100)
    public void process() {
        while (true) {
            Runnable r;
            try {
                r = commands.take();
            } catch (InterruptedException ee) {
                return;
            }
            if (r == null) {
                return;
            }
            try {
                r.run();
            } catch (Throwable e) {
                log.error("Exception during command processing", e);
            }
        }
    }

    private final Log log = LogFactory.getLog(getClass());

}
