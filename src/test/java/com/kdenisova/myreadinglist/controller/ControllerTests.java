package com.kdenisova.myreadinglist.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class ControllerTests {

    @Test
    public void ApiThrowsException_ThrowsApplicationControllerException() throws IOException {
        // Arrange
        ApiClient mockedApiClient = mock(ApiClient.class);
        when(mockedApiClient.search(any())).thenThrow(new IOException());

        ApplicationControllerImpl controller = new ApplicationControllerImpl(mockedApiClient, null);

        // Act & Assert
        Assertions.assertThrows(ApplicationControllerException.class, () -> controller.search("some_query"));
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
