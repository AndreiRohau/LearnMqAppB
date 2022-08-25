package org.learn.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import static org.learn.jms.Destination.QUEUE;

@Slf4j
public class QueueListener {
    private String qid;

    @Value("Consumer.${activemq.virtual.queue}.VirtualTopic.${activemq.virtual.topic}")
    private String queueName;

    public QueueListener(String qid) {
        this.qid = qid;
    }

    @JmsListener(destination = "Consumer.${activemq.virtual.queue}.VirtualTopic.${activemq.virtual.topic}", containerFactory = "queueListenerFactory")
    public void receiveMessageFromQueue(Message message) throws JMSException {
        TextMessage textMessage = (TextMessage) message;
        String text = textMessage.getText();
        log.info("RECEIVED: CONSUMER={}, MSG={}, Q={}", qid, text, queueName);
        QUEUE.riseProcessed();
        QUEUE.append("CONSUMER=[" + qid + "], Q=[" + queueName + "], MSG=[" + text + "]");
    }

    @Override
    public String toString() {
        return "QueueListener{" +
                "qid='" + qid + '\'' +
                ", queueName='" + queueName + '\'' +
                '}';
    }
}
