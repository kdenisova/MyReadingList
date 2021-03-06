package com.kdenisova.myreadinglist.controller;

public class ApplicationCoreFactoryImpl implements ApplicationCoreFactory {

    @Override
    public ApplicationController createStandard() {
        return new ApplicationControllerImpl(
                new GoogleBooksApiClientImpl(new GoogleBooksUriBuilderImpl(), new GoogleBooksApiResponseMapperImpl()),
                new ReadingListImpl(new BookFormatterImpl()));
    }
}
