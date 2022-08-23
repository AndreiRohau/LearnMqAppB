package org.learn.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/status")
    public String status() {
        return "Working!";
    }

    @GetMapping("/")
    public String index() {
        return "index! AppB";
    }
}
