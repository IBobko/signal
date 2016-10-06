package org.thoughtcrime.securesms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ru.innopolis.messagino.DelayedMessageData;

/**
 * Created by Igor Bobko on 05.10.16.
 */

public class DelayedMessageDatabase extends Database {
    private static final String TABLE_NAME  = "delayed_message";
    public  static final String ID          = "_id";
    public  static final String DT  = "dt";
    public  static final String MESSAGE  = "message";
    public  static final String RECIPIENT = "recipient";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY, " +
            RECIPIENT + " INTEGER," +
            MESSAGE + " TEXT);";

    public DelayedMessageDatabase(final Context context, final SQLiteOpenHelper databaseHelper) {
        super(context, databaseHelper);
    }


    public SQLiteDatabase getDb() {
        return databaseHelper.getWritableDatabase();
    }

    public List<DelayedMessageData> getMessages() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("message","text");
//        database.insert(TABLE_NAME,null,contentValues);

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, new String[]{});

        final ArrayList<DelayedMessageData> messageData = new ArrayList<>();

        while(cursor.moveToNext()) {
            final DelayedMessageData delayedMessageData = new DelayedMessageData();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                System.out.println(cursor.getColumnName(i) + " " + i);
            }
            delayedMessageData.setId(cursor.getInt(0));
            //delayedMessageData.setDateForSending(cursor.getLong());
            delayedMessageData.setPerson(cursor.getString(1));
            delayedMessageData.setText(""+cursor.getString(2));
            messageData.add(delayedMessageData);
            ;
        }
        cursor.close();
        return messageData;
    }

}
