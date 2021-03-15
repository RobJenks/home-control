package org.rj.homectl.monitor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitorAgentService {
    private final MonitorAgent agent;

    public MonitorAgentService(MonitorAgent agent) {
        this.agent = agent;
    }

    @PostMapping("/terminate")
    public void terminate() {
        agent.terminate("service request");
    }
}
