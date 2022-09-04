package org.learn.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Slf4j
public class CustomConsumer implements Consumer<Message<String>> {

    private final String consumerName;

    public CustomConsumer(String consumerName) {
        this.consumerName = consumerName;
    }

    @Override
    public void accept(Message<String> msg) {
//			log.info(consumerName +
//					", payload=" + in.getPayload() +
//					", headers=" + in.getHeaders());
        List deathHeader = msg.getHeaders().get("x-death", List.class);
        Map<String, Object> death = deathHeader != null && deathHeader.size() > 0
                ? (Map<String, Object>) deathHeader.get(0)
                : null;
        log.info(consumerName + "_#_ACCEPT(Message<String> msg)" +
                "MSG=[" + msg.getPayload() + "]. " +
                "deliveryAttempt=[" + msg.getHeaders().get("deliveryAttempt", AtomicInteger.class) + "]. " +
                "x-death=[" + deathHeader + "]. " +
                "count=[" + (death != null ? (long) death.get("count") : "") + "].");

        if ("Consumer1".equals(consumerName)) {
            emulateError(death);
        }

    }

    private void emulateError(Map<String, Object> death) {
        if (death != null && (long) death.get("count") > 1) {
            // giving up - don't send to DLX
            throw new ImmediateAcknowledgeAmqpException("Failed after 2 attempts of DLX approach (in fact 3=initial + 2*requeued)");
        }
        // nack and do not re-queue
        throw new AmqpRejectAndDontRequeueException("Failed but sent to DLX->DLQ->OriginalQueue. count=[" + (death != null ? (long) death.get("count") : "") + "].");
    }
}
