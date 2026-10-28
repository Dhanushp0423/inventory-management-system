package com.library;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookService {

    private final Map<String, Book> books = new LinkedHashMap<>();


    public Book addBook(Book book) {
        String key = normalizeIsbn(book.getIsbn());
        if (books.containsKey(key)) {
            throw new DuplicateIsbnException(book.getIsbn());
        }
        books.put(key, book);
        return book;
    }


    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }


    public Book findByIsbn(String isbn) {
        Book book = books.get(normalizeIsbn(isbn));
        if (book == null) {
            throw new BookNotFoundException(isbn);
        }
        return book;
    }


    public List<Book> searchByKeyword(String keyword) {
        String needle = keyword.trim().toLowerCase();
        return books.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(needle)
                        || b.getAuthor().toLowerCase().contains(needle))
                .collect(Collectors.toList());
    }


    public Book updateBook(String isbn, String title, String author, int year) {
        Book book = findByIsbn(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublicationYear(year);
        return book;
    }


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

    private String normalizeIsbn(String isbn) {
        return isbn.trim().toUpperCase();
    }
}
