package org.no.sw.prototype.context;

import java.util.Map;

import org.no.generator.configuration.ConfigurationHandler;
import org.no.generator.configuration.ConfigurationHandlerFactory;

public class DependencyFactoryDefault implements DependencyFactory {

    private final ConfigurationHandlerFactory configurationHandlerFactory;

    public DependencyFactoryDefault(ConfigurationHandlerFactory configurationHandlerFactory) {
        this.configurationHandlerFactory = configurationHandlerFactory;
    }

    @Override
    public <T> T create(Map<String, Object> configuration, Class<T> cc, DependencyContext context) {
        String ct = (String) configuration.get("type");
        if (ct == null) {
            throw new IllegalArgumentException("Configuration property 'type' isn't specified: " + configuration);
        }

        ConfigurationHandler<T> configurationHandler = configurationHandlerFactory.get(cc, ct);
        if (configurationHandler == null) {
            throw new IllegalArgumentException("Unsupported factory type: " + ct);
        }

        return configurationHandler.handle(configuration, context);
    }
}
