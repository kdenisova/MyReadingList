# MyReadingList
A command line Java application that allows you to use the Google Books API to search for books and construct a reading list.

The application allows you to:

* Type in a query and display a list of up to 5 books matching that query.
* Each item in the list includes the title, book's author, and publishing company.
* User can select a book from the list of displayed to save to a “Reading List”
* View a “Reading List” with all the books the user has selected from previous queries.

## Technology stack
* Java
* [Maven](http://maven.apache.org) - Software project management.
* [Log4j](https://logging.apache.org/log4j/2.x/) - Logging library.
* [JUnit](https://junit.org/junit5/) - Unit testing framework.

## How to use?

[JDK](https://docs.oracle.com/en/java/javase/16/install/overview-jdk-installation.html#GUID-8677A77F-231A-40F7-98B9-1FD0B48C346A) and [Maven](https://maven.apache.org/download.cgi) are required prerequisites.

1. Build

     `mvn clean package`

2. Launch
     
     `java -jar target/myreadinglist.jar`

3. Tests

   `mvn clean test`
     
## Project Structure
* com.kdenisova.myreadinglist/

  *  **Application.java** - The Main class of MyReadingList application.
  
* com.kdenisova.myreadinglist.controller/

  * **ApplicationController.java** - The application controller that responsible for the main application logic and communication between model and view components (**ApplicationControllerImpl.java** as a default implementation).

  * **BookFormatter.java** - The component responsible for book formatting (**BookFormatterImpl.java** as a default implementation).
  
  * **GoogleBooksApiClient.java** -  The component responsible for making a GET request to Google Books API and returning a search result  (**GoogleBooksApiClientImpl.java** as a default implementation).

  * **ReadingList.java** - The component responsible for working with a Reading List (**ReadingListImpl.java** as a default implementation).

* com.kdenisova.myreadinglist.model/
  
  * **BookModel.java** - The book model.

* com.kdenisova.myreadinglist.view/

  * **CLIConsole.java** - The application renderer.



