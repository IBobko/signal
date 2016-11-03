package org.thoughtcrime.securesms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import ru.innopolis.messagino.DelayedMessageData;
import ru.innopolis.messagino.SendMessage;

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
    static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    private Timer timer;
    private SendDelayedMessages sdm;

    private static DateFormat dateFormat = DateFormat.getDateTimeInstance();

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY, " +
            TREAD_ID + " INTEGER," +
            DT + " TEXT," +
            STATUS + " INTEGER," +
            MESSAGE + " TEXT);";

    DelayedMessageDatabase(final Context context, final SQLiteOpenHelper databaseHelper) {
        super(context, databaseHelper);

        sdm = new SendDelayedMessages(this);
        timer = new Timer();
        timer.schedule(sdm, 1000, 1000);
    }


    public SQLiteDatabase getDb() {
        return databaseHelper.getWritableDatabase();
    }

    public List<DelayedMessageData> getMessages() {
        final SQLiteDatabase database = databaseHelper.getReadableDatabase();
        final Cursor cursor = database.rawQuery("SELECT " + ID + ", " + TREAD_ID + "," + MESSAGE + "," + DT + "," + STATUS + " FROM " + TABLE_NAME, new String[]{});
        return getByCursor(cursor);
    }

    public List<DelayedMessageData> getCurrentDelayedMessages() {
        final SQLiteDatabase database = databaseHelper.getReadableDatabase();
        final String status = "0";
        final Cursor cursor = database.rawQuery("SELECT "   + ID + ", "
                                                            + TREAD_ID + ","
                                                            + MESSAGE + ","
                                                            + DT + ","
                                                            + STATUS
                                                + " FROM "  + TABLE_NAME
                                                + " WHERE " + STATUS + "= ?"
                , new String[]{""+status});



        List<DelayedMessageData> allData = getByCursor(cursor);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        List<DelayedMessageData> result = new ArrayList<>();

        for(DelayedMessageData d : allData){

            d.getDateForSending().set(Calendar.SECOND,0);
            d.getDateForSending().set(Calendar.MILLISECOND,0);

            System.out.println(DateFormat.getDateTimeInstance().format(cal.getTime()));
            System.out.println("Status" + d.getStatus());
            System.out.println(DateFormat.getDateTimeInstance().format(d.getDateForSending().getTime()));

            if (0 == d.getDateForSending().compareTo(cal)){
                result.add(d);
            }
        }
        return result;
    }

    public void updateDelayedMessagesAsPerformed(int messageId, int status) {
        final ContentValues newValues = new ContentValues();
        newValues.put(STATUS, status);
        getDb().update(TABLE_NAME, newValues, ID + " = ?", new String[]{"" + messageId });
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
        delayedMessageData.setStatus(cursor.getInt(4));
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

    public  List<DelayedMessageData> getByRecipientAndStatus(final long recipient,final int status) {
        final SQLiteDatabase database = databaseHelper.getReadableDatabase();
        final Cursor cursor = database.rawQuery("SELECT " + ID + ", " + TREAD_ID + "," + MESSAGE + "," + DT + "," + STATUS + " FROM " + TABLE_NAME + " WHERE " + TREAD_ID + " = ? AND " + STATUS + "= ?", new String[]{""+recipient,""+status});
        return getByCursor(cursor);
    }


    @SuppressWarnings("unused")
    public  List<DelayedMessageData> getByRecipient(final long recipient) {
        final SQLiteDatabase database = databaseHelper.getReadableDatabase();
        final Cursor cursor = database.rawQuery("SELECT " + ID + ", " + TREAD_ID + "," + MESSAGE + "," + DT + "," + STATUS + " FROM " + TABLE_NAME + " WHERE " + TREAD_ID + " = ?", new String[]{""+recipient});
        return getByCursor(cursor);
    }

    public void save(final DelayedMessageData delayedMessageData) {
        final ContentValues newValues = new ContentValues();

        newValues.put(MESSAGE,delayedMessageData.getText());
        newValues.put(DT,dateFormat.format(delayedMessageData.getDateForSending().getTime()));
        newValues.put(TREAD_ID,delayedMessageData.getThreadId());
        newValues.put(STATUS,delayedMessageData.getStatus());

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

    public void updateStatus(int messageId, int status) {
        final ContentValues newValues = new ContentValues();
        newValues.put(STATUS,status);
        getDb().update(TABLE_NAME, newValues, ID + " = ?", new String[]{"" + messageId });
    }

}
class SendDelayedMessages extends TimerTask {
    DelayedMessageDatabase dmdb;

    SendDelayedMessages(DelayedMessageDatabase p_dmdb){
        dmdb = p_dmdb;
    }

    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        System.out.println("First step");
        List<DelayedMessageData> dm = dmdb.getCurrentDelayedMessages();

        if (dm.isEmpty()){
            System.out.println("Empty");
            return;
        }
        System.out.println("Next step");
        for(DelayedMessageData d:dm){
            System.out.println(DateFormat.getDateTimeInstance().format(d.getDateForSending().getTime()) + d.getText());
            dmdb.updateDelayedMessagesAsPerformed(d.getId(), 1);

            SendMessage.sendMessage(d.getText());

            System.out.println("УРРРРРРРААААА! ОТПРАВИЛИ!!!");
        }

    }
}