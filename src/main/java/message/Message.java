package message;

import java.util.Date;
import java.util.List;

/**
 * POJO used to store JSON content
 *
 * @author Luciano Kholos
 */
public class Message {
    private String content;
    private List<String> args;
    private Date date;

    public Message(String content, List<String> args, Date date) {
        this.content = content;
        this.args = args;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public List<String> getArgs() {
        return args;
    }

    public Date getDate() {
        return date;
    }
}