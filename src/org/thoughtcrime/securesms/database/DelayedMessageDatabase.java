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
    private static final String DT  = "dt";
    private static final String MESSAGE  = "message";
    private static final String TREAD_ID = "tread_id";
    public static final String STATUS = "status";
    static final String DROP_TABLE = "DROP TABLE " + TABLE_NAME + ";";

    private static DateFormat dateFormat = DateFormat.getDateTimeInstance();

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY, " +
            TREAD_ID + " INTEGER," +
            DT + " TEXT," +
            STATUS + " INTEGER," +
            MESSAGE + " TEXT);";

    DelayedMessageDatabase(final Context context, final SQLiteOpenHelper databaseHelper) {
        super(context, databaseHelper);
    }


    public SQLiteDatabase getDb() {
        return databaseHelper.getWritableDatabase();
    }

    public List<DelayedMessageData> getMessages() {
        final SQLiteDatabase database = databaseHelper.getReadableDatabase();
        final Cursor cursor = database.rawQuery("SELECT " + ID + ", " + TREAD_ID + "," + MESSAGE + "," + DT + " FROM " + TABLE_NAME, new String[]{});
        return getByCursor(cursor);
    }

    private DelayedMessageData delayedMessageDataFromCursor(final Cursor cursor) {
        final DelayedMessageData delayedMessageData = new DelayedMessageData();
        delayedMessageData.setId(cursor.getInt(0));
        delayedMessageData.setThreadId(cursor.getLong(1));
        delayedMessageData.setText(""+cursor.getString(2));
        Calendar calendar = new GregorianCalendar();
        try {
            final Date dt = DateFormat.getDateTimeInstance().parse(cursor.getString(3));
            calendar.setTime(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        delayedMessageData.setDateForSending(calendar);
        return delayedMessageData;
    }


    private List<DelayedMessageData> getByCursor(final Cursor cursor) {
        final ArrayList<DelayedMessageData> messageData = new ArrayList<>();
        while(cursor.moveToNext()) {
            final DelayedMessageData delayedMessageData = delayedMessageDataFromCursor(cursor);
            messageData.add(delayedMessageData);
        }
        cursor.close();
        return messageData;
    }

    public  List<DelayedMessageData> getByRecipientAndStatus(final String recipient,final Integer status) {
        final SQLiteDatabase database = databaseHelper.getReadableDatabase();
        final Cursor cursor = database.rawQuery("SELECT " + ID + ", " + TREAD_ID + "," + MESSAGE + "," + DT + "," + STATUS + " FROM " + TABLE_NAME + " WHERE " + TREAD_ID + " = ?, " + STATUS + "= ?", new String[]{recipient,status.toString()});
        return getByCursor(cursor);
    }


    public  List<DelayedMessageData> getByRecipient(final String recipient) {
        final SQLiteDatabase database = databaseHelper.getReadableDatabase();
        final Cursor cursor = database.rawQuery("SELECT " + ID + ", " + TREAD_ID + "," + MESSAGE + "," + DT + "," + STATUS + " FROM " + TABLE_NAME + " WHERE " + TREAD_ID + " = ?", new String[]{recipient});
        return getByCursor(cursor);
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
