package com.ihrsachin.ecommerce.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIException extends RuntimeException{
    private String message;

    public APIException(String message) {
        super(message);
        this.message = message;
    }
}
