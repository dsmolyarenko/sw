package org.no.sw.prototype.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.no.sw.core.util.MapAccessor;
import org.springframework.stereotype.Component;

@Component
public class ProtypeServiceDefault implements PrototypeService {

    private final Map<String, MapAccessor> prototypes;

    private final Queue<MapAccessor> queue;

    private final Lock queueLock = new ReentrantLock();

    public ProtypeServiceDefault() {
        this.prototypes = new LinkedHashMap<>();
        this.queue = new LinkedList<>();
    }

    @Override
    public void collect(Map<String, String> prototype) {
        queueLock.lock();
        try {
            collect(MapAccessor.of(prototype));
        } finally {
            queueLock.unlock();
        }
    }

    private void collect(MapAccessor properties) {
        String id = properties.getProperty("id");

        // check if all dependencies
        List<MapAccessor> parentsProperties = new ArrayList<>();
        for (String parentId : properties.getPropertyValues("parent")) {
            MapAccessor parent = prototypes.get(parentId);
            if (parent != null) {
                parentsProperties.add(parent);
            } else {
                // missing dependency faced
                queue.add(properties);
                // resolve later
                return;
            }
        }

        if (parentsProperties.size() > 0) {
            MapAccessor prentPropertiesResult = MapAccessor.of(new TreeMap<>());
            for (MapAccessor parentProperties : parentsProperties) {
                prentPropertiesResult.addPropertiesExcluding(parentProperties, "id", "parent");
            }
            // override parent properties
            prentPropertiesResult.setPropertiesExcluding(properties, "parent");
            // switch to new copy
            properties = prentPropertiesResult;
        }

        prototypes.put(id, properties);
    }

    @Override
    public Map<String, MapAccessor> getAll() throws DependencyUnesolvedException {
        resolve(); // check if there are some unresolved stuff
        return prototypes;
    }

    private void resolve() {
        queueLock.lock();
        try {
            while (true) {
                int size = queue.size();
                if (size == 0) {
                    break;
                }
                for (int i = 0; i < size; i++) {
                    collect(queue.poll());
                }
                if (queue.size() == size) { // no one was resolved
                    throw new DependencyUnesolvedException(queue.stream().map(e -> {
                        return e.getProperty("id") + " -> " + e.getProperty("parent");
                    }).collect(Collectors.toList()));
                }
            }
        } finally {
            queueLock.unlock();
        }
    }
}
