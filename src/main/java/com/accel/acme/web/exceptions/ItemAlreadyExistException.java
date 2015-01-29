package com.accel.acme.web.exceptions;

public class ItemAlreadyExistException extends Exception {

    private static final long serialVersionUID = 1L;

    public ItemAlreadyExistException(String message) {
        super(message);
    }
}
