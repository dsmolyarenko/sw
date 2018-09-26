package org.no.sw.core.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.no.sw.core.command.CommandProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CommandProcessorRegistryServiceLocal implements CommandProcessorRegistryService {

    private final Map<Class<? extends CommandProcessor.Command>, CommandProcessor<?>> commandProcessors;

    public CommandProcessorRegistryServiceLocal() {
        commandProcessors = new ConcurrentHashMap<>();
    }

    @Override
    public <C extends CommandProcessor.Command> void register(Class<C> commandClass, CommandProcessor commandProcessor) {
        commandProcessors.put(commandClass, commandProcessor);
        logger.info("Registered command processor: {}", commandClass);
    }

    @Override
    public <C extends CommandProcessor.Command> CommandProcessor<C> getCommandProcessor(C command) {
        return (CommandProcessor<C>) commandProcessors.get(command.getClass());
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());
}
