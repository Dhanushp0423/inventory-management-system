# Library Book Inventory Manager

Junior Java Developer Internship project. A menu-driven command-line Java
application that manages a library's book inventory using in-memory Java
collections, with a JUnit 5 test suite added in Week 3.

## Features

- Add a new book (title, author, ISBN, publication year)
- List all books in a formatted table
- Search a book by its ISBN
- Update a book's title, author and publication year (ISBN stays fixed)
- Delete a book with a YES confirmation
- Input validation (no empty fields, valid year range, numeric menu choices)
- Custom exceptions: BookNotFoundException, DuplicateIsbnException
- Case-insensitive ISBN matching (Week 3 bug fix)

## Project Structure

    src/main/java/com/library/
    |-- Book.java                   - book entity (ISBN is the unique id)
    |-- BookService.java            - CRUD logic, LinkedHashMap storage (v1.1)
    |-- BookNotFoundException.java  - thrown for unknown ISBNs
    |-- DuplicateIsbnException.java - thrown when an ISBN already exists
    |-- LibraryApp.java             - menu-driven CLI (main class)

    src/test/java/com/library/
    |-- BookServiceTest.java        - 12 unit tests for the CRUD service
    |-- BookTest.java               - 3 entity contract tests
    |-- LibraryAppTest.java         - 4 CLI tests (simulated keyboard input)

## Requirements

- JDK 17 or newer
- Maven 3.8 or newer (check with: mvn -version)

## How to Compile and Run the App

    mvn compile
    mvn exec:java -Dexec.mainClass=com.library.LibraryApp

(If you prefer plain javac: compile src/main/java/com/library/*.java with
javac -d out, then run java -cp out com.library.LibraryApp.)

## How to Run the Tests

    mvn test

Expected result: 19 tests run, 0 failures, 0 errors.

## Week 3 Notes

See docs/Week3_Testing_Debugging_Report.docx for the full list of bugs
found during testing, the debugging process, and the v1.1 refactor of
BookService (ISBN normalisation).
