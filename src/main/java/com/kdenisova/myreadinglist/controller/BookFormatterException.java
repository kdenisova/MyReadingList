package com.kdenisova.myreadinglist.controller;

public class BookFormatterException extends Exception {

    public BookFormatterException(String message) {
        super(message);
    }

    public BookFormatterException(String message, Exception innerException) {
        super(message, innerException);
    }
}
