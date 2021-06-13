package com.kdenisova.myreadinglist.view;

import com.kdenisova.myreadinglist.controller.ApplicationController;
import com.kdenisova.myreadinglist.controller.ApplicationControllerException;
import com.kdenisova.myreadinglist.model.BookModel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

public class CLIConsole {

    private static final Logger log = LogManager.getLogger(CLIConsole.class);

    private final Scanner scanner;
    private final ApplicationController applicationController;

    public CLIConsole(ApplicationController applicationController) {
        this.applicationController = applicationController;

        this.scanner = new Scanner(System.in).useDelimiter("\r?\n");
    }

    public void renderMenu() {
        String option;
        boolean selected = false;

        renderHeader();

        do {
            System.out.println(ColorType.WHITE + "Choose an option:");
            System.out.println("(1) Find Books");
            System.out.println("(2) View Reading List");
            System.out.println("(3) Exit\n");
            System.out.print("> ");

            option = scanner.next();

            final String availableOptions = "123";

            if (option.length() == 1 && availableOptions.contains(option)) {
                selected = true;
            } else {
                System.out.println(ColorType.RESET + "\n*** Unknown option! ***\n");
            }
        } while (!selected);

        System.out.print(ColorType.RESET);

        switch (option) {
            case "1":
                runSearchByQuery();
                break;
            case "2":
                showReadingList();
                break;
            case "3":
                scanner.close();
                System.exit(0);
                break;
        }
    }

    private void returnToMainMenu() {
        String option;
        boolean selected = false;

        do {
            System.out.println(ColorType.WHITE + "\nWould you like to return to the Main Menu (yY/nN)?");
            System.out.print("> ");

            option = scanner.next();

            final String availableOptions = "yYnN";

            if (option.length() == 1 && availableOptions.contains(option)) {
                selected = true;
            } else {
                System.out.println(ColorType.RESET + "\n*** Unknown option! ***\n");
            }
        } while (!selected);

        System.out.print(ColorType.RESET);

        switch (option.toLowerCase()) {
            case "y":
                renderMenu();
                break;
            case "n":
                scanner.close();
                System.exit(0);
                break;
        }
    }

    private static void renderHeader() {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println(ColorType.WHITE + "* * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("*                                                 *");
        System.out.println("*                                                 *");
        System.out.print("*           ");
        System.out.print(ColorType.CYAN + "Welcome to My Reading List");
        System.out.println(ColorType.WHITE + "            *");
        System.out.println("*                                                 *");
        System.out.println("*                                                 *");
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println();
    }

    private void runSearchByQuery() {
        String option;
        boolean selected = false;
        List<BookModel> books = null;

        do {
            System.out.println(ColorType.WHITE + "\nEnter the book you would like to find:");
            System.out.print("> ");

            option = scanner.next();

            try {
                books = applicationController.search(option);
            } catch (ApplicationControllerException e) {
                log.log(Level.ERROR, e.getMessage(), e);
                System.out.println(e.getMessage());
            }

            if (books != null && !books.isEmpty()) {
                selected = true;
            } else {
                System.out.println(ColorType.RESET + "\n*** Books not found! Please try again ***\n");
            }

        } while (!selected);

        showQueryResult(books);
    }

    private void showQueryResult(List<BookModel> books) {
        String option;
        boolean selected = false;

        renderHeader();

        Integer selectedBookOption = null;

        do {
            System.out.println(ColorType.WHITE + "Choose the book you would like to save:");

            for (int i = 0; i < books.size(); i++) {
                System.out.printf("(%d) %s\n", i + 1, convertBookToString(books.get(i)));
            }

            System.out.printf("\n(%d) %s\n", 0, "Return to Main Menu");

            System.out.print("\n> ");

            option = scanner.next();

            try {
                selectedBookOption = Integer.valueOf(option);
            } catch (NumberFormatException ignored) {
            }

            if (selectedBookOption != null && selectedBookOption >= 0 && selectedBookOption <= books.size()) {
                selected = true;
            } else {
                System.out.println(ColorType.RESET + "\n*** Unknown option! ***\n");
            }
        } while (!selected);

        if (selectedBookOption == 0) {
            renderMenu();
            return;
        }

        try {
            applicationController.saveToReadingList(books.get(selectedBookOption - 1));
        } catch (ApplicationControllerException e) {
            log.log(Level.ERROR, e.getMessage(), e);
            System.out.println(e.getMessage());
        }

        System.out.println(ColorType.RESET + "\n*** The book has been saved to the Reading List ***");

        returnToMainMenu();
    }

    private void showReadingList() {
        try {
            List<BookModel> books = applicationController.getReadingList();

            if (books.isEmpty()) {
                System.out.println("\n*** The Reading list is empty! ***");
                returnToMainMenu();
            }

            renderHeader();

            System.out.println("Your Reading List:");

            for (int i = 0; i < books.size(); i++) {
                System.out.printf("(%d) %s\n", i + 1, convertBookToString(books.get(i)));
            }

        } catch (ApplicationControllerException e) {
            log.log(Level.ERROR, e.getMessage(), e);
            System.out.println(e.getMessage());
        }

        returnToMainMenu();
    }

    private static String convertBookToString(BookModel book) {
        StringBuilder sb = new StringBuilder();

        sb.append("\"").append(book.getTitle()).append("\", ");

        List<String> authors = book.getAuthors();

        if (authors.size() == 1) {
            sb.append("author: ").append(authors.get(0)).append(", ");
        } else if (authors.size() > 1) {
            sb.append("authors: ");

            for (String author : authors) {
                sb.append(author).append(", ");
            }
        }

        if (!book.getPublisher().isEmpty()) {
            sb.append("published by \"").append(book.getPublisher()).append("\"");
        } else {
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }
}
