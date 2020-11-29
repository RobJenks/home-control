package org.rj.homectl.aggregation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/")
    public String home() {
        return String.format("<html>%d items: <BR><BR>%s", TmpData.getData().size(), String.join("<BR>", TmpData.getData()));
    }

}
