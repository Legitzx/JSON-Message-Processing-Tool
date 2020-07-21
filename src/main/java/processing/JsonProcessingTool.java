package processing;

import com.google.gson.*;
import message.Message;
import message.MessageManager;
import util.Settings;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * The JsonProcessingTool program will load the
 * messages into the MessageManager and filter
 * the data into the output file.
 *
 * @author Luciano Kholos
 */
public class JsonProcessingTool {
    private final Settings settings;

    private final MessageManager manager;

    private static final Logger LOGGER = Logger.getLogger(JsonProcessingTool.class.getName());

    public JsonProcessingTool(Settings settings) {
        this.settings = settings;

        manager = new MessageManager(settings);
    }

    /**
     * This method will iterate through the input file
     * and load the messages into the MessageManagers
     * container.
     */
    public void loadJsonMessages() {
        Pattern pattern = Pattern.compile("[{}\",:]");

        if(!settings.doesInputExist()) {
            LOGGER.severe("Could not find [input.txt] make sure it is in the directory of the .jar file!");
            return;
        }

        try (InputStream is = new FileInputStream(settings.getInputFilePath());
             Reader r = new InputStreamReader(is, "UTF-8");) {

            JsonStreamParser p = new JsonStreamParser(r);

            while(p.hasNext()) { // Loops through each JSON object in the file
                JsonElement e = p.next();
                if (e.isJsonObject()) { // Validates the object
                    JsonObject obj = e.getAsJsonObject();  // Converts it to a JSON Object
                    JsonElement timestamp = obj.get("timeStamp"); // Gets the timeStamp

                    String originalJsonLine = e.toString(); // Un-edited JSON Object
                    String strippedJsonLine = originalJsonLine; // Edited JSON Object
                    strippedJsonLine = strippedJsonLine.replaceAll(pattern.toString(), " ");

                    //Timestamp of JSON Object - Also replaces " that are present after parsing
                    String timestampRaw = timestamp.toString().replaceAll("\"", "");

                    List<String> args = extractArgs(Arrays.asList(strippedJsonLine.split(" ")));

                    String regexStr = arrayToString(args);

                    // Adds the message to the manager
                    manager.addMessage(new Message(originalJsonLine, regexStr, args, settings.rawToDate(timestampRaw)));
                }
            }

        } catch (Exception e) { // TODO: Make it so if malformed data is provided in the middle of a config, it will skip it and continue - as of right now it will just exit once it detects malformed data
            LOGGER.severe("Malformed data provided in input.txt");
        }
    }

    /**
     * This method will run the MessageManagers methods on the
     * input files messages. It will then filter and extract
     * that data into the output file.
     * @throws IOException      if output file does not exist
     */
    public void process() throws IOException {
        ArrayList<Message> messages = new ArrayList<>();

        if(settings.isTimeSearchActive()) {
            messages.addAll(manager.getMessagesByDate());
            manager.updateMessages(messages);
            messages.clear();
        }

        if(settings.isStartKeywordActive()) {
            messages.addAll(manager.getMessagesByStartKeyword(settings.getStartKeyword()));
            manager.updateMessages(messages);
            messages.clear();
        }

        if(settings.isSearchKeywordActive()) {
            messages.addAll(manager.getMessagesByKeyword());
            manager.updateMessages(messages);
            messages.clear();
        }

        messages = manager.getMessages();

        if(!settings.doesOutputExist()) {
            LOGGER.severe("Could not find [output.txt] make sure it is in the directory of the .jar file!");
            return;
        }

        LOGGER.info("Found " + messages.size() + " message(s) that fit your specifications.");

        if(settings.isMultiLine()) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            try(FileWriter writer = new FileWriter(settings.getOutputFilePath())) {
                for(Message message : messages) {
                    JsonElement obj = JsonParser.parseString(message.getContent());

                    String prettyJson = gson.toJson(obj);

                    writer.append(prettyJson + "\n");
                }
            }
        } else {
            try(FileWriter writer = new FileWriter(settings.getOutputFilePath())) {
                for(Message message : messages) {
                    writer.append(message.getContent() + "\n");
                }
            }
        }
    }

    /**
     * Extracts the properties/body from the raw json message
     * @param args      raw message (in array form)
     * @return          properties/body of the message
     */
    private List<String> extractArgs(List<String> args) {
        List<String> noSpacesArray = new ArrayList<>();
        List<String> finalArray = new ArrayList<>();

        // Strips array elements that are empty
        for(String arg : args) {
            if(!arg.isEmpty()) {
                noSpacesArray.add(arg);
            }
        }

        /*
        Skips the first 8 elements of the array - Also removes the "body" key.
        Input: 0: timeStamp 1: 07-01-2020 2: 01 3: 59 4: 51 5: delayMillis 6: 13953 7: properties 8: body 9: This 10: is 11: a 12: search 13: string
        Output: 8: body 9: This 10: is 11: a 12: search 13: string
         */
        for(int x = 0; x < noSpacesArray.size(); x++) {
            if(x > 7) {
                if(!noSpacesArray.get(x).equalsIgnoreCase("body")) {
                    //System.out.print(x + ": " + noSpacesArray.get(x) + " ");
                    finalArray.add(noSpacesArray.get(x));
                }
            }
        }

        //System.out.println();
        return finalArray;
    }

    /**
     * Converts Lists to Strings
     * @param arr       List
     * @return          String
     */
    private String arrayToString(List<String> arr) {
        StringBuilder builder = new StringBuilder();

        for(String str : arr) {
            builder.append(str + " ");
        }

        return builder.toString();
    }
}