package com.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BookService unit tests")
class BookServiceTest {

    private BookService service;
    private final Book sample = new Book("Clean Code", "Robert C. Martin", "9780132350884", 2008);

    @BeforeEach
    void setUp() {
        service = new BookService();
    }

    // ---------- CREATE ----------

    @Test
    @DisplayName("addBook stores the book and updates the count")
    void addBookStoresBook() {
        service.addBook(sample);
        assertEquals(1, service.count());
        assertSame(sample, service.findByIsbn("9780132350884"));
    }

    @Test
    @DisplayName("addBook rejects a duplicate ISBN and stores nothing extra")
    void addBookRejectsDuplicateIsbn() {
        service.addBook(sample);
        Book duplicate = new Book("Other title", "Other author", "9780132350884", 2020);
        assertThrows(DuplicateIsbnException.class, () -> service.addBook(duplicate));
        assertEquals(1, service.count());
    }

    @Test
    @DisplayName("ISBN matching is case-insensitive after the Week 3 fix")
    void isbnMatchingIsCaseInsensitive() {
        service.addBook(new Book("Refactoring", "Martin Fowler", "ABC123X", 1999));
        // 'abc123x' is the same ISBN in different case - it must resolve to the same book
        assertDoesNotThrow(() -> service.findByIsbn("abc123x"));
        assertThrows(DuplicateIsbnException.class,
                () -> service.addBook(new Book("T", "A", "abc123x", 2001)));
    }

    // ---------- READ ----------

    @Test
    @DisplayName("getAllBooks returns an empty list when nothing was added")
    void listIsEmptyAtStart() {
        assertTrue(service.getAllBooks().isEmpty());
    }

    @Test
    @DisplayName("getAllBooks keeps the insertion order")
    void listPreservesInsertionOrder() {
        service.addBook(new Book("B", "a", "ISBN-B", 2001));
        service.addBook(new Book("A", "b", "ISBN-A", 2002));
        List<Book> all = service.getAllBooks();
        assertEquals("ISBN-B", all.get(0).getIsbn());
        assertEquals("ISBN-A", all.get(1).getIsbn());
    }

    @Test
    @DisplayName("getAllBooks returns a copy - mutating it must not damage the inventory")
    void returnedListIsDefensiveCopy() {
        service.addBook(sample);
        List<Book> all = service.getAllBooks();
        all.clear();                          // caller tries to wipe the list
        assertEquals(1, service.count());     // inventory must be untouched
    }

    @Test
    @DisplayName("findByIsbn throws BookNotFoundException for an unknown ISBN")
    void findThrowsForUnknownIsbn() {
        assertThrows(BookNotFoundException.class, () -> service.findByIsbn("NOPE-000"));
    }

    // ---------- UPDATE ----------

    @Test
    @DisplayName("updateBook edits title, author and year but never the ISBN")
    void updateChangesEditableFieldsOnly() {
        service.addBook(sample);
        Book updated = service.updateBook("9780132350884", "Clean Code (2nd ed.)", "Robert C. Martin", 2023);
        assertEquals("Clean Code (2nd ed.)", updated.getTitle());
        assertEquals(2023, updated.getPublicationYear());
        assertEquals("9780132350884", updated.getIsbn());
    }

    @Test
    @DisplayName("updateBook throws for an unknown ISBN")
    void updateThrowsForUnknownIsbn() {
        assertThrows(BookNotFoundException.class,
                () -> service.updateBook("UNKNOWN", "t", "a", 2000));
    }

    // ---------- DELETE ----------

    @Test
    @DisplayName("deleteBook removes the book and returns true")
    void deleteRemovesBook() {
        service.addBook(sample);
        assertTrue(service.deleteBook("9780132350884"));
        assertEquals(0, service.count());
        assertThrows(BookNotFoundException.class, () -> service.findByIsbn("9780132350884"));
    }

    @Test
    @DisplayName("deleteBook throws for an unknown ISBN and changes nothing")
    void deleteThrowsForUnknownIsbn() {
        service.addBook(sample);
        assertThrows(BookNotFoundException.class, () -> service.deleteBook("UNKNOWN"));
        assertEquals(1, service.count());
    }

    // ---------- FULL FLOW ----------

    @Test
    @DisplayName("a full create-update-read-delete session works end to end")
    void fullCrudFlow() {
        service.addBook(sample);
        service.updateBook("9780132350884", "New title", "New author", 2010);
        assertEquals("New title", service.findByIsbn("9780132350884").getTitle());
        service.deleteBook("9780132350884");
        assertTrue(service.getAllBooks().isEmpty());
    }
}
