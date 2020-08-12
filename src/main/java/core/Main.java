package core;

import config.Config;
import processing.JsonProcessingTool;
import util.Settings;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * This program will process the content
 * inside of the input file and output
 * data based on the configs constraints.
 *
 * @author Luciano Kholos
 * @version 0.0.2
 */
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        JsonProcessingTool tool = new JsonProcessingTool(new Settings(
                Config.get("input_file", String.class),
                Config.get("output_file", String.class),
                Config.get("start_keyword", String.class),
                Config.get("multi_line_output", Boolean.class)
        ));

        try {
            tool.loadJsonMessages();
            tool.process();
        } catch (IOException e) {
            LOGGER.severe("Invalid File Path");
        }

        long stop = System.currentTimeMillis() - start;

        System.out.println("\n" + "Execution Time: " + stop / 1000.0 + " seconds");
    }
}