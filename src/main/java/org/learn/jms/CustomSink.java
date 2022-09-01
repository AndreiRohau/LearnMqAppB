package org.learn.jms;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface CustomSink {
    String QUEUE1 = "input-queue1";
    String QUEUE2 = "input-queue2";
    @Input(QUEUE1)
    SubscribableChannel inputQueue1();
    @Input(QUEUE2)
    SubscribableChannel inputQueue2();
}
