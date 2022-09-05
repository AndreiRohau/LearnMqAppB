package org.learn.config;

import lombok.extern.slf4j.Slf4j;
import org.learn.jms.CustomConsumer;
import org.learn.jms.FailedMessageProcessor;
import org.springframework.amqp.core.DeclarableCustomizer;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.time.Duration;
import java.util.function.Consumer;

@Slf4j
@Configuration
public class JmsConfig {
    @Autowired
    private FailedMessageProcessor failedMessageProcessor;

//    @StreamListener("errorChannel")
//    public void errorChannel(Message<?> message) {
//        log.info("_____errorChannel Handling ERROR: " + message);
//        failedMessageProcessor.processFailed(message);
//    }


    @Bean
    // wellll, I'm probably need to open a jira ticket in spring-cloud-steam-rabbitmq repository
    public DeclarableCustomizer declarableCustomizer() {
        return declarable -> {
            if (declarable instanceof Queue) {
                var queue = (Queue) declarable;
                if (queue.getName().equals("demo-queue1")
                        || queue.getName().equals("demo-queue2")) {
                    queue.removeArgument("x-dead-letter-exchange");
                    queue.removeArgument("x-dead-letter-routing-key");

                    queue.addArgument("x-dead-letter-exchange", "deadletter-exchange");
                }
            }
            return declarable;
        };
    }

    @Bean
    public Consumer<Message<String>> queue1Sink() {
        return new CustomConsumer("INIT_MSG_CONSUMER");
    }

}
