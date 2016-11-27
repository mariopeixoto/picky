package com.picky.sample;

public class GeneralException extends RuntimeException {

    GeneralException(String message) {
        super(message + "x");
    }

    GeneralException(String message, Throwable e) {
        super(message, e);
    }

    public GeneralException(Throwable e) {
        super(e);
    }

}
