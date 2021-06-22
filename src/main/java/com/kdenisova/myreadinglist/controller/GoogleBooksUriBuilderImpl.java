package com.kdenisova.myreadinglist.controller;

import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GoogleBooksUriBuilderImpl implements UriBuilder {

    private final int MaxResultsReturned = 5;

    @Override
    public URI build(String query) {
        URI uri = null;

        try {
            uri = new URIBuilder()
                    .setScheme("https")
                    .setHost("www.googleapis.com")
                    .setPath("/books/v1/volumes")
                    .setParameter("q", URLEncoder.encode(query, StandardCharsets.UTF_8))
                    .setParameter("maxResults", String.valueOf(MaxResultsReturned))
                    .build();
        } catch (URISyntaxException ignored) {
            // ignored as always correct
        }

        return uri;
    }
}
