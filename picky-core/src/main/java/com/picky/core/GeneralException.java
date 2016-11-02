package com.picky.core;

public class GeneralException extends RuntimeException {

    GeneralException(String message) {
        super(message);
    }

    GeneralException(String message, Throwable e) {
        super(message, e);
    }

    public GeneralException(Throwable e) {
        super(e);
    }

}
