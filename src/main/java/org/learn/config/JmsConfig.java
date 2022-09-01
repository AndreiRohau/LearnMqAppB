package org.learn.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateRequeueAmqpException;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import static java.lang.Boolean.TRUE;

@Slf4j
@Configuration
public class JmsConfig {
    private static final String HEADER_NAME_AMQP_REDELIVERED = "amqp_redelivered";
    @Value("${rmq.declare.consumer.name.1}")
    private String consumerName;
    @Value("${rmq.declare.consumer.name.2}")
    private String consumerNameAnother;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "${rmq.declare.queue.1}", durable = "true"),
                    key = "${rmq.declare.routing.key.1}",
                    exchange = @Exchange(name = "${rmq.declare.exchange}", type = ExchangeTypes.TOPIC))
    )
    public void listenQueue1(Message<String> in) {
        doListen(consumerName, in);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "${rmq.declare.queue.2}", durable = "true"),
                    key = "${rmq.declare.routing.key.2}",
                    exchange = @Exchange(name = "${rmq.declare.exchange}", type = ExchangeTypes.TOPIC))
    )
    public void listenQueue2(Message<String> in) {
        doListen(consumerNameAnother, in);
    }

    private static void doListen(String consumerName, Message<String> in) {
        log.info(consumerName +
                ", headers=" + in.getHeaders() +
                ", payload=" + in.getPayload());

        boolean isAmpqRedelivered = TRUE.equals(in.getHeaders().get(HEADER_NAME_AMQP_REDELIVERED, Boolean.class));
        if (isAmpqRedelivered) {
            // when we receive message SECOND TIME it has redelivered=true
            // then we discard processing this message
            throw new AmqpRejectAndDontRequeueException("return and discard message");
        }
        // when received message FIRST TIME by default redelivered=false
        // then we kick it back with redelivered=true
        throw new ImmediateRequeueAmqpException("return with re-queue=true");
    }
}
