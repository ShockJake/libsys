package com.university.libsys.backend.utils;

import org.thymeleaf.util.StringUtils;

import javax.validation.ValidationException;
import java.util.Map;
import java.util.Optional;

public class ValidationUtil {

    public static void validateFields(Map<String, String> fields) throws ValidationException {
        fields.entrySet().stream()
                .map(entry -> {
                    String value = Optional.ofNullable(entry.getValue()).orElseThrow(() ->
                            new ValidationException(String.format("Field (%s) cannot be null", entry.getKey())));
                    return Map.entry(entry.getKey(), value);
                })
                .forEach(entry -> {
                    if (StringUtils.isEmptyOrWhitespace(entry.getValue())) {
                        throw new ValidationException(String.format("%s cannot be blank", entry.getKey()));
                    }
                });
    }
}
