package org.no.sw.prototype.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    public void collect(MapAccessor properties) {
        String id = properties.getProperty("id");

        while (true) {
            String parentId = properties.popProperty("parent");
            if (parentId == null) {
                break;
            }
            MapAccessor parent = prototypes.get(parentId);
            if (parent == null) {
                queue.add(properties);
                return;
            }
            properties.addProperties(parent, "id");
            properties.delProperty("parent");
        }
        prototypes.put(id, properties);
    }

    @Override
    public Map<String, MapAccessor> getAll() throws DependencyUnesolvedException {
        if (queue.size() > 0) {
            int size = queue.size();
            while (queue.size() > 0) {
                new ArrayList<>(queue).forEach(this::collect);
                if (queue.size() == size) {
                    DependencyUnesolvedException dependencyUnesolvedException = new DependencyUnesolvedException();
                    queue.forEach(p -> dependencyUnesolvedException.addId(p.getProperty("id")));
                    throw dependencyUnesolvedException;
                }
            }
        }
        return prototypes;
    }
}
