package ru.innopolis.messagino;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Igor Bobko <limit-speed@yandex.ru>
 */

public class DelayedMessageData implements Serializable {
    private Integer id = 0;
    private String text = "";
    private Calendar dateForSending = new GregorianCalendar();
    private long threadId = 0;

    public DelayedMessageData() {
        dateForSending = new GregorianCalendar();
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Calendar getDateForSending() {
        return dateForSending;
    }

    public void setDateForSending(Calendar dateForSending) {
        this.dateForSending = dateForSending;
    }
}
