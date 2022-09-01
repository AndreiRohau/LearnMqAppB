package org.learn.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.learn.jms.Consumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.learn.config.StringFinals.EXCHANGE_TYPE_TOPIC;

@Configuration
public class JmsConfig {

    @Value("${cloudy.rmq.user}")
    private String user;
    @Value("${cloudy.rmq.password}")
    private String password;
    @Value("${cloudy.rmq.virtual.host}")
    private String virtualHost;
    @Value("${cloudy.rmq.host}")
    private String host;
    @Value("${cloudy.rmq.port}")
    private int port;

    @Value("${rmq.declare.exchange}")
    private String exchangeName;
    @Value("${rmq.declare.routing.key}")
    private String routingKey;
    @Value("${rmq.declare.queue}")
    private String queueName;
    @Value("${rmq.declare.consumer.name}")
    private String consumerName;
    @Value("${rmq.declare.consumer.name}")
    private String consumerTag;

    @Bean
    public Connection connection() {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setUsername(user);
            connectionFactory.setPassword(password);
            connectionFactory.setVirtualHost(virtualHost);
            connectionFactory.setHost(host);
            connectionFactory.setPort(port);
            Connection connection = connectionFactory.newConnection();
            declareResources(connection);
            declareConsumer(connection);
            return connection;
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private void declareConsumer(Connection connection) {
        try {
            final Channel channel = connection.createChannel();
            Consumer consumer = new Consumer(channel, consumerName);
            channel.basicConsume(queueName, false, consumerTag, consumer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void declareResources(Connection connection) {
        try (Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(exchangeName, EXCHANGE_TYPE_TOPIC, true);
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, routingKey);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException("#channel(Connection connection). " + e);
        }
    }

}
