package com.library;

import java.util.List;
import java.util.Scanner;
public class LibraryApp {

    private final BookService service = new BookService();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new LibraryApp().start();
    }

    public void start() {
        System.out.println("========================================");
        System.out.println("     Library Book Inventory Manager");
        System.out.println("========================================");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": addBook();    break;
                case "2": listBooks();  break;
                case "3": searchBook(); break;
                case "4": updateBook(); break;
                case "5": deleteBook(); break;
                case "0":
                    running = false;
                    System.out.println("Thank you for using the Library Manager. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please choose a number from 0 to 5.");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println();
        System.out.println("---------- MAIN MENU ----------");
        System.out.println("1. Add a new book");
        System.out.println("2. List all books");
        System.out.println("3. Search book by ISBN");
        System.out.println("4. Update a book");
        System.out.println("5. Delete a book");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    // ---- CREATE ----
    private void addBook() {
        System.out.println("\n--- Add a new book ---");
        String title = readNonEmpty("Title: ");
        String author = readNonEmpty("Author: ");
        String isbn = readNonEmpty("ISBN: ");
        int year = readYear("Publication year: ", null);

        try {
            service.addBook(new Book(title, author, isbn, year));
            System.out.println("Book added successfully.");
        } catch (DuplicateIsbnException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---- READ (all) ----
    private void listBooks() {
        List<Book> all = service.getAllBooks();
        System.out.println("\n--- Book list (" + all.size() + " book(s)) ---");
        if (all.isEmpty()) {
            System.out.println("The inventory is empty.");
            return;
        }
        System.out.printf("%-14s | %-28s | %-20s | %s%n",
                "ISBN", "TITLE", "AUTHOR", "YEAR");
        System.out.println("--------------------------------------------------------------------------------");
        for (Book b : all) {
            System.out.println(b);
        }
    }

    // ---- READ (one) ----
    private void searchBook() {
        System.out.println("\n--- Search book by ISBN ---");
        String isbn = readNonEmpty("Enter ISBN: ");
        try {
            Book b = service.findByIsbn(isbn);
            System.out.printf("%-14s | %-28s | %-20s | %s%n",
                    "ISBN", "TITLE", "AUTHOR", "YEAR");
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println(b);
        } catch (BookNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---- UPDATE ----
    private void updateBook() {
        System.out.println("\n--- Update a book ---");
        String isbn = readNonEmpty("Enter ISBN of the book to update: ");

        try {
            Book existing = service.findByIsbn(isbn);
            System.out.println("Leave a field blank and press Enter to keep the current value.");
            System.out.println("Current title: " + existing.getTitle());
            String title = readKeepCurrent("New title: ", existing.getTitle());
            System.out.println("Current author: " + existing.getAuthor());
            String author = readKeepCurrent("New author: ", existing.getAuthor());
            int year = readYear("New publication year: ", existing.getPublicationYear());

            service.updateBook(isbn, title, author, year);
            System.out.println("Book updated successfully.");
        } catch (BookNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---- DELETE ----
    private void deleteBook() {
        System.out.println("\n--- Delete a book ---");
        String isbn = readNonEmpty("Enter ISBN of the book to delete: ");

        try {
            Book b = service.findByIsbn(isbn);
            System.out.println("About to delete: " + b.getTitle() + " (" + b.getIsbn() + ")");
            String confirm = readNonEmpty("Type YES to confirm: ");
            if (!confirm.equalsIgnoreCase("YES")) {
                System.out.println("Deletion cancelled.");
                return;
            }
            service.deleteBook(isbn);
            System.out.println("Book deleted successfully.");
        } catch (BookNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("This field cannot be empty. Please try again.");
        }
    }


    private String readKeepCurrent(String prompt, String current) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? current : input;
    }


    private int readYear(String prompt, Integer current) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty() && current != null) {
                return current;   // update mode: blank keeps the old year
            }
            try {
                int year = Integer.parseInt(input);
                if (year < 1400 || year > 2026) {
                    System.out.println("Please enter a realistic year between 1400 and 2026.");
                    continue;
                }
                return year;
            } catch (NumberFormatException e) {
                System.out.println("That is not a valid number. Please try again.");
            }
        }
    }
}
