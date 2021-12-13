package com.starwars.resistance.domain.exception;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(final String message) {
        super(message);
    }

}
