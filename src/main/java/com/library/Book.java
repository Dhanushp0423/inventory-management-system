package com.library;

public class Book {


    public static final String TABLE_HEADER =
            String.format("%-14s | %-28s | %-20s | %s", "ISBN", "TITLE", "AUTHOR", "YEAR");
    public static final String TABLE_SEPARATOR =
            "--------------------------------------------------------------------------------";

    private static final String ROW_FORMAT = "%-14s | %-28s | %-20s | %d";

    private String title;
    private String author;
    private final String isbn;
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


    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }


    @Override
    public String toString() {
        return String.format(ROW_FORMAT, isbn, title, author, publicationYear);
    }
}
