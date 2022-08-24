package org.learn.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.learn.jms.QueueListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import java.util.UUID;
import java.util.function.Supplier;

@Configuration
@EnableJms
public class JmsConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;
    @Value("${spring.activemq.user}")
    private String brokerUsername;
    @Value("${spring.activemq.password}")
    private String brokerPassword;

    @Value("${activemq.queue}")
    private String queueName;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setUserName(brokerUsername);
        connectionFactory.setPassword(brokerPassword);
        return connectionFactory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory queueListenerFactory(ActiveMQConnectionFactory connectionFactory) {
        return getJmsListener(connectionFactory, false, false, null);
    }

    private DefaultJmsListenerContainerFactory getJmsListener(ActiveMQConnectionFactory connectionFactory, boolean isPubSubDomain, boolean isSubscriptionDurable, String clientId) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(isPubSubDomain);
        factory.setSubscriptionDurable(isPubSubDomain && isSubscriptionDurable);
        factory.setClientId(clientId);
        return factory;
    }

    @Bean
    @Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public QueueListener queueListener() {
        return new QueueListener(UUID.randomUUID().toString());
    }

    @Bean
    public Supplier<QueueListener> queueListenerSupplier() {
        return () -> queueListener();
    }

}
