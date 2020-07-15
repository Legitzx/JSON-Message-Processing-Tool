package message;

import java.util.Date;
import java.util.List;

/**
 * POJO used to store JSON content
 *
 * @author Luciano Kholos
 */
public class Message {
    private String content; // Contains the raw message
    private String regexContent; // Content used when dealing with regex (basically the same as args but in the form of a single string)
    private List<String> args; // List containing a splitted version of the raw message
    private Date date;  // Contains the timestamp

    public Message(String content, String regexContent, List<String> args, Date date) {
        this.content = content;
        this.regexContent = regexContent;
        this.args = args;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public String getRegexContent() {
        return regexContent;
    }

    public List<String> getArgs() {
        return args;
    }

    public Date getDate() {
        return date;
    }
}