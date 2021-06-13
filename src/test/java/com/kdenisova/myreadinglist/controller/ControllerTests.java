package com.kdenisova.myreadinglist.controller;

import com.kdenisova.myreadinglist.model.BookModel;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ControllerTests {

    @Test
    public void GoogleBooksApiThrowsException_ThrowsApplicationControllerException() throws IOException {
        // Arrange
        GoogleBooksApiClient mockedGoogleBooksApiClient = mock(GoogleBooksApiClient.class);
        when(mockedGoogleBooksApiClient.search(any())).thenThrow(new IOException());

        ApplicationControllerImpl controller = new ApplicationControllerImpl(mockedGoogleBooksApiClient, null);

        // Act & Assert
        Assertions.assertThrows(ApplicationControllerException.class, () -> controller.search("some_query"));
    }

    @Test
    public void WhenMakingBooksSearch_CallsGoogleApiClientOnlyOnce() throws IOException, ApplicationControllerException {
        // Arrange
        GoogleBooksApiClient mockedGoogleBooksApiClient = mock(GoogleBooksApiClient.class);
        when(mockedGoogleBooksApiClient.search(any()))
                .thenReturn(new JSONObject("{ \"items\": [ { \"volumeInfo\": { \"title\": \"\" } } ] }"));

        ApplicationControllerImpl controller = new ApplicationControllerImpl(mockedGoogleBooksApiClient, null);

        // Act
        controller.search("query");

        // Assert
        verify(mockedGoogleBooksApiClient, times(1)).search(eq("query"));
    }

    @Test
    public void GivenCorrectEmptyJson_ReturnsEmpty() throws ApplicationControllerException, IOException {
        // Arrange
        final String booksSearchQuery = "Harry Potter";

        GoogleBooksApiClient mockedGoogleBooksApiClient = mock(GoogleBooksApiClient.class);
        when(mockedGoogleBooksApiClient.search(booksSearchQuery))
                .thenReturn(new JSONObject("{ \"items\": [ ] }"));

        ApplicationControllerImpl controller = new ApplicationControllerImpl(mockedGoogleBooksApiClient, null);

        // Act
        List<BookModel> search = controller.search(booksSearchQuery);

        // Assert
        Assertions.assertEquals(search.size(), 0);
    }

    @Test
    public void GivenCorrectJson_ReturnsBooksWithOptionalPublisher() throws ApplicationControllerException, IOException {
        // Arrange
        final String booksSearchQuery = "Harry Potter";
        final String aBookTitle = "Harry Potter and the Art of Unit Testing";

        GoogleBooksApiClient mockedGoogleBooksApiClient = mock(GoogleBooksApiClient.class);
        when(mockedGoogleBooksApiClient.search(booksSearchQuery))
                .thenReturn(new JSONObject(String.format("{ \"items\": [ { \"volumeInfo\": { \"title\": \"%s\" } } ] }", aBookTitle)));

        ApplicationControllerImpl controller = new ApplicationControllerImpl(mockedGoogleBooksApiClient, null);

        // Act
        List<BookModel> search = controller.search(booksSearchQuery);

        // Assert
        Assertions.assertEquals(search.size(), 1);
        Assertions.assertEquals(search.get(0).getTitle(), aBookTitle);
    }

    @Test
    public void GivenCorrectJson_ReturnsBooksWithPublisher() throws ApplicationControllerException, IOException {
        // Arrange
        final String booksSearchQuery = "Harry Potter";
        final String aBookTitle = "Harry Potter and the Art of Unit Testing";
        final String aBookPublisher = "Scholastic";

        GoogleBooksApiClient mockedGoogleBooksApiClient = mock(GoogleBooksApiClient.class);
        when(mockedGoogleBooksApiClient.search(booksSearchQuery))
                .thenReturn(
                        new JSONObject(
                                String.format("{ \"items\": [ { \"volumeInfo\": { \"title\": \"%s\", \"publisher\": \"%s\" } } ] }",
                                        aBookTitle,
                                        aBookPublisher)));

        ApplicationControllerImpl controller = new ApplicationControllerImpl(mockedGoogleBooksApiClient, null);

        // Act
        List<BookModel> search = controller.search(booksSearchQuery);

        // Assert
        Assertions.assertEquals(search.size(), 1);
        Assertions.assertEquals(search.get(0).getTitle(), aBookTitle);
        Assertions.assertEquals(search.get(0).getPublisher(), aBookPublisher);
    }

    @Test
    public void GivenCorrectJson_ReturnsBooksWithAuthors() throws ApplicationControllerException, IOException {
        // Arrange
        final String booksSearchQuery = "Harry Potter";
        final String aBookTitle = "Harry Potter and the Art of Unit Testing";
        final String aBookAuthor = "J.K. Rowling";

        GoogleBooksApiClient mockedGoogleBooksApiClient = mock(GoogleBooksApiClient.class);
        when(mockedGoogleBooksApiClient.search(booksSearchQuery))
                .thenReturn(new JSONObject(
                        String.format(
                                "{ \"items\": [ { \"volumeInfo\": { \"title\": \"%s\", \"authors\": [ \"%s\" ] } } ] }",
                                aBookTitle,
                                aBookAuthor)));

        ApplicationControllerImpl controller = new ApplicationControllerImpl(mockedGoogleBooksApiClient, null);

        // Act
        List<BookModel> search = controller.search(booksSearchQuery);

        // Assert
        Assertions.assertEquals(search.size(), 1);
        Assertions.assertEquals(search.get(0).getTitle(), aBookTitle);
        Assertions.assertEquals(search.get(0).getAuthors().size(), 1);
        Assertions.assertEquals(search.get(0).getAuthors().get(0), aBookAuthor);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{ }",
            "{ \"data\": [ ] }",
            "{ \"items\": [ { \"volume\": {  } } ] }",
            "{ \"items\": [ { \"volumeInfo\": [ { \"title1\": \"\" } ] } ] }"
    })
    public void GivenIncorrectJson_ThrowsApplicationControllerException(String json) throws IOException {
        // Arrange
        GoogleBooksApiClient mockedGoogleBooksApiClient = mock(GoogleBooksApiClient.class);
        when(mockedGoogleBooksApiClient.search(any()))
                .thenReturn(new JSONObject(json));

        ApplicationControllerImpl controller = new ApplicationControllerImpl(mockedGoogleBooksApiClient, null);

        // Act & Assert
        ApplicationControllerException exception = Assertions.assertThrows(ApplicationControllerException.class, () -> controller.search(any()));
        Assertions.assertEquals("Error occurred while processing the search results", exception.getMessage());
    }

    @Test
    public void WhenGettingReadingList_CallsReadingListOnlyOnce() throws BookFormatterException, IOException, ApplicationControllerException {
        // Arrange
        ReadingList mockedReadingList = mock(ReadingList.class);
        when(mockedReadingList.getAll()).thenReturn(new ArrayList<>());

        ApplicationControllerImpl controller = new ApplicationControllerImpl(null, mockedReadingList);

        // Act
        controller.getReadingList();

        // Assert
        verify(mockedReadingList, times(1)).getAll();
    }

    @Test
    public void WhenGettingReadingList_RethrowsException() throws BookFormatterException, IOException {
        // Arrange
        ReadingList mockedReadingList = mock(ReadingList.class);
        when(mockedReadingList.getAll()).thenThrow(new BookFormatterException("malformed!"));

        ApplicationControllerImpl controller = new ApplicationControllerImpl(null, mockedReadingList);

        // Act & Assert
        Assertions.assertThrows(ApplicationControllerException.class, controller::getReadingList);
    }

    @Test
    public void WhenSavingToReadingList_CallsReadingListOnlyOnce() throws IOException, ApplicationControllerException {
        // Arrange
        ReadingList mockedReadingList = mock(ReadingList.class);

        ApplicationControllerImpl controller = new ApplicationControllerImpl(null, mockedReadingList);

        // Act
        controller.saveToReadingList(any());

        // Assert
        verify(mockedReadingList, times(1)).save(any());
    }

    @Test
    public void WhenSavingToReadingList_RethrowsException() throws IOException {
        // Arrange
        ReadingList mockedReadingList = mock(ReadingList.class);
        doAnswer((Answer<Void>) invocation -> {
            throw new IOException("FNF");
        }).when(mockedReadingList).save(null);

        ApplicationControllerImpl controller = new ApplicationControllerImpl(null, mockedReadingList);

        // Act & Assert
        Assertions.assertThrows(ApplicationControllerException.class, () -> controller.saveToReadingList(any()));
    }
}
