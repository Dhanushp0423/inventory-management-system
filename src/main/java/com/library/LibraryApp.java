package com.library;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class LibraryApp {

    private final BookService service;
    private final ConsoleInput input = new ConsoleInput(new Scanner(System.in));

    private final Map<String, Runnable> menuActions = new LinkedHashMap<>();
    private boolean running = false;

    /** Name of the environment variable that points at the CSV data file. */
    public static final String DATA_FILE_ENV = "LIBRARY_DATA_FILE";

    /** In-memory application (used by the tests and when no data file is configured). */
    public LibraryApp() {
        this(new BookService());
    }

    /** Application wired to the given service (dependency injection, Week 5). */
    public LibraryApp(BookService service) {
        this.service = service;
        menuActions.put("1", this::addBook);
        menuActions.put("2", this::listBooks);
        menuActions.put("3", this::searchByIsbn);
        menuActions.put("4", this::updateBook);
        menuActions.put("5", this::deleteBook);
        menuActions.put("6", this::searchByKeyword);
        menuActions.put("0", this::exitApp);
    }

    /**
     * Entry point. Storage is chosen at start-up:
     * {@code --data <file>} argument, else the LIBRARY_DATA_FILE environment variable,
     * else in-memory only (nothing is saved).
     */
    public static void main(String[] args) {
        String dataFile = resolveDataFile(args, System.getenv(DATA_FILE_ENV));
        BookService service;
        if (dataFile == null) {
            service = new BookService();
        } else {
            try {
                service = new BookService(new FileBookRepository(Path.of(dataFile)));
                System.out.println("Using data file: " + Path.of(dataFile).toAbsolutePath());
            } catch (RepositoryException e) {
                System.err.println("Startup failed: " + e.getMessage());
                System.exit(1);
                return;
            }
        }
        new LibraryApp(service).start();
    }

    static String resolveDataFile(String[] args, String envValue) {
        for (int i = 0; i < args.length - 1; i++) {
            if ("--data".equals(args[i])) {
                return args[i + 1];
            }
        }
        return (envValue == null || envValue.isBlank()) ? null : envValue;
    }

    public void start() {
        System.out.println("========================================");
        System.out.println("     Library Book Inventory Manager");
        System.out.println("========================================");

        running = true;
        while (running) {
            printMenu();
            String choice = input.readRaw("Enter your choice: ").trim();
            try {
                menuActions.getOrDefault(choice, this::invalidChoice).run();
            } catch (RepositoryException e) {
                System.out.println("Storage error: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("---------- MAIN MENU ----------");
        System.out.println("1. Add a new book");
        System.out.println("2. List all books");
        System.out.println("3. Search book by ISBN");
        System.out.println("4. Update a book");
        System.out.println("5. Delete a book");
        System.out.println("6. Search by title / author keyword");
        System.out.println("0. Exit");
    }

    private void exitApp() {
        running = false;
        System.out.println("Thank you for using the Library Manager. Goodbye!");
    }

    private void invalidChoice() {
        System.out.println("Invalid option. Please choose a number from 0 to 6.");
    }


    private void addBook() {
        System.out.println("\n--- Add a new book ---");
        String title = input.readNonEmpty("Title: ");
        String author = input.readNonEmpty("Author: ");
        String isbn = input.readNonEmpty("ISBN: ");
        int year = input.readYear("Publication year: ", null);

        try {
            service.addBook(new Book(title, author, isbn, year));
            System.out.println("Book added successfully.");
        } catch (DuplicateIsbnException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private void listBooks() {
        List<Book> all = service.getAllBooks();
        System.out.println("\n--- Book list (" + all.size() + " book(s)) ---");
        if (all.isEmpty()) {
            System.out.println("The inventory is empty.");
            return;
        }
        printTable(all);
    }


    private void searchByIsbn() {
        System.out.println("\n--- Search book by ISBN ---");
        String isbn = input.readNonEmpty("Enter ISBN: ");
        try {
            printTable(List.of(service.findByIsbn(isbn)));
        } catch (BookNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private void searchByKeyword() {
        System.out.println("\n--- Search by title / author keyword ---");
        String keyword = input.readNonEmpty("Enter keyword: ");
        List<Book> matches = service.searchByKeyword(keyword);
        System.out.println("--- " + matches.size() + " match(es) ---");
        if (matches.isEmpty()) {
            System.out.println("No books matched your keyword.");
            return;
        }
        printTable(matches);
    }


    private void updateBook() {
        System.out.println("\n--- Update a book ---");
        String isbn = input.readNonEmpty("Enter ISBN of the book to update: ");
        try {
            Book existing = service.findByIsbn(isbn);
            System.out.println("Leave a field blank and press Enter to keep the current value.");
            String title = input.readKeepCurrent(
                    "New title (" + existing.getTitle() + "): ", existing.getTitle());
            String author = input.readKeepCurrent(
                    "New author (" + existing.getAuthor() + "): ", existing.getAuthor());
            int year = input.readYear(
                    "New publication year (" + existing.getPublicationYear() + "): ",
                    existing.getPublicationYear());

            service.updateBook(isbn, title, author, year);
            System.out.println("Book updated successfully.");
        } catch (BookNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private void deleteBook() {
        System.out.println("\n--- Delete a book ---");
        String isbn = input.readNonEmpty("Enter ISBN of the book to delete: ");
        try {
            Book b = service.findByIsbn(isbn);
            System.out.println("About to delete: " + b.getTitle() + " (" + b.getIsbn() + ")");
            String confirm = input.readNonEmpty("Type YES to confirm: ");
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


    private void printTable(List<Book> books) {
        System.out.println(Book.TABLE_HEADER);
        System.out.println(Book.TABLE_SEPARATOR);
        for (Book b : books) {
            System.out.println(b);
        }
    }
}
