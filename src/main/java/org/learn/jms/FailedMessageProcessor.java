package org.learn.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FailedMessageProcessor {
    private static final String FAILED_MESSAGE_EXCHANGE = "source2-out-0";
    private final String processorName;
    @Value("${routing.key.header}")
    private String routingKeyHeader;
    @Value("${mq.queue2Sink-in-0.consumer.bindingRoutingKey}")
    private String failedMessageRoutingKey;
    @Autowired
    private final StreamBridge streamBridge;

    public FailedMessageProcessor(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
        this.processorName = "FAILED_MSG_PROCESSOR";
    }

    public void processFailed(Message<?> message) {
        ErrorMessage errorMessage = (ErrorMessage) message;
        String msgTxt = new String((byte[]) errorMessage.getOriginalMessage().getPayload());
        log.info("_____{}. Processing. MSG={}", processorName, msgTxt);
        streamBridge.send(FAILED_MESSAGE_EXCHANGE,
                MessageBuilder
                        .withPayload(msgTxt)
                        .setHeader(routingKeyHeader, failedMessageRoutingKey)
                        .build());
        log.info("_____{}. Processed (SENT to FAILED_MSG_Q). MSG={}", processorName, msgTxt);
    }
}
