package com.transaction.exceptions;

import lombok.Data;

@Data
public class EntityAlreadyExistsException extends Exception {

    private String details ;

    public EntityAlreadyExistsException() {
    }

    public EntityAlreadyExistsException(String message, String details) {
        super(message);
        this.details = details ;
    }

}