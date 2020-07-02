import config.Config;
import processing.JsonProcessingTool;
import util.Settings;

import java.io.IOException;

/**
 * This program will process the content
 * inside of the input file and output
 * data based on the configs constraints.
 *
 * @author Luciano Kholos
 */
public class Main {
    public static void main(String[] args) {
        JsonProcessingTool tool = new JsonProcessingTool(Config.get("input_file", String.class), new Settings(
                Config.get("search_keyword", String.class),
                Config.get("start_keyword", String.class),
                Config.get("start_time", String.class),
                Config.get("stop_time", String.class)
        ));

        try {
            tool.loadJsonMessages();
            tool.process();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}