package config;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * The ConfigParser program extracts data from the config.txt file and
 * returns it to the user.
 *
 * @author Luciano Kholos
 */
public class ConfigParser {
    private static final String filePath = System.getProperty("user.dir") + "\\config.txt";
    private static ArrayList<String> configContent = new ArrayList<>();

    private static final Logger LOGGER = Logger.getLogger(ConfigParser.class.getName());

    private ConfigParser(ArrayList<String> arrayList) {
        configContent = arrayList;
    }

    /**
     * Loads the configs content into an array (caching)
     * @throws IOException      if config file does not exist
     */
    public static ConfigParser load() {
        File file = new File(filePath);

        // Caches each line from text file to array
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while((line = reader.readLine()) != null) {
                configContent.add(line);
            }
        } catch (IOException e) { // If the file does not exist -> wipe array
            configContent.clear();
        }

        return new ConfigParser(configContent);
    }

    /**
     * This method will get a value from the specified key
     * @param key           config key
     * @param type          type of value
     * @param <T>
     * @return
     */
    public < T > T get(String key, Class<T> type) {
        if(configContent.isEmpty()) {
            if(type == Boolean.class) {
                return type.cast(false);
            }
            return null;
        }

        /*
        Iterates through each "line" in the array.
        It then determines whether or not the line
        has the valid key, if so it will attempt to
        grab the value.
         */
        for(String line : configContent) {
            if(isValidKey(key, line)) {
                // The key is valid -> now get the value
                String value = getValue(line);

                if(value == null || value.isEmpty()) {
                    if(type == Boolean.class) {
                        return type.cast(false);
                    }
                    return null;
                }

                // Handles Boolean types
                if(type == Boolean.class) {
                    // Checks to see if the value is true or false
                    if(!(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))) {
                        LOGGER.severe("[Boolean] Malformed data provided for key: " + key + " - Expected: true or false.");
                        return null;
                    }
                    boolean bool = Boolean.parseBoolean(value);
                    return type.cast(bool);
                }

                // Handles String types
                if(type == String.class) {
                    return type.cast(value);
                }

                // Handles Integer types
                if(type == Integer.class) {
                    try {
                        int intValue = Integer.parseInt(value);
                        return type.cast(intValue);
                    } catch (Exception e) {
                        LOGGER.severe("[Integer] Malformed data provided for key: " + key + " - Expected: 0 - 9");
                        return null;
                    }
                }
            }
        }

        /*
        Returns false when it cannot find a value
        for a key. Only used for booleans since booleans
        cannot be null.
         */
        if(type == Boolean.class) {
            return type.cast(false);
        }

        return null;
    }

    /**
     * This method will attempt to find a match between key and line
     * @param key           search key
     * @param line          config line
     * @return              true if there is a match & false if no match
     */
    private boolean isValidKey(String key, String line) {
        boolean isValid = false;

        char[] keyChars = key.toCharArray();
        char[] lineChars = line.toCharArray();

        int keyLength = keyChars.length;
        int lineLength = lineChars.length;

        /*
        Basically just attempts to see if the line contains a key
         */
        for(int x = 0; x < lineLength; x++) {

            if(x == lineLength || x == keyLength) { // Prevents ArrayOutOfBoundsException
                break;
            }

            if(lineChars[x] == '#') {
                break;
            }

            if(lineChars[x] == '=') {
                break;
            }

            if(lineChars[x] == ' ') {
                isValid = false;
                break;
            }

            if(keyChars[x] == lineChars[x]) {
                isValid = true;
                continue;
            } else {
                isValid = false;
                break;
            }
        }

        return isValid;
    }

    /**
     * This method will extract the value from the line
     * @param line      config line
     * @return          the value extracted from the line
     */
    private String getValue(String line) {
        char[] chars = line.toCharArray();
        StringBuilder builder = new StringBuilder();

        boolean capture = false;

        for(char c : chars) {
            if(c == '#') {
                break;
            }

            if(c == '=') {
                capture = true;
                continue;
            }

            if(capture) {
                builder.append(c);
            }
        }

        return builder.toString();
    }
}