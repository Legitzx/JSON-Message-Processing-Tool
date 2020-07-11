package processing;

import com.google.gson.*;
import message.Message;
import message.MessageManager;
import util.Settings;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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

        manager = new MessageManager();
    }

    /**
     * This method will iterate through the input file
     * and load the messages into the MessageManagers
     * container.
     * @throws IOException  if input file does not exist
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

                    /*
                    Timestamp of JSON Object
                    Also replaces " that are present after parsing
                     */
                    String timestampRaw = timestamp.toString().replaceAll("\"", "");

                    manager.addMessage(new Message(originalJsonLine, Arrays.asList(strippedJsonLine.split(" ")), settings.rawToDate(timestampRaw)));
                }
            }

        } catch (Exception e) {
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
            messages.addAll(manager.getMessagesByDate(settings.getStart(), settings.getEnd()));
            manager.updateMessages(messages);
            messages.clear();
        }

        if(settings.isStartKeywordActive()) {
            messages.addAll(manager.getMessagesByStartKeyword(settings.getStartKeyword()));
            manager.updateMessages(messages);
            messages.clear();
        }

        if(settings.isSearchKeywordActive()) {
            messages.addAll(manager.getMessagesByKeyword(settings.getSearchKeyword()));
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
}