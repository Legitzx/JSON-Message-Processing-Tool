import config.Config;
import config.ConfigParser;
import core.Main;
import org.junit.Assert;
import org.junit.Test;
import processing.JsonProcessingTool;
import util.Settings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class StartKeywordTest {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    @Test
    public void startKeywordTest() throws Exception {
        ConfigParser.filePath = "C:\\Users\\Luciano\\Desktop\\Programming\\Boeing\\Projects\\JSONProcessingTool\\src\\test\\resources\\startConfigTest.txt";

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

        Assert.assertEquals(6, countOutputLines());
    }


    // Counts the amount of lines in the output file
    private int countOutputLines() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Luciano\\Desktop\\Programming\\Boeing\\Projects\\JSONProcessingTool\\output.txt"));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();

        return lines;
    }
}
