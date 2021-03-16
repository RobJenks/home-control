package org.rj.homectl.service;


import org.rj.homectl.common.config.Config;
import org.rj.homectl.spring.application.SpringApplicationContext;
import org.rj.homectl.spring.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;

@ComponentScan(basePackages = "org.rj")
public abstract class ServiceBase {
    private final Logger log;
    private final SpringApplicationContext context;
    private Config config;
    private ServiceBaseController service;

    @Autowired
    public ServiceBase(Class<?> implementationClass, SpringApplicationContext context) {
        this.log = LoggerFactory.getLogger(implementationClass);
        this.context = context;

        Runtime.getRuntime().addShutdownHook(new Thread(this::preShutdown));
    }

    @PostConstruct
    private void initialise() {
        this.config = SpringUtil.getConfigurationFromSpringEnvironment(context.getEnvironment());
        this.config.toMap().entrySet().forEach(e -> log.info("Application Property: {}={}", e.getKey(),e.getValue()));

        this.service = new ServiceBaseController(this);
    }

    protected Config getConfig() {
        return config;
    }
    protected SpringApplicationContext getContext() { return context; }

    /**
     * Event called when the terminate signal is received.  Method for derived classes to
     * apply logic before application shutdown is called.
     *
     * @param reason        Reason provided for termination
     * @return              Implementer should return 'true' to continue with shutdown, or
     *                      'false' will cancel the termination request
     */
    protected abstract boolean handleTerminationRequest(String reason);

    public void terminate(String reason) {
        log.info("Received termination signal ({})", reason);

        final var allowTermination = handleTerminationRequest(reason);
        if (!allowTermination) return;

        SpringApplication.exit(context.getApplicationContext(), () -> 0);
    }

    private void preShutdown() {
        log.info("JVM shutdown initiated, ending process");
    }
}
