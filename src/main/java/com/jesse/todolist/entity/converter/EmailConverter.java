package com.jesse.todolist.entity.converter;

import com.jesse.todolist.entity.vo.Email;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA converter for Email value object.
 * Handles conversion between the Email value object and String representation for database storage.
 */
@Converter(autoApply = true)
public class EmailConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email email) {
        return email != null ? email.getValue() : null;
    }

    @Override
    public Email convertToEntityAttribute(String dbData) {
        try {
            return dbData != null && !dbData.isEmpty() ? new Email(dbData) : null;
        } catch (IllegalArgumentException e) {
            // Handle invalid email in database with a fallback
            return null;
        }
    }
}
