package org.no.sw.core.service;

import org.no.sw.core.command.CommandProcessor;

public interface CommandProcessorService {

    <C extends CommandProcessor.Command> void submit(C command);

}
