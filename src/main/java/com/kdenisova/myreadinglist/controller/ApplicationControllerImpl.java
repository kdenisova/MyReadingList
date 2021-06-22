package com.kdenisova.myreadinglist.controller;

import com.kdenisova.myreadinglist.model.BookModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class ApplicationControllerImpl implements ApplicationController {

    private static final Logger log = LogManager.getLogger(ApplicationControllerImpl.class);

    private final ApiClient apiClient;
    private final ReadingList readingList;

    public ApplicationControllerImpl(ApiClient apiClient, ReadingList readingList) {
        this.apiClient = apiClient;
        this.readingList = readingList;
    }

    @Override
    public List<BookModel> search(String query) throws ApplicationControllerException {
        try {
            return apiClient.search(query);
        } catch (IOException e) {
            log.error(e);
            throw new ApplicationControllerException("Error occurred while searching for the book", e);
        }
    }

    @Override
    public void saveToReadingList(BookModel book) throws ApplicationControllerException {
        try {
            readingList.save(book);
        } catch (IOException e) {
            log.error(e);
            throw new ApplicationControllerException("Error occurred while saving Book to the Reading List", e);
        }
    }

    @Override
    public List<BookModel> getReadingList() throws ApplicationControllerException {
        try {
            return readingList.getAll();
        } catch (IOException | BookFormatterException e) {
            log.error(e);
            throw new ApplicationControllerException("Error occurred while trying to access the reading list", e);
        }
    }
}
