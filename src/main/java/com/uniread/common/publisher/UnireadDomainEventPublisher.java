package com.uniread.common.publisher;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class UnireadDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher publisher;

    public UnireadDomainEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(Object event) {
        publisher.publishEvent(event);
    }
}
