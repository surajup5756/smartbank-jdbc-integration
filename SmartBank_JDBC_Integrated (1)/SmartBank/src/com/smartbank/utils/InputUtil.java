package com.smartbank.utils;

import java.math.BigDecimal;
import java.util.Scanner;

public final class InputUtil {
    private InputUtil() { }

    public static String readNonEmpty(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    public static BigDecimal readMoney(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            try {
                BigDecimal amount = new BigDecimal(value);
                if (amount.scale() > 2) {
                    System.out.println("Enter an amount with maximum 2 decimal places.");
                    continue;
                }
                return amount;
            } catch (NumberFormatException ex) {
                System.out.println("Invalid amount. Example: 2500 or 2500.50");
            }
        }
    }

    public static int readChoice(Scanner scanner) {
        System.out.print("Choose an option: ");
        String value = scanner.nextLine().trim();
        try { return Integer.parseInt(value); }
        catch (NumberFormatException ex) { return -1; }
    }
}
