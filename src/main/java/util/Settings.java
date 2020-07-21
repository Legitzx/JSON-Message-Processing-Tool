package util;

import config.Config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

    private boolean searchRegexActive;
    private boolean startRegexActive;

    private final String inputFilePath;
    private final String outputFilePath;
    private HashMap<Date, Date> timestampsMap;
    private HashMap<String, Boolean> searchKeywordMap;
    private final String startKeyword;
    private final boolean multiLine;

    private final String dateFormat = "MM-dd-yyyy hh:mm:ss";

    private static final Logger LOGGER = Logger.getLogger(Settings.class.getName());

    public Settings(String inputFilePath, String outputFilePath, String startKeyword, boolean multiLine) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;

        this.timestampsMap = getTimestamps();

        this.searchKeywordMap = getSearchKeywords();
        this.startKeyword = startKeyword;

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
        if(searchKeywordMap.isEmpty()) {
            searchKeywordActive = false;
        } else {
            searchKeywordActive = true;
        }

        if(searchKeywordActive) {
            searchRegexActive = false;
            for(Map.Entry<String, Boolean> set : searchKeywordMap.entrySet()) {
                if(set.getValue() == true) {
                    searchRegexActive = true;
                    break;
                }
            }
        }

        if(startKeyword == null || startKeyword.isEmpty()) {
            startKeywordActive = false;
        } else {
            startKeywordActive = true;
        }

        if(timestampsMap.isEmpty()) {
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

        if(startKeywordActive) {
            startRegexActive = isRegex(startKeyword);
        }

        if(!searchKeywordActive && !startKeywordActive && !timeSearchActive) {
            // Something is wrong since all the input values are null
            // throw some error and handle it
            LOGGER.severe("Failed to get values from config. Did you forget to edit the config.txt?");
        }
    }

    /**
     * Determines whether or not a value is a regex
     * @param value         value
     * @return              true if the value is a regex
     */
    private boolean isRegex(String value) {
        boolean isActive = false;

        if(value.startsWith("regex(")) {
            if(value.endsWith(")")) {
                // Ok its a regex
                isActive = true;
            }
        }

        return isActive;
    }

    /**
     * Gets all the search keywords from the config
     * @return      all the search keywords
     */
    private HashMap<String, Boolean> getSearchKeywords() {
        HashMap<String, Boolean> keywords = new HashMap<>();

        String value = "";
        while(value != null) {
            value = Config.get("search_keyword", String.class);

            if(value != null && !value.isEmpty()) {
                //System.out.println(value);
                if(isRegex(value)) {
                    keywords.put(value, true);
                    continue;
                }

                keywords.put(value, false);
            }
        }

        return keywords;
    }

    /**
     * Gets all the start/stop times from the config
     * @return      a hashmap of start/stop times
     */
    private HashMap<Date, Date> getTimestamps() {
        HashMap<Date, Date> timestamps = new HashMap<>();

        Date startKey = new Date();
        Date endValue = new Date();

        while(startKey != null || endValue != null) {
            startKey = rawToDate(Config.get("start_time", String.class));
            endValue = rawToDate(Config.get("stop_time", String.class));

            if(startKey != null || endValue != null) {
                if(startKey == null || endValue == null) {
                    timestamps.put(startKey, endValue);
                } else {
                    if(endValue.after(startKey)) { // Make sure its a proper start/stop time
                        timestamps.put(startKey, endValue);
                    }
                }
            }
        }

        return timestamps;
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

    public HashMap<String, Boolean> getSearchKeywordMap() {
        return searchKeywordMap;
    }

    public String getStartKeyword() {
        return startKeyword;
    }

    public HashMap<Date, Date> getTimestampsMap() {
        return timestampsMap;
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

    public boolean isSearchRegexActive() {
        return searchRegexActive;
    }

    public boolean isStartRegexActive() {
        return startRegexActive;
    }
}