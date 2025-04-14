package com.jesse.todolist.entity;

import com.jesse.todolist.event.DomainEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to collect domain events during the execution of a business transaction.
 * The service layer will access these events and publish them.
 */
public class DomainEvents {
    
    private static final ThreadLocal<List<DomainEvent>> events = ThreadLocal.withInitial(ArrayList::new);
    
    public static void raise(DomainEvent event) {
        events.get().add(event);
    }
    
    public static List<DomainEvent> getEvents() {
        return events.get();
    }
    
    public static void clear() {
        events.get().clear();
    }
}
