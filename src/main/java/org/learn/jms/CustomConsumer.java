package org.learn.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Slf4j
public class CustomConsumer implements Consumer<Message<String>> {

    private final String consumerName;
    private static final String CONTAINS_NUMBERS_REGEX = ".*\\d.*";

    public CustomConsumer(String consumerName) {
        this.consumerName = consumerName;
    }

    @Override
    public void accept(Message<String> msg) {
        log.info("_____{}. MSG=[{}]. ATTEMPT", consumerName, msg.getPayload());
//        log.info(consumerName +
//                ", payload=" + msg.getPayload() +
//                ", headers=" + msg.getHeaders());
        if (msg.getPayload().matches(CONTAINS_NUMBERS_REGEX)) {
            throw new RuntimeException("No numbers allowed. Use words.");
        }

        log.info("_____{}. MSG=[{}]. Successfully processed!!!", consumerName, msg.getPayload());
    }

}
