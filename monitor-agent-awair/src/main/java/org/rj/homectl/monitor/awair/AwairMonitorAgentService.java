package org.rj.homectl.monitor.awair;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AwairMonitorAgentService {
    private final AwairMonitorAgent agent;

    public AwairMonitorAgentService(AwairMonitorAgent agent) {
        this.agent = agent;
    }

    @PostMapping("/terminate")
    public void terminate() {
        agent.terminate("service request");
    }

}
