package com.kdenisova.myreadinglist.controller;

public interface ApplicationCoreFactory {

    ApplicationController createStandard();

    static ApplicationCoreFactory getInstance() {
        return new ApplicationCoreFactoryImpl();
    }
}
