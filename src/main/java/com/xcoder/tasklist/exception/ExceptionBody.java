package com.xcoder.tasklist.exception;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ExceptionBody {
    private String message;
    private Map<String, String> errors;

    public ExceptionBody(String message) {
        this.message = message;
    }

    public ExceptionBody(String message, Map<String, String> errors) {
        this.message = message;
        this.errors = errors;
    }
}
