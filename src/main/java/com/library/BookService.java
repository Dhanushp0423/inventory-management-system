package com.library;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookService {

    private final Map<String, Book> books = new LinkedHashMap<>();
    private final BookRepository repository;

    /** In-memory service (Week 2-4 behaviour); nothing is saved between runs. */
    public BookService() {
        this(BookRepository.inMemory());
    }

    /** Service backed by a repository: existing data is loaded now, changes are saved as they happen. */
    public BookService(BookRepository repository) {
        this.repository = repository;
        for (Book b : repository.loadAll()) {
            books.put(normalizeIsbn(b.getIsbn()), b);
        }
    }


    public Book addBook(Book book) {
        String key = normalizeIsbn(book.getIsbn());
        if (books.containsKey(key)) {
            throw new DuplicateIsbnException(book.getIsbn());
        }
        books.put(key, book);
        persist();
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
        persist();
        return book;
    }


    public boolean deleteBook(String isbn) {
        Book removed = books.remove(normalizeIsbn(isbn));
        if (removed == null) {
            throw new BookNotFoundException(isbn);
        }
        persist();
        return true;
    }

    public int count() {
        return books.size();
    }

    private void persist() {
        repository.saveAll(books.values());
    }

    private String normalizeIsbn(String isbn) {
        return isbn.trim().toUpperCase();
    }
}
