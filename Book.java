package com.library;

public class Book {

    private String title;
    private String author;
    private final String isbn;        // unique key - fixed after creation
    private int publicationYear;

    public Book(String title, String author, String isbn, int publicationYear) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    // title, author and year can be edited later, ISBN cannot
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    // formatted row used when printing the book list
    @Override
    public String toString() {
        return String.format("%-14s | %-28s | %-20s | %d",
                isbn, title, author, publicationYear);
    }
}
