package org.no.sw.core.command;

import java.lang.reflect.ParameterizedType;

import javax.annotation.PostConstruct;

import org.no.sw.core.service.CommandProcessorRegistryService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommandProcessorAdaptor<C extends CommandProcessor.Command> implements CommandProcessor<C> {

    @Autowired
    private CommandProcessorRegistryService commandProcessorService;

    @PostConstruct
    public final void setup() {
        Class<C> commandClass = (Class<C>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        commandProcessorService.register(commandClass, this);
    }
}
