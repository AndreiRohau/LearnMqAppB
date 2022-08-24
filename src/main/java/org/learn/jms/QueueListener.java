package org.learn.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.concurrent.TimeUnit;

@Slf4j
public class QueueListener {
    private String qid;

    @Value("${activemq.queue}")
    private String queueName;

    public QueueListener(String qid) {
        this.qid = qid;
    }

    @JmsListener(destination = "Consumer.MyCustomConsumer.VirtualTopic.MY-SUPER-TOPIC", containerFactory = "queueListenerFactory")
    public void receiveMessageFromQueue(Message message) throws JMSException {
        TextMessage textMessage = (TextMessage) message;
        String messageData = textMessage.getText();
        log.info(qid + " - Received message: " + messageData + ". From queueName: " + queueName);
    }

    @Override
    public String toString() {
        return "QueueListener{" +
                "qid='" + qid + '\'' +
                ", queueName='" + queueName + '\'' +
                '}';
    }
}
