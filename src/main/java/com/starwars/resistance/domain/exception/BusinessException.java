package com.starwars.resistance.domain.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(final String message) {
        super(message);
    }

}
