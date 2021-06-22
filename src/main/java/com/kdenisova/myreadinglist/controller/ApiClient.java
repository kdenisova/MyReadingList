package com.kdenisova.myreadinglist.controller;

import com.kdenisova.myreadinglist.model.BookModel;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public interface ApiClient {

    List<BookModel> search(String query) throws IOException;
}
