package org.rj.homectl.aggregation.controller;

import org.rj.homectl.aggregation.Aggregation;
import org.rj.homectl.common.model.HomeState;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class AggregationController {
    private final Aggregation parent;

    public AggregationController(final Aggregation parent) {
        this.parent = parent;
    }

    @GetMapping("/updates")
    public ResponseEntity<Map<String, Object>> updates(
            @RequestParam(value = "count", required = false, defaultValue = "50") int count) {

        if (count < 0) return new ResponseEntity<>(Map.of("error", "Invalid update count requested"), HttpStatus.BAD_REQUEST);

        final var data = parent.getRecordCache().getLatestRecords(count);
        final Map<String, Object> responseData = Map.of(
                "count", data.size(),
                "data", data
        );

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(responseData, headers, HttpStatus.OK);
    }

    @GetMapping("/state")
    public ResponseEntity<HomeState> state() {
        final var state = parent.getAggregationService().getState();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(state, headers, HttpStatus.OK);
    }
}
