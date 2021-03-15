package org.rj.homectl.monitor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "org.rj")
public abstract class MonitorAgent {
    private final Logger log;
    private final MonitorAgentService service;

    public MonitorAgent(Class<?> implementationClass) {
        this.log = LoggerFactory.getLogger(implementationClass);
        this.service = new MonitorAgentService(this);

        Runtime.getRuntime().addShutdownHook(new Thread(this::preShutdown));
    }

    protected abstract ApplicationContext getApplicationContext();

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

        SpringApplication.exit(getApplicationContext(), () -> 0);
    }

    private void preShutdown() {
        log.info("JVM shutdown initiated, ending process");
    }
}
