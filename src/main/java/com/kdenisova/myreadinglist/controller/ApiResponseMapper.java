package com.kdenisova.myreadinglist.controller;

import com.kdenisova.myreadinglist.model.BookModel;
import org.json.JSONObject;

import java.util.List;

public interface ApiResponseMapper {

    List<BookModel> fromJson(JSONObject jsonObject);
}
