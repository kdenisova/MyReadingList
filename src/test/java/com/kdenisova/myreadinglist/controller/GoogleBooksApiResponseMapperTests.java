package com.kdenisova.myreadinglist.controller;

import com.kdenisova.myreadinglist.model.BookModel;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

public class GoogleBooksApiResponseMapperTests {

    @Test
    public void GivenCorrectEmptyJson_ReturnsEmpty() {
        // Arrange
        ApiResponseMapper mapper = new GoogleBooksApiResponseMapperImpl();

        // Act
        List<BookModel> bookModels = mapper.fromJson(new JSONObject("{ \"items\": [ ] }"));

        // Assert
        Assertions.assertEquals(bookModels.size(), 0);
    }

    @Test
    public void GivenCorrectJson_ReturnsBooksWithOptionalPublisher() {
        // Arrange
        final String aBookTitle = "Harry Potter and the Art of Unit Testing";

        ApiResponseMapper mapper = new GoogleBooksApiResponseMapperImpl();

        // Act
        List<BookModel> bookModels = mapper.fromJson(new JSONObject(String.format("{ \"items\": [ { \"volumeInfo\": { \"title\": \"%s\" } } ] }", aBookTitle)));

        // Assert
        Assertions.assertEquals(bookModels.size(), 1);
        Assertions.assertEquals(bookModels.get(0).getTitle(), aBookTitle);
    }

    @Test
    public void GivenCorrectJson_ReturnsBooksWithPublisher() {
        // Arrange
        final String aBookTitle = "Harry Potter and the Art of Unit Testing";
        final String aBookPublisher = "Scholastic";

        ApiResponseMapper mapper = new GoogleBooksApiResponseMapperImpl();

        // Act
        List<BookModel> bookModels = mapper.fromJson(new JSONObject(
                String.format("{ \"items\": [ { \"volumeInfo\": { \"title\": \"%s\", \"publisher\": \"%s\" } } ] }",
                        aBookTitle,
                        aBookPublisher)));

        // Assert
        Assertions.assertEquals(bookModels.size(), 1);
        Assertions.assertEquals(bookModels.get(0).getTitle(), aBookTitle);
        Assertions.assertEquals(bookModels.get(0).getPublisher(), aBookPublisher);
    }

    @Test
    public void GivenCorrectJson_ReturnsBooksWithAuthors() {
        // Arrange
        final String aBookTitle = "Harry Potter and the Art of Unit Testing";
        final String aBookAuthor = "J.K. Rowling";

        ApiResponseMapper mapper = new GoogleBooksApiResponseMapperImpl();

        // Act
        List<BookModel> bookModels = mapper.fromJson(new JSONObject(
                String.format(
                        "{ \"items\": [ { \"volumeInfo\": { \"title\": \"%s\", \"authors\": [ \"%s\" ] } } ] }",
                        aBookTitle,
                        aBookAuthor)));

        // Assert
        Assertions.assertEquals(bookModels.size(), 1);
        Assertions.assertEquals(bookModels.get(0).getTitle(), aBookTitle);
        Assertions.assertEquals(bookModels.get(0).getAuthors().size(), 1);
        Assertions.assertEquals(bookModels.get(0).getAuthors().get(0), aBookAuthor);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{ }",
            "{ \"data\": [ ] }",
            "{ \"items\": [ { \"volume\": {  } } ] }",
            "{ \"items\": [ { \"volumeInfo\": [ { \"title1\": \"\" } ] } ] }"
    })
    public void GivenIncorrectJson_ThrowsApplicationControllerException(String json) {
        // Arrange
        ApiResponseMapper mapper = new GoogleBooksApiResponseMapperImpl();

        // Act & Assert
        Assertions.assertThrows(JSONException.class, () -> mapper.fromJson(new JSONObject(json)));
    }
}
