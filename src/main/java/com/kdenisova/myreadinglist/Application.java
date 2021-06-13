package com.kdenisova.myreadinglist;

import com.kdenisova.myreadinglist.controller.ApplicationController;
import com.kdenisova.myreadinglist.controller.ApplicationCoreFactory;
import com.kdenisova.myreadinglist.view.CLIConsole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Application {

    private static final Logger log = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        try {
            ApplicationController applicationController = ApplicationCoreFactory.getInstance().createStandard();

            CLIConsole console = new CLIConsole(applicationController);
            console.renderMenu();
        } catch (Exception e) {
            log.fatal(e);
        }
    }
}
