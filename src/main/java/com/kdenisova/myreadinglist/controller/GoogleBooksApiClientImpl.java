package com.kdenisova.myreadinglist.controller;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GoogleBooksApiClientImpl implements GoogleBooksApiClient {

    private static final Logger log = LogManager.getLogger(GoogleBooksApiClientImpl.class);

    private final int MaxResultsReturned = 5;

    @Override
    public JSONObject search(String query) throws IOException {
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

        log.debug("calling GoogleBooksApi {}", uri);

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = null;

        try {
            HttpGet httpGet = new HttpGet(uri);
            response = httpClient.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new IOException("Status received is other than a 200:OK");
            }

            log.debug("received a {} response from GoogleBooksApi", statusCode);

            HttpEntity entity = response.getEntity();

            String jsonString = EntityUtils.toString(entity);

            log.debug("response output {}", jsonString);

            EntityUtils.consume(entity);

            return new JSONObject(jsonString);

        } catch(Exception e) {
            log.error(e);
            throw e;
        }
        finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
