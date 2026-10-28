# Library Book Inventory Manager

Junior Java Developer Internship project. A menu-driven command-line Java
application that manages a library's book inventory using in-memory Java
collections. Week 3 added a JUnit 5 test suite; Week 4 refactored the
codebase to v2.0.

## Features

- Add a new book (title, author, ISBN, publication year)
- List all books in a formatted table
- Search a book by its ISBN
- Search by title / author keyword (case-insensitive, Week 4 addition)
- Update a book's title, author and publication year (ISBN stays fixed)
- Delete a book with a YES confirmation
- Input validation (no empty fields, valid year range, numeric menu choices)
- Custom exceptions: BookNotFoundException, DuplicateIsbnException
- Case-insensitive ISBN matching (Week 3 bug fix)

## Week 4 Refactoring Highlights

- Extracted ConsoleInput class - all keyboard input and validation now
  lives in one place (single responsibility)
- Menu dispatch is a command map instead of a switch statement
- Shared table layout lives in Book constants (list / ISBN search /
  keyword search all print identical columns)
- Publication-year bounds are named constants, not magic numbers
- New keyword search built on the streams API

## Project Structure

    src/main/java/com/library/
    |-- Book.java                   - book entity + shared table layout (v2.0)
    |-- BookService.java            - CRUD + keyword search (v2.0)
    |-- ConsoleInput.java           - all input handling (new in v2.0)
    |-- BookNotFoundException.java  - thrown for unknown ISBNs
    |-- DuplicateIsbnException.java - thrown when an ISBN already exists
    |-- LibraryApp.java             - menu-driven CLI (v2.0)

    src/test/java/com/library/
    |-- BookServiceTest.java        - 15 unit tests for the service
    |-- BookTest.java               - 3 entity contract tests
    |-- LibraryAppTest.java         - 5 CLI tests (simulated keyboard input)

## Requirements

- JDK 17 or newer
- Maven 3.8 or newer (check with: mvn -version)

## How to Compile and Run the App

    mvn compile
    mvn exec:java -Dexec.mainClass=com.library.LibraryApp

## How to Run the Tests

    mvn test

Expected result: 23 tests run, 0 failures, 0 errors.

## Documentation

- docs/Week1_Inventory_Management_System_Design_Document.docx - architecture
- docs/Week2_Library_Inventory_Report.docx - implementation
- docs/Week3_Testing_Debugging_Report.docx - bugs and test suite
- docs/Week4_Refactoring_Report.docx - before/after refactoring analysis
