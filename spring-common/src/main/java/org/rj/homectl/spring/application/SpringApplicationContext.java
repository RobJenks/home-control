package org.rj.homectl.spring.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SpringApplicationContext {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private Environment environment;

    public SpringApplicationContext() { }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public Environment getEnvironment() {
        return environment;
    }
}
