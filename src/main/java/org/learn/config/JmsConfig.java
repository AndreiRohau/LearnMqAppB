package org.learn.config;

import lombok.extern.slf4j.Slf4j;
import org.learn.jms.CustomSink;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

@Slf4j
@Configuration
@EnableBinding({CustomSink.class})
public class JmsConfig {

    @StreamListener(CustomSink.QUEUE1)
    public void listenQueue1(Message<String> in) {
        log.info("Consumer1: " + in.getPayload());
//		throw new RuntimeException("Demo exception");
    }

    @StreamListener(CustomSink.QUEUE2)
    public void listenQueue2(Message<String> in) {
        log.info("Consumer2: " + in.getPayload());
    }
}
