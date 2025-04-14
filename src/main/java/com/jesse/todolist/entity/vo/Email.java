package com.jesse.todolist.entity.vo;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Email value object.
 * Represents an email address and ensures its validity.
 * This is used with EmailConverter for JPA storage.
 */
public class Email {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    
    private final String value;
    
    public Email(String value) {
        validate(value);
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    private void validate(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value.toLowerCase(), email.value.toLowerCase());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value.toLowerCase());
    }
    
    @Override
    public String toString() {
        return value;
    }
}
