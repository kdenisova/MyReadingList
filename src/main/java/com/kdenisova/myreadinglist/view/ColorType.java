package com.kdenisova.myreadinglist.view;

public enum ColorType {

    RESET("\033[0m"),
    CYAN("\033[0;36m"),
    WHITE("\033[0;97m");

    private final String code;

    ColorType(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }

}
