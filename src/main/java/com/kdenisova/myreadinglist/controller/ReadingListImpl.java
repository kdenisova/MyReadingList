package com.kdenisova.myreadinglist.controller;

import com.kdenisova.myreadinglist.model.BookModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadingListImpl implements ReadingList {

    private static final Logger log = LogManager.getLogger(ReadingListImpl.class);

    private final String pathToStorageFile;
    private final BookFormatter bookFormatter;

    public ReadingListImpl(BookFormatter bookFormatter) {
        this(bookFormatter, "ReadingList.txt");
    }

    public ReadingListImpl(BookFormatter bookFormatter, String pathToStorageFile) {
        this.bookFormatter = bookFormatter;
        this.pathToStorageFile = pathToStorageFile;
    }

    @Override
    public void save(BookModel book) throws IOException {
        BufferedWriter writer = null;

        try {
            String formattedBook = bookFormatter.toString(book);

            log.debug("received a formatted book {}", formattedBook);

            File file = new File(pathToStorageFile);

            log.debug("saving to {}", file);

            writer = new BufferedWriter(new FileWriter(file, true));

            if (file.length() > 0) {
                writer.newLine();
            }

            writer.write(formattedBook);
        } catch (Exception e) {
            log.error(e);
            throw e;
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    @Override
    public List<BookModel> getAll() throws IOException, BookFormatterException {
        List<BookModel> books = new ArrayList<>();

        BufferedReader reader = null;
        try {
            File file = new File(pathToStorageFile);

            log.debug("accessing reading list storage at {}", file);

            if (!file.exists()) {
                return books;
            }

            reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                books.add(bookFormatter.fromString(line));
            }
        } catch (Exception e) {
            log.error(e);
            throw e;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return books;
    }
}
