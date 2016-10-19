package org.thoughtcrime.securesms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ru.innopolis.messagino.DelayedMessageData;

/**
 * @author Igor Bobko on 05.10.16.
 */

public class DelayedMessageDatabase extends Database {
    private static final String TABLE_NAME  = "delayed_message";
    public static final String ID          = "_id";
    public static final String DT  = "dt";
    public static final String MESSAGE  = "message";
    public static final String RECIPIENT = "recipient";
    public static final String DROP_TABLE = "DROP TABLE " + TABLE_NAME + ";";

    public static DateFormat dateFormat = DateFormat.getDateTimeInstance();

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY, " +
            RECIPIENT + " INTEGER," +
            DT + " TEXT," +
            MESSAGE + " TEXT);";

    public DelayedMessageDatabase(final Context context, final SQLiteOpenHelper databaseHelper) {
        super(context, databaseHelper);
    }


    public SQLiteDatabase getDb() {
        return databaseHelper.getWritableDatabase();
    }

    public List<DelayedMessageData> getMessages() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT " + ID + ", " + RECIPIENT + "," + MESSAGE + "," + DT + " FROM " + TABLE_NAME, new String[]{});

        final ArrayList<DelayedMessageData> messageData = new ArrayList<>();

        while(cursor.moveToNext()) {
            final DelayedMessageData delayedMessageData = new DelayedMessageData();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                System.out.println(cursor.getColumnName(i) + " " + i);
            }
            delayedMessageData.setId(cursor.getInt(0));
            delayedMessageData.setPerson(cursor.getString(1));
            delayedMessageData.setText(""+cursor.getString(2));
            Calendar calendar = new GregorianCalendar();
            try {
                final Date dt = DateFormat.getDateTimeInstance().parse(cursor.getString(3));
                calendar.setTime(dt);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            delayedMessageData.setDateForSending(calendar);
            messageData.add(delayedMessageData);

        }
        cursor.close();
        return messageData;
    }

    public void save(final DelayedMessageData delayedMessageData) {
        final ContentValues newValues = new ContentValues();

        newValues.put(MESSAGE,delayedMessageData.getText());
        newValues.put(DT,dateFormat.format(delayedMessageData.getDateForSending().getTime()));

        if (delayedMessageData.getId() != null && delayedMessageData.getId() != 0) {
            getDb().update(TABLE_NAME, newValues, ID + " = ?", new String[]{delayedMessageData.getId().toString()});
        } else {
            getDb().insert(TABLE_NAME, null,  newValues);
        }
    }


    public void delete(final Integer id) {
        if (id == null) return;
        getDb().delete(TABLE_NAME,ID + " = ?", new String[]{id.toString()});
    }

}
