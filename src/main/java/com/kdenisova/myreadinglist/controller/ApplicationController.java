package com.kdenisova.myreadinglist.controller;

import com.kdenisova.myreadinglist.model.BookModel;
import java.util.List;

public interface ApplicationController {

    List<BookModel> search(String query) throws ApplicationControllerException;

    void saveToReadingList(BookModel book) throws ApplicationControllerException;

    List<BookModel> getReadingList() throws ApplicationControllerException;
}
