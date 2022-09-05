package org.learn.config;

import lombok.extern.slf4j.Slf4j;
import org.learn.jms.CustomConsumer;
import org.learn.jms.FailedMessageProcessor;
import org.springframework.amqp.core.DeclarableCustomizer;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

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

    // wellll, I'm probably need to open a jira ticket in spring-cloud-steam-rabbitmq repository
    @Bean
    @ConditionalOnProperty(name = "republish", havingValue = "true")
    public DeclarableCustomizer declarableCustomizer() {
        return declarable -> {
            if (declarable instanceof Queue) {
                var queue = (Queue) declarable;
                if (queue.getName().equals("demo-queue-1")
                        || queue.getName().equals("demo-queue-2")) {
                    queue.removeArgument("x-dead-letter-exchange");
                    queue.removeArgument("x-dead-letter-routing-key");
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
