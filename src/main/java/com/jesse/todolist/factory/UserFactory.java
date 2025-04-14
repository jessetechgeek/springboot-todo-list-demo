package com.jesse.todolist.factory;

import com.jesse.todolist.entity.User;
import com.jesse.todolist.entity.vo.Email;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Factory for creating and building User domain objects.
 * This separates the creation logic from the domain objects themselves.
 */
@Component
public class UserFactory {

    private final PasswordEncoder passwordEncoder;

    public UserFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a basic User with required fields.
     *
     * @param username the username
     * @param rawPassword the raw password (will be encoded)
     * @param email the email address
     * @return a newly created User with encoded password
     */
    public User createUser(String username, String rawPassword, String email) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        return User.Factory.createUser(username, encodedPassword, email);
    }

    /**
     * Creates a User with all details.
     *
     * @param username the username
     * @param rawPassword the raw password (will be encoded)
     * @param email the email address
     * @param firstName the first name
     * @param lastName the last name
     * @return a newly created User with all details and encoded password
     */
    public User createUser(String username, String rawPassword, String email, String firstName, String lastName) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        return User.Factory.createUser(username, encodedPassword, email, firstName, lastName);
    }

    /**
     * Creates a User with pre-encoded password for system use or migrations.
     * This should generally be avoided in favor of the raw password version.
     *
     * @param username the username
     * @param encodedPassword the already-encoded password
     * @param email the email address
     * @return a newly created User with the provided encoded password
     */
    public User createUserWithEncodedPassword(String username, String encodedPassword, String email) {
        return User.Factory.createUser(username, encodedPassword, email);
    }
}
