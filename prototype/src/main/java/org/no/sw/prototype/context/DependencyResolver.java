package org.no.sw.prototype.context;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DependencyResolver {

    private DependencyContext dependencyContext;

    public DependencyResolver(DependencyContext dependencyContext) {
        this.dependencyContext = dependencyContext;
    }

    private Queue<Tuple> dependencies = new LinkedList<>();

    public DependencyResolver collect(Class<?> cc, Object... configurations) {
        for (Object c : configurations) {
            dependencies.add(new Tuple(cc, c));
        }
        return this;
    }

    public <T> void resolve() {
        Map<Object, String> r = new HashMap<>();

        int rc = 0;
        int rd = 0;

        for (;;) {
            Tuple tuple = dependencies.poll();
            if (tuple == null) {
                break;
            }
            try {
                dependencyContext.get(tuple.cc, tuple.c);

                String id = r.remove(tuple);
                if (id != null) {
                    rc++;
                }
            } catch (DependencyUnesolvedException e) {
                String id = r.put(tuple.c, e.getId());
                if (id != null) {
                    rd++;
                }
                dependencies.offer(tuple);
                if (dependencies.size() == r.size()) {
                    throw new IllegalArgumentException("Unable to resolve one of dependencies for specified configurations: " + r);
                }
            }
        }

        logger.log(Level.INFO, "Dependency complexity {0}", rc + rd);
    }

    private Logger logger = Logger.getGlobal();

    private static class Tuple {

        private Class<?> cc;

        private Object c;

        public Tuple(Class<?> cc, Object c) {
            this.cc = cc;
            this.c = c;
        }

    }

}