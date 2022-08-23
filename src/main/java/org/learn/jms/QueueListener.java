package org.learn.jms;

import lombok.extern.slf4j.Slf4j;
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
    private boolean isSendResponse;
    @Value("${activemq.response.delay}")
    private int delaySeconds;
    @Value("${activemq.response.queue}")
    private String responseQueueName;

    @Autowired
    private JmsTemplate jmsResponseQueueTemplate;

    @JmsListener(destination = "${activemq.queue}", containerFactory = "queueListenerFactory")
    public void receiveMessageFromQueue(Message message) throws JMSException, InterruptedException {
        TextMessage textMessage = (TextMessage) message;
        String messageData = textMessage.getText();
        log.info("Received message: " + messageData + ". From queue: " + queue);
        if (isSendResponse) {
            TimeUnit.SECONDS.sleep(delaySeconds);
            StringBuilder sb = new StringBuilder(messageData);
            sb.reverse();
            jmsResponseQueueTemplate.convertAndSend(responseQueueName, sb.toString());
            log.info("Processed message: " + sb.toString() + ". From queue: " + queue);
        }
    }
}
