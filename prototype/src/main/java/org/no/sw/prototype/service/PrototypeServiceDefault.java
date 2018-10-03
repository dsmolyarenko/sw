package org.no.sw.prototype.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.no.sw.core.model.Source;
import org.no.sw.core.model.Target;
import org.springframework.stereotype.Component;

@Component
public class PrototypeServiceDefault implements PrototypeService {

    private final Map<String, Source> prototypes;

    private final Queue<Source> queue;

    private final Lock queueLock = new ReentrantLock();

    public PrototypeServiceDefault() {
        this.prototypes = new LinkedHashMap<>();
        this.queue = new LinkedList<>();
    }

    @Override
    public void collect(Map<String, String> prototype) {
        queueLock.lock();
        try {
            collect(Source.of(prototype));
        } finally {
            queueLock.unlock();
        }
    }

    private void collect(Source properties) {
        String id = properties.getId();

        // check if all dependencies
        List<Source> parentsProperties = new ArrayList<>();
        for (String parentId : properties.getPropertyValues("parent")) {
            Source parent = prototypes.get(parentId);
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
            Target prentPropertiesResult = Target.of();
            for (Source parentProperties : parentsProperties) {
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
    public Map<String, Source> getAll() throws DependencyUnesolvedException {
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
                        return e.getPropertyValue("id") + " -> " + e.getPropertyValue("parent");
                    }).collect(Collectors.toList()));
                }
            }
        } finally {
            queueLock.unlock();
        }
    }
}
