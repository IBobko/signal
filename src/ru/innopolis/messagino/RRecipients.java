package ru.innopolis.messagino;

import org.thoughtcrime.securesms.recipients.Recipient;

import java.io.Serializable;
import java.util.List;

/**
 * Created by i.minnakhmetov on 10/20/2016.
 */

public class RRecipients  implements Serializable {
    private List<Recipient> recipients;

    public List<Recipient> getRecipient() {
        return recipients;
    }

    public void setRecipient(List<Recipient> recipients) {
        this.recipients = recipients;
    }

    public RRecipients(List<Recipient> recipients) {
        this.recipients = recipients;
    }
}