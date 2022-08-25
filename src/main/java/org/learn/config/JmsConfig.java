package org.learn.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

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
    public JmsTemplate jmsResponseQueueTemplate(ActiveMQConnectionFactory connectionFactory) {
        return getJmsTemplate(connectionFactory, false);
    }

    private JmsTemplate getJmsTemplate(ActiveMQConnectionFactory connectionFactory, boolean isPubSumDomain) {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory);
        template.setPubSubDomain(isPubSumDomain);
        return template;
    }

}
