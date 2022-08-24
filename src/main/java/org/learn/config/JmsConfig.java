package org.learn.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.learn.example.RequestHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import javax.jms.*;


@Configuration
@EnableJms
public class JmsConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;
    @Value("${spring.activemq.user}")
    private String brokerUsername;
    @Value("${spring.activemq.password}")
    private String brokerPassword;

    @Value("${activemq.transacted}")
    private boolean transacted;
    @Value("${activemq.ackMode}")
    private int ackMode;
    @Value("${activemq.client.queue.name}")
    private String clientMqName;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setUserName(brokerUsername);
        connectionFactory.setPassword(brokerPassword);
        return connectionFactory;
    }

    @Bean
    public Session session() throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(transacted, ackMode);
        return session;
    }

    @Bean
    public MessageProducer replyProducer(Session session) throws JMSException {
        MessageProducer replyProducer = session.createProducer(null);
        replyProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        return replyProducer;
    }

    @Bean
    public MessageConsumer consumer(Session session, RequestHandler requestHandler) throws JMSException {
        Destination adminQueue = session.createQueue(clientMqName);

        MessageConsumer consumer = session.createConsumer(adminQueue);
        consumer.setMessageListener(requestHandler);
        return consumer;
    }

}
