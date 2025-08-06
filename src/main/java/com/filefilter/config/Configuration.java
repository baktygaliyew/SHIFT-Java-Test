package com.filefilter.config;

import java.util.List;

public class Configuration {
    private final List<String> inputFiles;
    private final String outputPath;
    private final String prefix;
    private final boolean appendMode;
    private final boolean shortStatistics;
    private final boolean fullStatistics;

    public Configuration(List<String> inputFiles, String outputPath, String prefix, boolean appendMode, boolean shortStatistics, boolean fullStatistics) {
        this.inputFiles = inputFiles;
        this.outputPath = outputPath;
        this.prefix = prefix;
        this.appendMode = appendMode;
        this.shortStatistics = shortStatistics;
        this.fullStatistics = fullStatistics;
    }

    public List<String> getInputFiles() {
        return inputFiles;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isAppendMode() {
        return appendMode;
    }

    public boolean isShortStatistics() {
        return shortStatistics;
    }

    public boolean isFullStatistics() {
        return fullStatistics;
    }

    public boolean hasStatistics() {
        return shortStatistics || fullStatistics;
    }
}
