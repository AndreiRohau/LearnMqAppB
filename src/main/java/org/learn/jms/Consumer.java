package org.learn.jms;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class Consumer extends DefaultConsumer {
    private final String consumerName;

    public Consumer(Channel channel, String consumerName) {
        super(channel);
        this.consumerName = consumerName;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String routingKey = envelope.getRoutingKey();
        String contentType = properties.getContentType();
        long deliveryTag = envelope.getDeliveryTag();
        boolean redeliver = envelope.isRedeliver();
        String messageBody = new String(body, StandardCharsets.UTF_8);

        log.info(consumerName +
                ", routingKey=" + routingKey +
                ", contentType=" + contentType +
                ", deliveryTag=" + deliveryTag +
                ", redeliver=" + redeliver +
                ", messageBody=" + messageBody);

        // positive ack
        getChannel().basicAck(deliveryTag, false);

        // nack with re-queue
        // nack and discard
//        getChannel().basicNack(deliveryTag, false, !redeliver);
    }

}
