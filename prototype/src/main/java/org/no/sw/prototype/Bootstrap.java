package org.no.sw.prototype;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Bootstrap {
    public static void main(String[] arguments) throws Throwable {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring-prototype.xml");
        context.registerShutdownHook();

    }
}