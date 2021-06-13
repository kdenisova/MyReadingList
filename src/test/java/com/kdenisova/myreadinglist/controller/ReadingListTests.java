package com.kdenisova.myreadinglist.controller;

import com.kdenisova.myreadinglist.model.BookModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ReadingListTests {
    @Test
    public void ValidRoundTrip() throws IOException, BookFormatterException {
        // Slightly more integration than Unit

        // Arrange
        File f = new File("reading_list.data");
        if (f.exists()) {
            f.delete();
        }

        BookModel bookModel = new BookModel(
                Collections.singletonList("J.K. Rowling"),
                "Harry Potter",
                "Scholastic");

        BookFormatter mockedBookFormatter = mock(BookFormatter.class);
        when(mockedBookFormatter.toString(bookModel)).thenReturn("<data>");
        when(mockedBookFormatter.fromString("<data>")).thenReturn(bookModel);

        ReadingListImpl readingList = new ReadingListImpl(mockedBookFormatter,"reading_list.data");

        // Act
        readingList.save(bookModel);
        List<BookModel> allBooks = readingList.getAll();

        // Assert
        Assertions.assertEquals(allBooks.size(), 1);
        Assertions.assertEquals(allBooks.get(0).getTitle(), bookModel.getTitle());
        Assertions.assertEquals(allBooks.get(0).getAuthors(), bookModel.getAuthors());
        Assertions.assertEquals(allBooks.get(0).getPublisher(), bookModel.getPublisher());
    }
}