package org.learn.controller;

import lombok.extern.slf4j.Slf4j;
import org.learn.jms.QueueListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@RestController
public class MainController {
    @Value("${spring.application.name}")
    private String springApplicationName;
    @Autowired
    private Supplier<QueueListener> queueListenerSupplier;
    private List<QueueListener> consumers = new ArrayList<>();

    @PostConstruct
    private void postConstruct() {
        log.info("queueListenerSupplier === {}", queueListenerSupplier);
        log.info("consumers === {}", consumers);
        log.info("postConstruct worked");
        addAnotherListener();
    }

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
        addAnotherListener();
        return consumers.toString();
    }

    private void addAnotherListener() {
        consumers.add(queueListenerSupplier.get());
    }
}
