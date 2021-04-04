package org.rj.homectl.aggregation.controller;

import org.rj.homectl.aggregation.Aggregation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AggregationController {
    private final Aggregation parent;

    public AggregationController(Aggregation parent) {
        this.parent = parent;
    }

    @RequestMapping("/")
    public String home() {
        return String.format("Cache holds %d items", parent.getCache().get().getCount());
    }
}
