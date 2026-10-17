# Library Book Inventory Manager

Week 2 deliverable for the Junior Java Developer Internship - a menu-driven
command-line Java application that manages a library's book inventory using
in-memory Java collections.

## Features

- Add a new book (title, author, ISBN, publication year)
- List all books in a formatted table
- Search a book by its ISBN
- Update a book's title, author and publication year (ISBN stays fixed)
- Delete a book with a YES confirmation
- Input validation (no empty fields, valid year range, numeric menu choices)
- Custom exceptions: BookNotFoundException, DuplicateIsbnException

## Project Structure

    src/main/java/com/library/
    |-- Book.java                  - book entity (ISBN is the unique id)
    |-- BookService.java           - CRUD logic, inventory stored in LinkedHashMap
    |-- BookNotFoundException.java - thrown for unknown ISBNs
    |-- DuplicateIsbnException.java- thrown when an ISBN already exists
    |-- LibraryApp.java            - menu-driven CLI (main class)

## Requirements

- JDK 17 or newer (check with: java -version)

## How to Compile

From the project root folder (the folder that contains `src`):

    javac -d out src/main/java/com/library/*.java

This puts the compiled .class files into the `out` folder.

## How to Run

    java -cp out com.library.LibraryApp

## How to Use

The app shows a numbered menu. Type the number and press Enter:

    1. Add a new book
    2. List all books
    3. Search book by ISBN
    4. Update a book
    5. Delete a book
    0. Exit

When updating a book, leave a field blank and press Enter to keep its
current value.

## Design Notes

- Inventory is stored in a LinkedHashMap keyed by ISBN: fast lookups plus
  predictable listing order.
- ISBN is immutable after creation because it identifies the book.
- All validation happens in LibraryApp; business rules (duplicates, missing
  books) are enforced by BookService through custom exceptions.
