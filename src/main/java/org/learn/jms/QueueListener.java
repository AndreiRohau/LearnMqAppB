package org.learn.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import static org.learn.jms.Destination.QUEUE;

@Slf4j
@Component
public class QueueListener {
    @Value("${activemq.queue}")
    private String queue;

    @JmsListener(destination = "${activemq.queue}", containerFactory = "queueListenerFactory")
    public void receiveMessageFromQueue(Message message) throws JMSException {
        TextMessage textMessage = (TextMessage) message;
        String messageData = textMessage.getText();
        log.info("RECEIVED: MSG=[{}]. Q=[{}].", messageData, queue);
        QUEUE.riseProcessed();
        QUEUE.append("[Q___] - " + messageData);
    }
}
