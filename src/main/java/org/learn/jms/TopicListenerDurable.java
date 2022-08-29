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
public class TopicListenerDurable {
    @Value("${activemq.topic}")
    private String topic;

    @JmsListener(destination = "${activemq.topic}", containerFactory = "topicListenerFactoryDurable")
    public void receiveMessageFromTopic(Message message) throws JMSException {
        TextMessage textMessage = (TextMessage) message;
        String text = textMessage.getText();
        log.info("Received message text: {}. From topic: {}. By subscriber dur 1.", text, topic);
    }
}
