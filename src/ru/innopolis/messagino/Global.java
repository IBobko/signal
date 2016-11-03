package ru.innopolis.messagino;

import android.app.Activity;

import org.thoughtcrime.securesms.crypto.MasterSecret;
import org.thoughtcrime.securesms.recipients.Recipients;

/**
 * @Author Igor Bobko
 */

public class Global {
    public static int type = 0;
    public static Recipients recipients;
    public static MasterSecret masterSecret;
    public static Activity activity;
}
