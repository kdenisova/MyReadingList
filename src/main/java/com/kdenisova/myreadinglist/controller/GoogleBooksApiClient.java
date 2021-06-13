package com.kdenisova.myreadinglist.controller;

import org.json.JSONObject;

import java.io.IOException;

public interface GoogleBooksApiClient {

    JSONObject search(String query) throws IOException;
}
