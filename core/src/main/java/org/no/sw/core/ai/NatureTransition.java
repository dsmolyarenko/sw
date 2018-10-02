package org.no.sw.core.ai;

import java.util.Map;

import org.no.sw.core.command.CommandProcessor;
import org.no.sw.core.command.CommandProcessorAdaptor;
import org.no.sw.core.util.Maps;
import org.springframework.stereotype.Component;

@Component
public class NatureTransition extends Nature {

    @Override
    protected Map<String, String> getTypeProperties() {
        return Maps.<String, String>of()
                .put("point")
                .build();
    }

    @Component
    public static class CommandProcessorTransitionGo extends CommandProcessorAdaptor<CommandProcessorTransitionGo.Command> {

        @Override
        public void process(Command c) {
        }

        public static class Command implements CommandProcessor.Command {
        }
    }
}
