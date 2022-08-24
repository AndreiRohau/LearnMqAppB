package org.learn.controller;

import lombok.extern.slf4j.Slf4j;
import org.learn.jms.QueueListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class MainController {
    @Value("${spring.application.name}")
    private String springApplicationName;
    @Autowired
    private QueueListener queueListener;
    private List<QueueListener> consumers = new ArrayList<>();

    @GetMapping("/status")
    public String status() {
        final String logMessage = "${spring.application.name}=[" + springApplicationName + "].\n" + "Working!";
        log.info(logMessage);
        return logMessage;
    }

    @GetMapping("/addConsumer")
    public String addConsumer() {
        final String logMessage = "${spring.application.name}=[" + springApplicationName + "].\n" + "Working!";
        log.info(logMessage);
        consumers.add(new QueueListener());
        return consumers.toString();
    }
}
