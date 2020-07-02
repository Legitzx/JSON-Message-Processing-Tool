package processing;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import config.Config;
import message.Message;
import message.MessageManager;
import util.Settings;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * The JsonProcessingTool program will load the
 * messages into the MessageManager and filter
 * the data into the output file.
 *
 * @author Luciano Kholos
 */
public class JsonProcessingTool {
    private final String path;
    private final Settings settings;

    private final MessageManager manager;

    public JsonProcessingTool(String path, Settings settings) {
        this.path = path;
        this.settings = settings;

        manager = new MessageManager();
    }

    /**
     * This method will iterate through the input file
     * and load the messages into the MessageManagers
     * container.
     * @throws IOException
     */
    public void loadJsonMessages() throws IOException {
        File file = new File(path);
        Pattern pattern = Pattern.compile("[{}\",:]");

        JsonObject jsonObject;

        // Iterates through each line
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while((line = reader.readLine()) != null) {
                String originalLine = line;

                // Converting String to JSONObject
                jsonObject = JsonParser.parseString(line).getAsJsonObject();
                String timestamp = jsonObject.get("timeStamp").getAsString();

                // Replaces all unnecessary items
                line = line.replaceAll(pattern.toString(), " ");

                // Adds the message to the manager
                manager.addMessage(new Message(originalLine, Arrays.asList(line.split(" ")), settings.rawToDate(timestamp)));
            }
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

        System.out.println(messages);
        try(FileWriter writer = new FileWriter(Config.get("output_file", String.class))) {
            for(Message message : messages) {
                writer.append(message.getContent() + "\n");
            }
        }
    }
}