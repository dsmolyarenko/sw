package org.no.sw.core.command;

public interface CommandProcessor<C extends CommandProcessor.Command> {

    void process(C command);

    public interface Command {

    }
}
