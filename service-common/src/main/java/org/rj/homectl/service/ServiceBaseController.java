package org.rj.homectl.service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceBaseController {
    private final ServiceBase service;

    public ServiceBaseController(ServiceBase service) {
        this.service = service;
    }

    @PostMapping("/terminate")
    public void terminate() {
        service.terminate("service request");
    }
}
