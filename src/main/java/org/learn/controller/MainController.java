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
import java.util.stream.Collectors;

import static org.learn.jms.Destination.QUEUE;

@Slf4j
@RestController
public class MainController {
    @Value("${spring.application.name}")
    private String springApplicationName;
    private final Supplier<QueueListener> queueListenerSupplier;
    private final List<QueueListener> consumers = new ArrayList<>();

    @Autowired
    public MainController(Supplier<QueueListener> queueListenerSupplier) {
        this.queueListenerSupplier = queueListenerSupplier;
    }

    @PostConstruct
    private void postConstruct() {
        addAnotherListener();
    }

    @GetMapping("/status")
    public String status() {
        log.info("CALLED: /status");
        return "[" + springApplicationName + "]. " + "Working!";
    }

    @GetMapping("/info")
    public String getInfo() {
        log.info("CALLED: /info");
        return "QUEUE:EMITTED/PROCESSED=[" + QUEUE.getEmitted() + "/" + QUEUE.getProcessed() + "]<br/>" +
                QUEUE.getStringBuilder().toString();
    }

    @GetMapping("/addConsumer")
    public String addConsumer() {
        log.info("CALLED /addConsumer");
        addAnotherListener();
        return String.join("<br/>", consumers.stream().map(QueueListener::toString).collect(Collectors.toList()));
    }

    private void addAnotherListener() {
        consumers.add(queueListenerSupplier.get());
    }
}
