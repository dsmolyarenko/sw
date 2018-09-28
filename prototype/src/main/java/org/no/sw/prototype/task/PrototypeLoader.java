package org.no.sw.prototype.task;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.no.sw.core.util.MapAccessor;
import org.no.sw.prototype.service.PrototypeService;
import org.no.sw.prototype.service.PrototypeService.DependencyUnesolvedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@Component
public class PrototypeLoader {

    @Autowired
    private PrototypeService prototypeService;

    private final ObjectMapper objectMapper;

    public PrototypeLoader() {
        objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(Visibility.ANY));
    }

    @PostConstruct
    public void load() throws IOException {
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();

        Resource[] resources = patternResolver.getResources("classpath*:*.yaml");
        for (Resource resource : resources) {
            load(resource);
        }

        try {
            Map<String, MapAccessor> prototypes = prototypeService.getAll();
            logger.info("Loaded {} prototypes", prototypes);
        } catch (DependencyUnesolvedException e) {
            logger.warn("Unable to resolve dependencies: {}", e.getIds());
        }
    }

    private void load(Resource resource) throws IOException {
        logger.info("Loading resource: {}", resource.getURI());

        Reader reader = new InputStreamReader(resource.getInputStream());

        List<Map<String, String>> prototypes = objectMapper.readValue(reader, new TypeReference<List<TreeMap<String, String>>>() {});
        for (Map<String, String> prototype : prototypes) {
            prototypeService.collect(prototype);
        }
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());
}
