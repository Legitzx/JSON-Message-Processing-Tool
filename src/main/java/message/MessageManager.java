package message;

import util.Settings;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The MessageManager program manages the messages. It will filter
 * the messages based on the configs values.
 *
 * @author Luciano Kholos
 */
public class MessageManager {
    private final Settings settings;

    private ArrayList<Message> messages = new ArrayList<>();

    public MessageManager(Settings settings) {
        this.settings = settings;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    /**
     * This method will get all the messages after the start
     * Date and before the end Date.
     * @param start         Date
     * @param end           Date
     * @return              all messages between start and end Dates
     */
    public ArrayList<Message> getMessagesByDate(Date start, Date end) {
        ArrayList<Message> startStopMessages = new ArrayList<>();

        if(start == null && end == null) {
            return null;
        }

        if(start == null) {
            for(Message message : messages) {
                if(message.getDate().before(end) || message.getDate().equals(end)) {
                    startStopMessages.add(message);
                }
            }

            return startStopMessages;
        }

        if(end == null) {
            for(Message message : messages) {
                if(message.getDate().after(start) || message.getDate().equals(start)) {
                    startStopMessages.add(message);
                }
            }

            return startStopMessages;
        }

        for(Message message : messages) {
            if((message.getDate().after(start) && message.getDate().before(end)) || (message.getDate().toString().equals(start.toString()) || message.getDate().toString().equals(end.toString()))) {
                startStopMessages.add(message);
            }
        }

        return startStopMessages;
    }

    /**
     * This method will filter out the messages
     * based on the startKeyword. Every message starting
     * from the startKeyword will be returned.
     * @param startKeyword      keyword used to filter messages
     * @return                  returns every message starting from startKeyword
     */
    public ArrayList<Message> getMessagesByStartKeyword(String startKeyword) {
        ArrayList<Message> keywordMessages = new ArrayList<>();

        // Handle regex (if present)
        if(settings.isStartRegexActive()) {
            String regex = extractRegex(startKeyword);
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher("");

            boolean valid = false;

            for(Message message : messages) {
                if(m.reset(message.getRegexContent()).find()) {
                    valid = true;
                }

                if(valid) {
                    keywordMessages.add(message);
                }
            }

            return keywordMessages;
        }

        boolean started = false;

        if(startKeyword.contains(" ")) { // Handle search strings
            String[] args = startKeyword.split(" ");

            for(Message message : messages) {
                boolean valid = false;

                if(started) {
                    keywordMessages.add(message);
                    continue;
                }

                for(String arg : args) {
                    if(message.getArgs().contains(arg)) {
                        valid = true;
                    } else {
                        valid = false;
                        break;
                    }
                }

                if(valid) {
                    keywordMessages.add(message);
                    started = true;
                }
            }
        } else { // Handle regular keywords
            for(Message message : messages) {
                if(message.getArgs().contains(startKeyword) && !started) {
                    started = true;
                }

                if(started) {
                    keywordMessages.add(message);
                }
            }
        }

        return keywordMessages;
    }

    /**
     * This method will filter out an array
     * of messages based on the specified keyword.
     * @return              ArrayList of filtered messages
     */
    public ArrayList<Message> getMessagesByKeyword() {
        ArrayList<Message> keywordMessages = new ArrayList<>();

        for(Map.Entry<String, Boolean> set : settings.getSearchKeywordMap().entrySet()) {
            String keyword = set.getKey();

            if(set.getValue() == true) { // its a regex -> handle it
                // Handle regex (if present)
                if(settings.isSearchRegexActive()) {
                    String regex = extractRegex(keyword);
                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher("");

                    for(Message message : messages) {
                        if(m.reset(message.getRegexContent()).find()) {
                            keywordMessages.add(message);
                        }
                    }
                }
            } else { // its not a regex (it could be a single string or search string) -> handle it
                if(keyword.contains(" ")) { // Handle search strings
                    String[] args = keyword.split(" ");

                    boolean valid = false;
                    for(Message message : messages) {
                        for(String arg : args) {
                            if(message.getArgs().contains(arg)) {
                                valid = true;
                            } else {
                                valid = false;
                                break;
                            }
                        }

                        if(valid) {
                            keywordMessages.add(message);
                            valid = false;
                        }
                    }
                } else { // Handle keywords without whitespace
                    for(Message message : messages) {
                        if(message.getArgs().contains(keyword)) {
                            keywordMessages.add(message);
                        }
                    }
                }
            }
        }

        // Removes duplicates
        Set<Message> set = new LinkedHashSet<>(keywordMessages);
        keywordMessages.clear();
        keywordMessages.addAll(set);

        return keywordMessages;
    }

    /**
     * Extracts the regex from a raw value
     * Raw: regex(someRegex)
     * Extracted: someRegex
     * @param value        raw regex
     * @return             extracted regex
     */
    private String extractRegex(String value) {
        value = value.substring(6);
        return value.substring(0, value.length() - 1);
    }

    public ArrayList<Message> getMessages() {
        return this.messages;
    }

    public void updateMessages(ArrayList<Message> messages) {
        this.messages.clear();
        this.messages.addAll(messages);
    }
}