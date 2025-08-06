package com.filefilter.processor;

import com.filefilter.config.Configuration;
import com.filefilter.data.DataIdentifier;
import com.filefilter.data.DataType;
import com.filefilter.output.FileWriterManager;
import com.filefilter.statistics.Statistics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileProcessor {
    private final Configuration configuration;
    private final Statistics statistics;

    public FileProcessor(Configuration configuration) {
        this.configuration = configuration;
        this.statistics = new Statistics();
    }

    public void process() {
        try (FileWriterManager writerManager = new FileWriterManager(configuration)) {
            boolean hasErrors = false;
            int processedFiles = 0;

            for (String inputFile: configuration.getInputFiles()) {
                try {
                    processFile(inputFile, writerManager);
                    processedFiles++;
                } catch (IOException e) {
                    System.err.println("Error while processing file '" + inputFile + "': " + e.getMessage());
                    hasErrors = true;
                }
            }

            writerManager.flush();

            if (processedFiles > 0) {
                System.out.println("The files have been processed successfully: " + processedFiles + " from " + configuration.getInputFiles().size());
            } else {
                System.err.println("No file was processed successfully");
            }

            if (configuration.hasStatistics()) {
                statistics.printStatistics(configuration.isFullStatistics());
            }

            if (hasErrors) {
                System.err.println("\nWarning: some files have errors");
            }
        } catch (Exception e) {
            System.err.println("Critical error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void processFile(String fileName, FileWriterManager fileWriterManager) throws IOException {
        if (!Files.exists(Paths.get(fileName))) {
            throw new IOException("File not found: " + fileName);
        }

        if(!Files.isReadable(Paths.get(fileName))) {
            throw new IOException("File is not readable: " + fileName);
        }

        int lineNumber = 0;
        int processedLines = 0;
        int skippedLines = 0;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while((line = bufferedReader.readLine()) != null) {
                lineNumber++;

                try {
                    if (processLine(line, fileWriterManager)) {
                        processedLines++;
                    } else {
                        skippedLines++;
                    }
                } catch (Exception e) {
                    System.err.println("Error while processing the string " + lineNumber + " in file '" + fileName + "': " + e.getMessage());
                    skippedLines++;
                }
            }
            System.out.println("File '" + fileName + "': processed strings " + processedLines +
                    (skippedLines > 0 ? ", skipped " + skippedLines : ""));
        } catch (IOException e) {
            throw new IOException("Error while reading file '" + fileName + "': " + e.getMessage());
        }
    }


    private boolean processLine(String line, FileWriterManager fileWriterManager) throws IOException {
        DataType dataType = DataIdentifier.identify(line);

        if (dataType == null) {
            return false;
        }

        String trimmedLine = line.trim();

        fileWriterManager.writeLine(dataType, trimmedLine);

        collectStatistics(dataType, trimmedLine);

        return true;
    }

    private void collectStatistics(DataType dataType, String value) {
        switch (dataType) {
            case INTEGER:
                try {
                    statistics.addInteger(Long.parseLong(value));
                } catch (NumberFormatException e) {
                    System.err.println("Unexpected error when parsing an integer: " + value);
                }
                break;
            case FLOAT:
                try {
                    statistics.addFloat(Double.parseDouble(value));
                } catch (NumberFormatException e) {
                    System.err.println("Unexpected error when parsing an integer: " + value);
                }
                break;
            case STRING:
                statistics.addString(value);
                break;
        }
    }


}