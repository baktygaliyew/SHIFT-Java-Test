package com.filefilter;

import com.filefilter.config.ArgumentParser;
import com.filefilter.config.Configuration;
import com.filefilter.processor.FileProcessor;

public class FileFilterUtility {
    public static void main(String[] args) {
        try {
            Configuration configuration = ArgumentParser.parse(args);
            FileProcessor processor = new FileProcessor(configuration);
            processor.process();
        } catch (IllegalArgumentException e) {
            System.err.println("Error in command line arguments: " + e.getMessage());
            printUsage();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void printUsage() {
        System.out.println("\nUsage:");
        System.out.println("java -jar file-filter-utility.jar [options] file1 file2 ...");
        System.out.println("\nOptions:");
        System.out.println("  -o <path>    Path to output files (default: current folder)");
        System.out.println("  -p <prefix>  Output file name prefix (default: empty)");
        System.out.println("  -a           Mode of adding to existing files");
        System.out.println("  -s           Short statistics");
        System.out.println("  -f           Full statistics");
        System.out.println("\nExamples:");
        System.out.println("  java -jar file-filter-utility.jar -s input1.txt input2.txt");
        System.out.println("  java -jar file-filter-utility.jar -o /tmp -p result_ -f data.txt");
    }
}
