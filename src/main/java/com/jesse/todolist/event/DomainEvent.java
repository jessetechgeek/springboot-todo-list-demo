package com.jesse.todolist.event;

import java.time.LocalDateTime;

/**
 * Base class for all domain events in the system.
 */
public abstract class DomainEvent {
    
    private final LocalDateTime occurredOn;
    
    protected DomainEvent() {
        this.occurredOn = LocalDateTime.now();
    }
    
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
}
