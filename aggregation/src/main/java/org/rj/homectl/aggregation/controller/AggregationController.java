package org.rj.homectl.aggregation.controller;

import org.rj.homectl.aggregation.Aggregation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class AggregationController {
    private final Aggregation parent;

    public AggregationController(Aggregation parent) {
        this.parent = parent;
    }

    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> data() {
        final var data = parent.getCache().get().toList();
        Collections.reverse(data);

        final Map<String, Object> responseData = Map.of(
                "count", parent.getCache().get().getCount(),
                "data", data
        );

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(responseData, headers, HttpStatus.OK);
    }
}
