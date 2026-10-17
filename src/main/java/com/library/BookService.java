package com.library;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BookService {

    private final Map<String, Book> books = new LinkedHashMap<>();


    public Book addBook(Book book) {
        if (books.containsKey(book.getIsbn())) {
            throw new DuplicateIsbnException(book.getIsbn());
        }
        books.put(book.getIsbn(), book);
        return book;
    }


    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }


    public Book findByIsbn(String isbn) {
        Book book = books.get(isbn);
        if (book == null) {
            throw new BookNotFoundException(isbn);
        }
        return book;
    }

    public Book updateBook(String isbn, String title, String author, int year) {
        Book book = findByIsbn(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublicationYear(year);
        return book;
    }


    public boolean deleteBook(String isbn) {
        Book removed = books.remove(isbn);
        if (removed == null) {
            throw new BookNotFoundException(isbn);
        }
        return true;
    }

    public int count() {
        return books.size();
    }
}
