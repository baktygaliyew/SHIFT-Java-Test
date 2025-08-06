package com.filefilter.data;

public enum DataType {
    INTEGER("integer.txt", "целые числа"),
    FLOAT("floats.txt", "вещественные числа"),
    STRING("strings.txt", "строки");

    private final String defaultFileName;
    private final String description;

    DataType(String defaultFileName, String description) {
        this.defaultFileName = defaultFileName;
        this.description = description;
    }

    public String getDefaultFileName() {
        return defaultFileName;
    }

    public String getDescription() {
        return description;
    }
}
