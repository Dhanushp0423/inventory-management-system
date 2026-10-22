package com.library;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class BookService {

    private final Map<String, Book> books = new LinkedHashMap<>();

    /** Adds a book. The ISBN must not already exist. */
    public Book addBook(Book book) {
        String key = normalizeIsbn(book.getIsbn());
        if (books.containsKey(key)) {
            throw new DuplicateIsbnException(book.getIsbn());
        }
        books.put(key, book);
        return book;
    }

    /** Returns all books. A copy is returned so the internal map stays private. */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    /** Finds one book by ISBN or throws BookNotFoundException. */
    public Book findByIsbn(String isbn) {
        Book book = books.get(normalizeIsbn(isbn));
        if (book == null) {
            throw new BookNotFoundException(isbn);
        }
        return book;
    }

    /**
     * Updates the editable fields of a book.
     * The ISBN is the identity of the book, so it is never updated here.
     */
    public Book updateBook(String isbn, String title, String author, int year) {
        Book book = findByIsbn(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublicationYear(year);
        return book;
    }

    /** Removes a book by ISBN or throws BookNotFoundException. */
    public boolean deleteBook(String isbn) {
        Book removed = books.remove(normalizeIsbn(isbn));
        if (removed == null) {
            throw new BookNotFoundException(isbn);
        }
        return true;
    }

    public int count() {
        return books.size();
    }

    /** Trims whitespace and upper-cases the ISBN so lookups are case-insensitive. */
    private String normalizeIsbn(String isbn) {
        return isbn.trim().toUpperCase();
    }
}
