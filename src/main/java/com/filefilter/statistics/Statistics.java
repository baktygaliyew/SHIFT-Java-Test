package com.filefilter.statistics;

import com.filefilter.data.DataType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Statistics {
    private final Map<DataType, TypeStatistics> stats = new HashMap<>();

    public void addInteger(long value) {
        getOrCreateStats(DataType.INTEGER).addNumber(new BigDecimal(value));
    }

    public void addFloat(double value) {
        getOrCreateStats(DataType.FLOAT).addNumber(new BigDecimal(value));
    }

    public void addString(String value) {
        getOrCreateStats(DataType.STRING).addString(value);
    }

    private TypeStatistics getOrCreateStats(DataType dataType) {
        return stats.computeIfAbsent(dataType, k -> new TypeStatistics());
    }

    public void printStatistics(boolean fullStatistics) {
        if (stats.isEmpty()) {
            System.out.println("Statistics: no data processed");
            return;
        }

        System.out.println("\nStatistics:");

        for (DataType type : DataType.values()) {
            TypeStatistics statistics = stats.get(type);
            if (statistics != null && statistics.getCount() > 0) {
                System.out.println("\n" + capitalizeFirst(type.getDescription()) + ":");
                statistics.print(type, fullStatistics);
            }
        }
    }

    private String capitalizeFirst(String str) {
        if (str.isEmpty()) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    private static class TypeStatistics {
        private int count = 0;
        private BigDecimal min;
        private BigDecimal max;
        private BigDecimal sum = BigDecimal.ZERO;
        private int minStringLength = Integer.MAX_VALUE;
        private int maxStringLength = 0;

        public void addNumber(BigDecimal value) {
            count++;
            sum = sum.add(value);

            if (min == null || value.compareTo(min) < 0) {
                min = value;
            }
            if (max == null || value.compareTo(max) > 0) {
                max = value;
            }
        }

        public void addString(String value) {
            count++;
            int length = value.length();

            if (length < minStringLength) {
                minStringLength = length;
            }
            if (length > maxStringLength) {
                maxStringLength = length;
            }
        }

        public int getCount() {
            return count;
        }

        public void print(DataType type, boolean fullStatistics) {
            System.out.println("  Number of elements: " + count);

            if (fullStatistics) {
                if (type == DataType.INTEGER || type == DataType.FLOAT) {
                    printNumberStatistics();
                } else if (type == DataType.STRING) {
                    printStringStatistics();
                }
            }
        }

        private void printNumberStatistics() {
            if (count > 0) {
                System.out.println("  Minimum value: " + formatNumber(min));
                System.out.println("  Maximum value: " + formatNumber(max));
                System.out.println("  Sum: " + formatNumber(sum));

                BigDecimal average = sum.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP);
                System.out.println("  Average value: " + formatNumber(average));
            }
        }

        private void printStringStatistics() {
            if (count > 0) {
                System.out.println("  Shortest line length: " + minStringLength);
                System.out.println("  Longest line length: " + maxStringLength);
            }
        }

        private String formatNumber(BigDecimal number) {
            return number.stripTrailingZeros().toPlainString();
        }
    }
}