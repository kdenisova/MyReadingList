package com.kdenisova.myreadinglist.controller;

import com.kdenisova.myreadinglist.model.BookModel;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public abstract class AbstractApiClient implements ApiClient {

    private static final Logger log = LogManager.getLogger(AbstractApiClient.class);

    private final UriBuilder uriBuilder;
    private final ApiResponseMapper apiResponseMapper;

    protected AbstractApiClient(UriBuilder uriBuilder, ApiResponseMapper apiResponseMapper) {
        this.uriBuilder = uriBuilder;
        this.apiResponseMapper = apiResponseMapper;
    }

    /** Allows to modify request before executing it
     * @param httpClient instance of HttpClient
     * @param httpGet instance of HttGet request
     * @implNote base implementation does nothing
     */
    protected void preProcess(HttpClient httpClient, HttpGet httpGet) {
    }

    /** Allows to modify API response before passing it to apiResponseMapper
     * @param httpEntity API entity returned in response body
     * @implNote base implementation does nothing
     */
    protected void postProcess(HttpEntity httpEntity) {
    }

    @Override
    public List<BookModel> search(String query) throws IOException {
        URI uri = uriBuilder.build(query);

        log.debug("calling API {}", uri);

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = null;

        try {
            HttpGet httpGet = new HttpGet(uri);

            preProcess(httpClient, httpGet);

            response = httpClient.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new IOException("Status received is other than a 200:OK");
            }

            log.debug("received a {} response from API", statusCode);

            HttpEntity entity = response.getEntity();

            postProcess(entity);

            String jsonString = EntityUtils.toString(entity);

            log.debug("response output {}", jsonString);

            EntityUtils.consume(entity);

            return apiResponseMapper.fromJson(new JSONObject(jsonString));

        } catch (Exception e) {
            log.error(e);
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
