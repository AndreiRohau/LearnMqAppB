package org.learn.jms;

import lombok.extern.slf4j.Slf4j;
import org.learn.controller.MainController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class QueueListener {
    @Value("${activemq.queue}")
    private String queue;
    @Value("${activemq.response.enabled}")
    private boolean isResponseEnabled;
    @Value("${activemq.response.delay}")
    private int delaySeconds;

    @Autowired
    private JmsTemplate jmsResponseQueueTemplate;

    @JmsListener(destination = "${activemq.queue}", containerFactory = "queueListenerFactory")
    public void receiveMessageFromQueue(Message message) throws JMSException, InterruptedException {
        TextMessage textMessage = (TextMessage) message;
        String messageData = textMessage.getText();
        log.info("RECEIVED==={}. Q={}.", messageData, queue);
        processResponse(message, messageData);
    }

    private void processResponse(Message message, String messageData) throws InterruptedException, JMSException {
        if (isResponseEnabled) {
            StringBuilder sb = processMessageData(messageData);
            log.info("PROCESSED==={}. Q={}.", sb.toString(), queue);

            jmsResponseQueueTemplate.convertAndSend(message.getJMSReplyTo(), sb.toString());
            log.info("REPLY==={}. Q={}.", sb.toString(), message.getJMSReplyTo());
            MainController.CACHE.add(sb.toString());
        }
    }

    private StringBuilder processMessageData(String messageData) throws InterruptedException {
        TimeUnit.SECONDS.sleep(delaySeconds);
        StringBuilder sb = new StringBuilder(messageData);
        sb.reverse();
        return sb;
    }
}
