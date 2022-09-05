package org.learn.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
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
    private final String processorName;
    private static final String FAILED_MESSAGE_EXCHANGE = "failed-out-0";
    private static final String routingKeyHeader = "myRoutingKey";
    private static final String routingKey = "routing-queue1";
    @Autowired
    private final StreamBridge streamBridge;

    public FailedMessageProcessor(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
        this.processorName = "FAILED_MSG_PROCESSOR";
    }

    public void processFailed(Message<?> message) {
        String msgTxt = getMsgTxt(message);
        log.info("_____{}. Processing. MSG={}", processorName, msgTxt);
        streamBridge.send(FAILED_MESSAGE_EXCHANGE,
                MessageBuilder
                        .withPayload(msgTxt)
                        .build());
        log.info("_____{}. Processed (SENT to FAILED_MSG_Q). MSG={}", processorName, msgTxt);
        // STOP RE-PUBLISH RETRYING EXCEPTION!
        throw new ImmediateAcknowledgeAmqpException("Failed after 2 attempts (in fact 3=initial + 2requeued)" + "REMINDING!!! No numbers allowed. Use words.");
    }

    private String getMsgTxt(Message<?> message) {
        String msgTxt = null;
        if (message instanceof ErrorMessage) {
            ErrorMessage errorMessage = (ErrorMessage) message;
            msgTxt = new String((byte[]) errorMessage.getOriginalMessage().getPayload());
        } else {
            msgTxt = message.getPayload().toString();
        }
        return msgTxt;
    }
}
