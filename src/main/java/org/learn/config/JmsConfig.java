package org.learn.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class JmsConfig {

	/*@StreamListener("testSource-in-0")
	public void processMessage(String message) {
		queue1Sink().accept(message);
	}*/

    @Bean
    public Consumer<String> queue1Sink() {
        return payload -> log.info("Consumer1: " + payload);
    }

    @Bean
    public Consumer<String> queue2Sink() {
        return payload -> log.info("Consumer2: " + payload);
    }
}
