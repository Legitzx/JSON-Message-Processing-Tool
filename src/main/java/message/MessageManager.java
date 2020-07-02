package message;

import java.util.ArrayList;
import java.util.Date;

/**
 * The MessageManager program manages the messages. It will a range
 * of filter message arrays.
 *
 * @author Luciano Kholos
 */
public class MessageManager {
    private ArrayList<Message> messages = new ArrayList<>();

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
                if(message.getDate().before(end)) {
                    startStopMessages.add(message);
                }
            }

            return startStopMessages;
        }

        if(end == null) {
            for(Message message : messages) {
                if(message.getDate().after(start)) {
                    startStopMessages.add(message);
                }
            }

            return startStopMessages;
        }

        for(Message message : messages) {
            if(message.getDate().after(start) && message.getDate().before(end)) {
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

        boolean started = false;

        for(Message message : messages) {
            if(message.getArgs().contains(startKeyword) && !started) {
                started = true;
            }

            if(started) {
                keywordMessages.add(message);
            }
        }

        return keywordMessages;
    }

    /**
     * This method will filter out an array
     * of messages based on the specified keyword.
     * @param keyword       used to filter out messages
     * @return              ArrayList of filtered messages
     */
    public ArrayList<Message> getMessagesByKeyword(String keyword) {
        ArrayList<Message> keywordMessages = new ArrayList<>();

        for(Message message : messages) {
            if(message.getArgs().contains(keyword)) {
                keywordMessages.add(message);
            }
        }

        return keywordMessages;
    }

    public ArrayList<Message> getMessages() {
        return this.messages;
    }

    public void updateMessages(ArrayList<Message> messages) {
        this.messages.clear();
        this.messages.addAll(messages);
    }
}