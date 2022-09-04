package org.learn.config;

import lombok.extern.slf4j.Slf4j;
import org.learn.jms.CustomConsumer;
import org.springframework.amqp.core.DeclarableCustomizer;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class JmsConfig {
    @Value("${mq.rabbit.q1}")
    private String q1name;
    @Value("${mq.rabbit.q2}")
    private String q2name;
    @Value("${mq.msg.xDlx}")
    private String xDlx;
    @Value("${mq.msg.xDlRoutingKey}")
    private String xDlRoutingKey;

//  same as below, but not functional approach --- here not sure but check it if u have time
//	@StreamListener("testSource-in-0")
//	public void processMessage(String message) {
//		queue1Sink().accept(message);
//	}

    // wellll, I'm probably need to open a jira ticket in spring-cloud-steam-rabbitmq repository
    @Bean
    @ConditionalOnProperty(name = "republish", havingValue = "true")
    public DeclarableCustomizer declarableCustomizer() {
        return declarable -> {
            if (declarable instanceof Queue) {
                Queue queue = (Queue) declarable;
                if (queue.getName().equals(q1name)
                        || queue.getName().equals(q2name)) {
                    queue.removeArgument(xDlx);
                    queue.removeArgument(xDlRoutingKey);
                }
            }
            return declarable;
        };
    }

    @Bean
    public Consumer<Message<String>> queue1Sink() {
        return new CustomConsumer("Consumer1");
    }

    @Bean
    public Consumer<Message<String>> queue2Sink() {
        return new CustomConsumer("Consumer2");
    }

}
