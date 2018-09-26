package org.no.sw.core.service;

import org.no.sw.core.command.CommandProcessor;

public interface CommandProcessorRegistryService {

    <C extends CommandProcessor.Command> void register(Class<C> commandClass, CommandProcessor commandProcessor);

    <C extends CommandProcessor.Command> CommandProcessor<C> getCommandProcessor(C command);

}
