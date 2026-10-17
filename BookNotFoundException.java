package com.library;


public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String isbn) {
        super("No book found with ISBN: " + isbn);
    }
}
