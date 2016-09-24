package ru.innopolis.messagino;

import java.util.Calendar;

/**
 * Created by igor on 24.09.16.
 */

public class DelayedMessageData {
    private String text;
    private Calendar dateForSending;
    private String person;

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
