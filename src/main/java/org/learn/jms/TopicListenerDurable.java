package org.learn.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import static org.learn.jms.Destination.TOPIC;

@Slf4j
@Component
public class TopicListenerDurable {
    @Value("${activemq.topic}")
    private String topic;

    @JmsListener(destination = "${activemq.topic}", containerFactory = "topicListenerFactoryDurable")
    public void receiveMessageFromTopic(Message message) throws JMSException {
        TextMessage textMessage = (TextMessage) message;
        String messageData = textMessage.getText();
        log.info("RECEIVED: [DURABLE]. MSG=[{}]. T=[{}].", messageData, topic);
        TOPIC.riseProcessed();
        TOPIC.append("[T_+D] - " + messageData);
    }
}
