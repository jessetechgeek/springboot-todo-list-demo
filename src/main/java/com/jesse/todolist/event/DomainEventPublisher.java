package com.jesse.todolist.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Event publisher for domain events.
 */
@Component
public class DomainEventPublisher {
    
    private final ApplicationEventPublisher publisher;
    
    public DomainEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
    
    public void publish(DomainEvent event) {
        publisher.publishEvent(event);
    }
}
