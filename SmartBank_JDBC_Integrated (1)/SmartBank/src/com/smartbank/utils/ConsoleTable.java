package com.smartbank.utils;

import java.util.ArrayList;
import java.util.List;

public final class ConsoleTable {
    private ConsoleTable() { }

    // Work out each column width first so every row lines up in the terminal.
    public static void print(String[] headers, List<String[]> rows) {
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) widths[i] = headers[i].length();
        for (String[] row : rows) {
            for (int i = 0; i < headers.length; i++) {
                String value = i < row.length && row[i] != null ? row[i] : "";
                widths[i] = Math.max(widths[i], value.length());
            }
        }
        String border = border(widths);
        System.out.println(border);
        printRow(headers, widths);
        System.out.println(border);
        for (String[] row : rows) printRow(row, widths);
        System.out.println(border);
        if (rows.isEmpty()) System.out.println("(No records found)");
    }

    private static String border(int[] widths) {
        StringBuilder result = new StringBuilder("+");
        for (int width : widths) {
            for (int i = 0; i < width + 2; i++) result.append('-');
            result.append('+');
        }
        return result.toString();
    }

    private static void printRow(String[] values, int[] widths) {
        StringBuilder result = new StringBuilder("|");
        for (int i = 0; i < widths.length; i++) {
            String value = i < values.length && values[i] != null ? values[i] : "";
            result.append(' ').append(value);
            for (int j = value.length(); j < widths[i] + 1; j++) result.append(' ');
            result.append('|');
        }
        System.out.println(result);
    }
}
