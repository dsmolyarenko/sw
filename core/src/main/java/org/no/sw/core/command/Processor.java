package org.no.sw.core.command;

public interface Processor<C extends Command> {

    void process(C command);

}
