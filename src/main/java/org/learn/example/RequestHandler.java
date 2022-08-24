package org.learn.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RequestHandler implements MessageListener {
    @Autowired
    private Session session;
    @Autowired
    private MessageProducer replyProducer;

    @Override
    public void onMessage(Message message) {
        log.info("Server started....");
        try {
            TextMessage response = session.createTextMessage();
            if (message instanceof TextMessage) {
                TextMessage txtMsg = (TextMessage) message;
                String messageText = txtMsg.getText();
                StringBuilder stringBuilder = new StringBuilder(messageText);
                stringBuilder.reverse();
                response.setText(stringBuilder.toString());
                TimeUnit.SECONDS.sleep(10);
                log.info("data: " + stringBuilder.toString());
                log.info("Server finished....\n");
            }
            response.setJMSCorrelationID(message.getJMSCorrelationID());
            replyProducer.send(message.getJMSReplyTo(), response);
        } catch (JMSException e) {
            //Handle the exception appropriately
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
