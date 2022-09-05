package org.learn.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Slf4j
public class CustomConsumer implements Consumer<Message<String>> {

    private final String consumerName;
    private static final String CONTAINS_NUMBERS_REGEX = ".*\\d.*";

    @Autowired
    private FailedMessageProcessor failedMessageProcessor;

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
            runRepublishRetryStrategy(msg);
//            throw new RuntimeException("No numbers allowed. Use words.");
        }

        log.info("_____{}. MSG=[{}]. Successfully processed!!!", consumerName, msg.getPayload());
    }

    private void runRepublishRetryStrategy(Message<String> msg) {
        List<Object> deathHeader = msg.getHeaders().get("x-death", List.class);
        Map<String, Object> death = deathHeader != null && deathHeader.size() > 0
                ? (Map<String, Object>)deathHeader.get(0)
                : null;
        log.info("_____{}. MSG=[{}]. deliveryAttempt=[{}]. retry-count=[{}]", consumerName, msg.getPayload(),
                msg.getHeaders().get("deliveryAttempt", AtomicInteger.class), (death != null ? (long) death.get("count") : ""));

        if (death != null && (long) death.get("count") > 1) {
            // giving up - don't send to DLX
//            throw new ImmediateAcknowledgeAmqpException("Failed after 2 attempts (in fact 3=initial + 2requeued)");
            // lets forwqard this message as unprocessable FAILED_MSG
            failedMessageProcessor.processFailed(msg);
        }
        // nack and do not re-queue
        throw new AmqpRejectAndDontRequeueException("failed and requeued count=[" + (death != null ? (long) death.get("count") : "") + "].");
    }

}
