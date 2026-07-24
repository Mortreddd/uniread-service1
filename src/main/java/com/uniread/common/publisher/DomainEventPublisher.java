package com.uniread.common.publisher;

import org.springframework.context.ApplicationEventPublisher;

public interface DomainEventPublisher {
    void publish(Object event);
}
