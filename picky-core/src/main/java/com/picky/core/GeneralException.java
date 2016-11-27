package com.picky.core;

public class GeneralException extends RuntimeException {

    public GeneralException(String message) {
        super(message);
    }

    public GeneralException(String message, Throwable e) {
        super(message, e);
    }

    public GeneralException(Throwable e) {
        super(e);
    }

}
