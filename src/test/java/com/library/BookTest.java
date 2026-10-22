package com.library;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Book entity tests")
class BookTest {

    @Test
    @DisplayName("ISBN is immutable - Book must not expose setIsbn()")
    void isbnIsImmutable() {
        boolean hasIsbnSetter = false;
        for (var method : Book.class.getDeclaredMethods()) {
            if (method.getName().equals("setIsbn")) {
                hasIsbnSetter = true;
            }
        }
        assertFalse(hasIsbnSetter, "Book must not expose setIsbn()");
    }

    @Test
    @DisplayName("title, author and publication year are editable")
    void editableFieldsHaveSetters() {
        Book book = new Book("T", "A", "ISBN-1", 2000);
        book.setTitle("T2");
        book.setAuthor("A2");
        book.setPublicationYear(2001);
        assertEquals("T2", book.getTitle());
        assertEquals("A2", book.getAuthor());
        assertEquals(2001, book.getPublicationYear());
    }

    @Test
    @DisplayName("toString contains the ISBN, title and author")
    void toStringContainsKeyFields() {
        Book book = new Book("Clean Code", "Robert C. Martin", "9780132350884", 2008);
        String out = book.toString();
        assertTrue(out.contains("9780132350884"));
        assertTrue(out.contains("Clean Code"));
        assertTrue(out.contains("Robert C. Martin"));
    }
}
