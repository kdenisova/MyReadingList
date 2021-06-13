package com.kdenisova.myreadinglist.controller;

import com.kdenisova.myreadinglist.model.BookModel;

import java.io.IOException;
import java.util.List;

public interface ReadingList {

    void save(BookModel book) throws IOException;

    List<BookModel> getAll() throws IOException, BookFormatterException;

}
