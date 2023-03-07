package com.company.socialmedia.backend.exception;

public class ResourceNotFoundException extends RuntimeException{

    public static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String msg) {
        super(msg);
    }

}
