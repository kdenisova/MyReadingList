package com.kdenisova.myreadinglist.controller;

import com.kdenisova.myreadinglist.model.BookModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GoogleBooksApiResponseMapperImpl implements ApiResponseMapper {

    @Override
    public List<BookModel> fromJson(JSONObject jsonObject) {
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
}
