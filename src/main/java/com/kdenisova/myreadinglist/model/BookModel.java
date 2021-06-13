package com.kdenisova.myreadinglist.model;

import java.util.ArrayList;
import java.util.List;

public final class BookModel {

    private List<String> authors;
    private String title;
    private String publisher;

    public BookModel() {
        authors = new ArrayList<>();
    }

    public BookModel(List<String> authors, String title, String publisher) {
        this.authors = authors;
        this.title = title;
        this.publisher = publisher;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "BookModel{" +
                "authors=" + authors +
                ", title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                '}';
    }
}
