package com.kdenisova.myreadinglist.controller;

import com.kdenisova.myreadinglist.model.BookModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BookFormatterTests {
    @Test
    public void ValidBooks_ValidFormattedOutput() {
        // Arrange
        BookFormatter formatter = new BookFormatterImpl();
        List<BookModel> bookModelList = Arrays.asList(
                new BookModel(new ArrayList<>(), "", ""),
                new BookModel(Collections.singletonList("J.K. Rowling"), "Harry Potter", "Scholastic"),
                new BookModel(new ArrayList<>(), "Harry Potter", ""),
                new BookModel(new ArrayList<>(), "Harry Potter", "Scholastic"),
                new BookModel(Arrays.asList("J.K. Rowling", "JK Rowling"), "Harry Potter", ""),
                new BookModel(Arrays.asList("J.K. Rowling", "JK Rowling"), "Harry Potter", "Scholastic")
        );

        List<String> bookModelStrings = Arrays.asList(
                "|||",
                "Harry Potter|J.K. Rowling;|Scholastic|",
                "Harry Potter|||",
                "Harry Potter||Scholastic|",
                "Harry Potter|J.K. Rowling;JK Rowling;||",
                "Harry Potter|J.K. Rowling;JK Rowling;|Scholastic|"
        );

        // Act
        List<String> bookModelStringResults = new ArrayList<>();
        for (BookModel model : bookModelList) {
            bookModelStringResults.add(formatter.toString(model));
        }

        // Assert
        for (int i = 0; i < bookModelStrings.size(); i++) {
            Assertions.assertEquals(bookModelStrings.get(i), bookModelStringResults.get(i));
        }
    }

    @Test
    public void ValidRoundTrip() throws BookFormatterException {
        // Arrange
        BookFormatter formatter = new BookFormatterImpl();
        BookModel bookModel = new BookModel(Collections.singletonList("J.K. Rowling"), "Harry Potter", "Scholastic");

        // Act
        String output = formatter.toString(bookModel);
        BookModel outputBookModel = formatter.fromString(output);

        // Assert
        Assertions.assertEquals(bookModel.getTitle(), outputBookModel.getTitle());
        Assertions.assertEquals(bookModel.getPublisher(), outputBookModel.getPublisher());
        Assertions.assertEquals(bookModel.getAuthors(), outputBookModel.getAuthors());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            " ",
            "   ",
            "      ",
            "         ",
            "            *",
            "***",
            "//",
            ".",
            "|",
            "|-",
            ";|"
    })
    public void InvalidInput_ThrowsBookFormatterException(String input) {
        // Arrange
        BookFormatter formatter = new BookFormatterImpl();

        // Act & Assert
        Assertions.assertThrows(BookFormatterException.class, () -> formatter.fromString(input));
    }
}
