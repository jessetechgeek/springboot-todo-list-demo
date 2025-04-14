package com.jesse.todolist.config;

import com.jesse.todolist.entity.User;
import com.jesse.todolist.entity.vo.Email;
import com.jesse.todolist.entity.vo.FullName;
import com.jesse.todolist.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Utility to migrate data from old model to new DDD model with value objects.
 * This is needed to handle existing data in the database after model changes.
 */
@Component
public class DataMigrationUtil {

    private static final Logger logger = LoggerFactory.getLogger(DataMigrationUtil.class);
    
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;
    
    @PersistenceContext
    private EntityManager entityManager;

    public DataMigrationUtil(UserRepository userRepository, JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Migrate existing users to use value objects.
     * This runs when the application starts.
     */
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void migrateExistingUsers() {
        try {
            logger.info("Starting data migration for users...");
            
            // Use JDBC directly for more reliable data access
            List<Map<String, Object>> userRecords = jdbcTemplate.queryForList(
                "SELECT id, email, first_name, last_name FROM users"
            );
            
            logger.info("Found {} user records to check", userRecords.size());
            
            int migratedCount = 0;
            for (Map<String, Object> record : userRecords) {
                try {
                    Long userId = ((Number) record.get("id")).longValue();
                    String emailStr = (String) record.get("email");
                    String firstName = (String) record.get("first_name");
                    String lastName = (String) record.get("last_name");
                    
                    // Only process users that need fixing
                    User user = userRepository.findById(userId).orElse(null);
                    if (user != null) {
                        boolean changed = false;
                        
                        // Fix email if needed
                        if (user.getEmail() == null && emailStr != null && !emailStr.isEmpty()) {
                            try {
                                user.setEmail(new Email(emailStr));
                                changed = true;
                            } catch (Exception e) {
                                logger.warn("Invalid email format for user {}: {}", userId, emailStr);
                                user.setEmail(new Email("user" + userId + "@example.com"));
                                changed = true;
                            }
                        }
                        
                        // Fix name if needed
                        if (user.getName() == null && 
                            ((firstName != null && !firstName.isEmpty()) || 
                             (lastName != null && !lastName.isEmpty()))) {
                            user.setName(new FullName(
                                firstName != null ? firstName : "",
                                lastName != null ? lastName : ""
                            ));
                            changed = true;
                        }
                        
                        if (changed) {
                            userRepository.save(user);
                            migratedCount++;
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error migrating user record: {}", e.getMessage());
                }
            }
            
            logger.info("User migration completed: {} users needed fixing", migratedCount);
        } catch (Exception e) {
            logger.error("Error during user migration", e);
        }
    }
}
