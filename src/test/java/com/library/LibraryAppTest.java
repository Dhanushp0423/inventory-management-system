package com.library;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LibraryApp command-line tests")
class LibraryAppTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream console;

    @BeforeEach
    void redirectStreams() {
        console = new ByteArrayOutputStream();
        System.setOut(new PrintStream(console));
    }

    @AfterEach
    void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    /** Runs the app with the given simulated keystrokes and returns its full output. */
    private String runAppWith(String simulatedInput) {
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        LibraryApp.main(new String[0]);
        return console.toString();
    }

    @Test
    @DisplayName("an invalid menu choice is rejected and the app keeps running")
    void invalidMenuChoiceIsHandled() {
        String output = runAppWith("9\n0\n");
        assertTrue(output.contains("Invalid option"));
        assertTrue(output.contains("Goodbye"));
    }

    @Test
    @DisplayName("adding a book through the menu prints the success message")
    void addBookThroughMenu() {
        String output = runAppWith("1\nClean Code\nRobert C. Martin\n9780132350884\n2008\n0\n");
        assertTrue(output.contains("Book added successfully."));
    }

    @Test
    @DisplayName("listing an empty inventory prints the empty-state message")
    void listEmptyInventory() {
        String output = runAppWith("2\n0\n");
        assertTrue(output.contains("The inventory is empty."));
    }

    @Test
    @DisplayName("a non-numeric publication year is rejected and re-asked")
    void invalidYearIsRejectedAndReprompted() {
        String output = runAppWith("1\nT\nA\nISBN-1\nnotANumber\n2005\n0\n");
        assertTrue(output.contains("not a valid number"));
        assertTrue(output.contains("Book added successfully."));
    }
}
