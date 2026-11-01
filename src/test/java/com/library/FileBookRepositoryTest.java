package com.library;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FileBookRepository (Week 5 persistence) tests")
class FileBookRepositoryTest {

    @TempDir
    Path tmp;

    @Test
    @DisplayName("loading a file that does not exist returns an empty list")
    void missingFileGivesEmptyList() {
        assertTrue(new FileBookRepository(tmp.resolve("none.csv")).loadAll().isEmpty());
    }

    @Test
    @DisplayName("books saved by one repository instance are loaded by another")
    void roundTrip() {
        Path file = tmp.resolve("books.csv");
        new FileBookRepository(file).saveAll(List.of(
                new Book("Clean Code", "Robert C. Martin", "9780132350884", 2008),
                new Book("Effective Java", "Joshua Bloch", "9780134685991", 2018)));

        List<Book> loaded = new FileBookRepository(file).loadAll();
        assertEquals(2, loaded.size());
        assertEquals("Clean Code", loaded.get(0).getTitle());
        assertEquals(2018, loaded.get(1).getPublicationYear());
    }

    @Test
    @DisplayName("commas and quotes inside fields survive a save/load cycle")
    void specialCharactersAreEscaped() {
        Path file = tmp.resolve("books.csv");
        new FileBookRepository(file).saveAll(List.of(
                new Book("Hello, \"World\"", "Doe, John", "ISBN-1", 2001)));

        Book b = new FileBookRepository(file).loadAll().get(0);
        assertEquals("Hello, \"World\"", b.getTitle());
        assertEquals("Doe, John", b.getAuthor());
    }

    @Test
    @DisplayName("a malformed line raises RepositoryException with the line number")
    void malformedLineIsReported() throws IOException {
        Path file = tmp.resolve("bad.csv");
        Files.writeString(file, "isbn,title,author,year\nONLY-ONE-FIELD\n");
        RepositoryException ex = assertThrows(RepositoryException.class,
                () -> new FileBookRepository(file).loadAll());
        assertTrue(ex.getMessage().contains("line 2"));
    }

    @Test
    @DisplayName("BookService persists every change and a new service sees the data")
    void serviceSurvivesRestart() {
        Path file = tmp.resolve("books.csv");
        BookService first = new BookService(new FileBookRepository(file));
        first.addBook(new Book("T", "A", "ISBN-9", 2000));
        first.updateBook("ISBN-9", "T2", "A2", 2005);

        BookService second = new BookService(new FileBookRepository(file));
        assertEquals(1, second.count());
        assertEquals("T2", second.findByIsbn("isbn-9").getTitle());

        second.deleteBook("ISBN-9");
        assertEquals(0, new BookService(new FileBookRepository(file)).count());
    }
}
