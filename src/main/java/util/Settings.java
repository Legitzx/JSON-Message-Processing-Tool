package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * The Settings program will examine the config
 * file and determine which parts of the
 * JsonProcessingTool will run.
 *
 * @author Luciano Kholos
 */
public class Settings {
    private boolean searchKeywordActive;
    private boolean startKeywordActive;
    private boolean timeSearchActive;
    private boolean inputExists;
    private boolean outputExists;

    private final String inputFilePath;
    private final String outputFilePath;
    private final Date start;
    private final Date end;
    private final String searchKeyword;
    private final String startKeyword;
    private final boolean multiLine;

    private final String dateFormat = "MM-dd-yyyy hh:mm:ss";

    private static final Logger LOGGER = Logger.getLogger(Settings.class.getName());

    public Settings(String inputFilePath, String outputFilePath, String searchKeyword, String startKeyword, String startRaw, String endRaw, boolean multiLine) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;

        this.searchKeyword = searchKeyword;
        this.startKeyword = startKeyword;

        this.start = rawToDate(startRaw);
        this.end = rawToDate(endRaw);

        this.multiLine = multiLine;

        setUp();
    }

    /**
     * Converts strings dates to Date
     * @param raw       string date
     * @return          Date
     */
    public Date rawToDate(String raw) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

        Date date;
        try {
            date = simpleDateFormat.parse(raw);
        } catch (Exception e) {
            date = null;
        }

        return date;
    }

    /**
     * This method will determine which parts of the JsonProcessingTool
     * will run based off the values of the variables.
     */
    private void setUp() {
        if(searchKeyword == null || searchKeyword.isEmpty()) {
            searchKeywordActive = false;
        } else {
            searchKeywordActive = true;
        }

        if(startKeyword == null || startKeyword.isEmpty()) {
            startKeywordActive = false;
        } else {
            startKeywordActive = true;
        }

        if(start == null && end == null) {
            timeSearchActive = false;
        } else {
            timeSearchActive = true;
        }

        if(inputFilePath == null || inputFilePath.isEmpty()) {
            inputExists = false;
        } else {
            inputExists = true;
        }

        if(outputFilePath == null || outputFilePath.isEmpty()) {
            outputExists = false;
        } else {
            outputExists = true;
        }

        if(!searchKeywordActive && !startKeywordActive && !timeSearchActive) {
            // Something is wrong since all the input values are null
            // throw some error and handle it
            LOGGER.severe("Failed to get values from config. Did you forget to edit the config.txt?");
        }
    }

    public boolean isSearchKeywordActive() {
        return searchKeywordActive;
    }

    public boolean isStartKeywordActive() {
        return startKeywordActive;
    }

    public boolean isTimeSearchActive() {
        return timeSearchActive;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public String getStartKeyword() {
        return startKeyword;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public boolean isMultiLine() {
        return multiLine;
    }

    public boolean doesInputExist() {
        return inputExists;
    }

    public boolean doesOutputExist() {
        return outputExists;
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }
}