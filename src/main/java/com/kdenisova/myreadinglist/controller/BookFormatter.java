package com.kdenisova.myreadinglist.controller;

import com.kdenisova.myreadinglist.model.BookModel;

public interface BookFormatter {

    BookModel fromString(String s) throws BookFormatterException;

    String toString(BookModel book);
}
