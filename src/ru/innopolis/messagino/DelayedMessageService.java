package ru.innopolis.messagino;

import java.util.List;

/**
 * Created by igor on 24.09.16.
 */

public interface DelayedMessageService {
    DelayedMessageData getMessageById(Integer id);
    DelayedMessageData removeMessageById(Integer id);
    void addMessage(DelayedMessageData message);
    List<DelayedMessageData> getMessagesByPerson(String name);
}
