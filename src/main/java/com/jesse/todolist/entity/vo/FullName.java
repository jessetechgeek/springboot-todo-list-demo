package com.jesse.todolist.entity.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

/**
 * FullName value object.
 * Represents a person's full name with first and last name components.
 */
@Embeddable
public class FullName {
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    // For JPA
    protected FullName() {
    }
    
    public FullName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    protected void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    protected void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getFullName() {
        if (firstName == null && lastName == null) {
            return "";
        }
        
        StringBuilder fullName = new StringBuilder();
        if (firstName != null) {
            fullName.append(firstName);
        }
        
        if (lastName != null) {
            if (fullName.length() > 0) {
                fullName.append(" ");
            }
            fullName.append(lastName);
        }
        
        return fullName.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullName that = (FullName) o;
        return Objects.equals(firstName, that.firstName) && 
               Objects.equals(lastName, that.lastName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }
    
    @Override
    public String toString() {
        return getFullName();
    }
}
