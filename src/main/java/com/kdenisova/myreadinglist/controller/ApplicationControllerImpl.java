package com.kdenisova.myreadinglist.controller;

import com.kdenisova.myreadinglist.model.BookModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationControllerImpl implements ApplicationController {

    private static final Logger log = LogManager.getLogger(ApplicationControllerImpl.class);

    private final GoogleBooksApiClient googleBooksApiClient;
    private final ReadingList readingList;

    public ApplicationControllerImpl(GoogleBooksApiClient googleBooksApiClient, ReadingList readingList) {
        this.googleBooksApiClient = googleBooksApiClient;
        this.readingList = readingList;
    }

    @Override
    public List<BookModel> search(String query) throws ApplicationControllerException {
        List<BookModel> books;
        JSONObject jsonObject;

        try {
            jsonObject = googleBooksApiClient.search(query);
        } catch (IOException e) {
            log.error(e);
            throw new ApplicationControllerException("Error occurred while searching for the book", e);
        }

        try {
            books = new ArrayList<>(fromJson(jsonObject));
        } catch (JSONException e) {
            log.error(e);
            throw new ApplicationControllerException("Error occurred while processing the search results", e);
        }

        return books;
    }

    private static List<BookModel> fromJson(JSONObject jsonObject) {
        final String ItemsToken = "items";
        final String VolumeInfoToken = "volumeInfo";
        final String AuthorsToken = "authors";
        final String TitleToken = "title";
        final String PublisherToken = "publisher";

        List<BookModel> models = new ArrayList<>();

        if (!jsonObject.has(ItemsToken)) {
            throw new JSONException(String.format("cannot find %s token in JSON", ItemsToken));
        }

        JSONArray itemsArray = jsonObject.getJSONArray(ItemsToken);

        for (int i = 0; i < itemsArray.length(); i++) {
            String title = "";
            String publisher = "";
            List<String> authors = new ArrayList<>();

            JSONObject itemObject = itemsArray.getJSONObject(i);

            if (!itemObject.has(VolumeInfoToken)) {
                throw new JSONException(String.format("expected %s but none was found", VolumeInfoToken));
            }

            if (itemObject.getJSONObject(VolumeInfoToken).has(AuthorsToken)) {
                JSONArray jsonArray = itemObject.getJSONObject(VolumeInfoToken).getJSONArray(AuthorsToken);

                for (int j = 0; j < jsonArray.length(); j++) {
                    authors.add(jsonArray.get(j).toString());
                }
            }

            if (itemObject.getJSONObject(VolumeInfoToken).has(TitleToken)) {
                title = itemObject.getJSONObject(VolumeInfoToken).get(TitleToken).toString().replace("\"", "");
            }

            if (itemObject.getJSONObject(VolumeInfoToken).has(PublisherToken)) {
                publisher = itemObject.getJSONObject(VolumeInfoToken).get(PublisherToken).toString();
            }

            models.add(new BookModel(authors, title, publisher));
        }

        return models;
    }

    @Override
    public void saveToReadingList(BookModel book) throws ApplicationControllerException {
        try {
            readingList.save(book);
        } catch (IOException e) {
            log.error(e);
            throw new ApplicationControllerException("Error while saving Book to the Reading List", e);
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
