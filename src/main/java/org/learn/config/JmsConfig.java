package org.learn.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;


@Configuration
@EnableJms
public class JmsConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;
    @Value("${spring.activemq.user}")
    private String brokerUsername;
    @Value("${spring.activemq.password}")
    private String brokerPassword;

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
        return getJmsListener(connectionFactory, false, false, "q-client");
    }

    @Bean
    public DefaultJmsListenerContainerFactory topicListenerFactoryDurable(ActiveMQConnectionFactory connectionFactory) {
        return getJmsListener(connectionFactory, true, true, "t-durable-client");
    }

    @Bean
    public DefaultJmsListenerContainerFactory topicListenerFactoryNonDurable(ActiveMQConnectionFactory connectionFactory) {
        return getJmsListener(connectionFactory, true, false, "t-non-durable-client");
    }

    private DefaultJmsListenerContainerFactory getJmsListener(ActiveMQConnectionFactory connectionFactory, boolean isPubSubDomain, boolean isSubscriptionDurable, String clientId) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(isPubSubDomain);
        factory.setSubscriptionDurable(isSubscriptionDurable);
        factory.setClientId(clientId);
        return factory;
    }

}
