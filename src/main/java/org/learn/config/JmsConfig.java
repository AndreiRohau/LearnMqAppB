package org.learn.config;

import lombok.extern.slf4j.Slf4j;
import org.learn.jms.CustomConsumer;
import org.learn.jms.FailedMessageProcessor;
import org.springframework.amqp.core.DeclarableCustomizer;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class JmsConfig {
    @Autowired
    private FailedMessageProcessor failedMessageProcessor;

    @StreamListener("errorChannel")
    public void errorChannel(Message<?> message) {
        log.info("_____errorChannel Handling ERROR: " + message);
        failedMessageProcessor.processFailed(message);
    }

    @Bean
    public Consumer<Message<String>> queue1Sink() {
        return new CustomConsumer("INIT_MSG_CONSUMER");
    }

}
