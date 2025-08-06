package com.filefilter.config;

import java.util.ArrayList;
import java.util.List;

public class ArgumentParser {

    public static Configuration parse(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No input files specified");
        }

        String outputPath = ".";
        String prefix = "";
        boolean appendMode = false;
        boolean shortStatistics = false;
        boolean fullStatistics = false;
        List<String> inputFiles = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.equals("-o")) {
                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("Option -o requires path to be specified");
                }
                outputPath = args[++i];
            } else if (arg.equals("-p")) {
                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("Option -p requires prefix");
                }
                prefix = args[++i];
            } else if (arg.equals("-a")) {
                appendMode = true;
            } else if (arg.equals("-s")) {
                shortStatistics = true;
            } else if (arg.equals("-f")) {
                fullStatistics = true;
            } else if (arg.startsWith("-")) {
                throw new IllegalArgumentException("Unknown option: " + arg);
            } else {
                inputFiles.add(arg);
            }

        }

        if (inputFiles.isEmpty()) {
            throw new IllegalArgumentException("No input files specified");
        }

        return new Configuration(inputFiles, outputPath, prefix, appendMode, shortStatistics, fullStatistics);
    }
}
