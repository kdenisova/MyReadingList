package com.kdenisova.myreadinglist.controller;

public class ApplicationControllerException extends Exception {

    public ApplicationControllerException(String message) {
        super(message);
    }

    public ApplicationControllerException(String message, Exception innerException) {
        super(message, innerException);
    }
}
