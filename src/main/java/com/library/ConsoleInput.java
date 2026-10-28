package com.library;

import java.util.Scanner;


public class ConsoleInput {
    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2026;

    private final Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }


    public String readRaw(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }


    public String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("This field cannot be empty. Please try again.");
        }
    }


    public String readKeepCurrent(String prompt, String current) {
        String input = readRaw(prompt).trim();
        return input.isEmpty() ? current : input;
    }


    public int readYear(String prompt, Integer current) {
        while (true) {
            String line = readRaw(prompt).trim();

            if (line.isEmpty() && current != null) {
                return current;   // update mode: blank keeps the old year
            }
            try {
                int year = Integer.parseInt(line);
                if (year < MIN_YEAR || year > MAX_YEAR) {
                    System.out.println("Please enter a realistic year between "
                            + MIN_YEAR + " and " + MAX_YEAR + ".");
                    continue;
                }
                return year;
            } catch (NumberFormatException e) {
                System.out.println("That is not a valid number. Please try again.");
            }
        }
    }
}
