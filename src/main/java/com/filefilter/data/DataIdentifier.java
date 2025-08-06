package com.filefilter.data;

public class DataIdentifier {
    public static DataType identify(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String trimmedLine = line.trim();

        if (isInteger(trimmedLine)) {
            return DataType.INTEGER;
        }

        if (isFloat(trimmedLine)) {
            return DataType.FLOAT;
        }

        return DataType.STRING;
    }

    public static boolean isInteger(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isFloat(String str) {
        try {
            Double.parseDouble(str);
            return str.contains(".") || str.toLowerCase().contains("e");
        } catch (NumberFormatException e) {
            return false;
        }
    }




}
