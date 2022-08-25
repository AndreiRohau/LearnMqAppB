package org.learn.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

@Slf4j
@RestController
public class MainController {
    @Value("${spring.application.name}")
    private String springApplicationName;

    public static ArrayBlockingQueue<String> CACHE = new ArrayBlockingQueue<>(1000);

    @GetMapping("/status")
    public String status() {
        log.info("CALLED: /status");
        return "[" + springApplicationName + "]. " + "Working!";
    }

    @GetMapping("/info")
    public String getInfo() {
        log.info("CALLED: /info");
        return String.join("<br/>", drainCacheToList());
    }

    private synchronized static List<String> drainCacheToList() {
        List<String> list = new ArrayList<>();
        CACHE.drainTo(list);
        return list;
    }
}
