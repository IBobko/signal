package ru.innopolis.messagino;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by igor on 24.09.16.
 */

public class DelayedMessageData implements Serializable {
    private Integer id;
    private String text;
    private Calendar dateForSending;
    private String person;

    public DelayedMessageData() {
        dateForSending = new GregorianCalendar();
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

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }
}
