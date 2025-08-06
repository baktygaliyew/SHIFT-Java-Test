package com.filefilter.output;

import com.filefilter.config.Configuration;
import com.filefilter.data.DataType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileWriterManager implements AutoCloseable {

    private final Configuration configuration;
    private final Map<DataType, BufferedWriter> writers = new HashMap<>();

    public FileWriterManager(Configuration configuration) {
        this.configuration = configuration;
    }

    public void writeLine(DataType dataType, String line) throws IOException {
        var writer = getWriter(dataType);
        writer.write(line);
        writer.newLine();
    }

    private BufferedWriter getWriter(DataType dataType) throws IOException {
        return writers.computeIfAbsent(dataType, this::createWriter);
    }

    private BufferedWriter createWriter(DataType dataType) {
        try {
            Path outputPath = Paths.get(configuration.getOutputPath());
            String fileName = configuration.getPrefix() + dataType.getDefaultFileName();
            Path filePath = outputPath.resolve(fileName);

            if (outputPath.getParent() != null) {
                outputPath.toFile().mkdirs();
            }

            FileWriter fileWriter = new FileWriter(filePath.toFile(), configuration.isAppendMode());
            return new BufferedWriter(fileWriter);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create file for type " + dataType.getDescription() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void close() throws Exception {
        for (BufferedWriter writer : writers.values()) {
            try {
                writer.close();
            } catch (IOException e) {
                System.err.println("Error while closing file: " + e.getMessage());
            }
        }
        writers.clear();
    }

    public void flush() throws IOException {
        for (BufferedWriter writer : writers.values()) {
            writer.flush();
        }
    }
}
