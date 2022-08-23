package org.learn.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Slf4j
@Component
public class TopicListener2 {
    @Value("${activemq.topic}")
    private String topic;

    @JmsListener(destination = "${activemq.topic}", containerFactory = "topicListenerFactory")
    public void receiveMessageFromTopic(Message message) throws JMSException {
        TextMessage textMessage = (TextMessage) message;
        String messageData = textMessage.getText();
        log.info("Received message: " + messageData + ". From topic: " + topic + ". By subscriber 2.");
    }
}
