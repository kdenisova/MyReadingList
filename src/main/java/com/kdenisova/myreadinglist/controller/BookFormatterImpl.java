package com.kdenisova.myreadinglist.controller;

import com.kdenisova.myreadinglist.model.BookModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookFormatterImpl implements BookFormatter {

    private static final Logger log = LogManager.getLogger(BookFormatterImpl.class);

    private final char Separator = '|';

    @Override
    public BookModel fromString(String input) throws BookFormatterException {
        BookModel result = new BookModel();

        try {
            log.debug("parsing book input {}", input);

            String[] parts = input.split("\\|", -1);

            result.setTitle(parts[0]);

            List<String> authors = new ArrayList<>();
            result.setAuthors(authors);

            if (!parts[1].isEmpty()) {
                authors.addAll(Arrays.asList(parts[1].split(";")));
            }

            result.setPublisher(parts[2]);

            log.debug("raw: {} parsed: {}", input, result);

            return result;
        } catch (Exception e) {
            log.error(e);
            throw new BookFormatterException("Cannot deserialize Book from given string. Data is malformed", e);
        }
    }

    @Override
    public String toString(BookModel book) {
        StringBuilder sb = new StringBuilder();

        sb.append(book.getTitle()).append(Separator);

        for (String author : book.getAuthors()) {
            sb.append(author).append(';');
        }

        sb.append(Separator);

        sb.append(book.getPublisher()).append(Separator);

        log.debug("serialized: {} to string: {}", book, sb);

        return sb.toString();
    }
}
