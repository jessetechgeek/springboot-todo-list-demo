package com.jesse.todolist.event.handler;

import com.jesse.todolist.event.TodoItemCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event handler for TodoItemCompletedEvent.
 * This handler logs the event and could perform other actions like sending notifications.
 */
@Component
public class TodoItemCompletedEventHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(TodoItemCompletedEventHandler.class);
    
    @EventListener
    public void handle(TodoItemCompletedEvent event) {
        logger.info("Todo item completed: Item ID={}, Title='{}', by User ID={}",
                event.getTodoItemId(), event.getTitle(), event.getUserId());
        
        // Here you could trigger other actions like:
        // - Send notifications
        // - Update statistics
        // - Trigger achievement systems
        // - Etc.
    }
}
