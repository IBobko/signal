package ru.innopolis.messagino;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.thoughtcrime.securesms.R;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.DelayedMessageDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * The delayed message card.
 *
 * @author Vyacheslav Stepanov
 */
public class DelayedMessageActivity extends Activity implements OnClickListener {
    final DelayedMessageData delayedMessageData = new DelayedMessageData();
    private TextView dateText;
    private TextView timeText;
    private EditText textMessage;

    @Override
    protected void onResume() {
        super.onResume();
        final Intent intent = getIntent();
        if (intent.getExtras() != null) {
            Bundle ex = intent.getExtras();
            try {
                delayedMessageData.setId(Integer.parseInt(ex.getString("MESSAGE_ID")));
            } catch(NumberFormatException ignore) {}


            final String text = intent.getExtras().getString("MESSAGE_TEXT");

            if (text!=null) {
                textMessage.setText(text);
            } else {
                textMessage.setText("");
            }

            final String dateTime = intent.getExtras().getString("MESSAGE_TIME");
            if (dateTime!=null) {
                dateText.setText(DateFormat.getDateInstance().format(dateTime));
                timeText.setText(DateFormat.getTimeInstance().format(dateTime));
            } else {
                final Calendar cal = Calendar.getInstance();
                dateText.setText(DateFormat.getDateInstance().format(cal.getTime()));
                timeText.setText(DateFormat.getTimeInstance().format(cal.getTime()));
            }

        } else {
            final Calendar cal = Calendar.getInstance();
            dateText.setText(DateFormat.getDateInstance().format(cal.getTime()));
            timeText.setText(DateFormat.getTimeInstance().format(cal.getTime()));
            textMessage.setText("");
            delayedMessageData.setDateForSending(cal);
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delayed_message_activity);

        Button btnTime = (Button) findViewById(R.id.btnTime);
        btnTime.setOnClickListener(this);

        Button btnDate = (Button) findViewById(R.id.btnDate);
        btnDate.setOnClickListener(this);

        Button btnSend = (Button) findViewById(R.id.btnSend);

        btnSend.setOnClickListener(this);

        timeText = (TextView) findViewById(R.id.textTime);
        dateText = (TextView) findViewById(R.id.textDate);
        textMessage = (EditText) findViewById(R.id.textMessage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTime:
                showTimePickerDialog(v);
                break;
            case R.id.btnDate:
                showDatePickerDialog(v);
                break;
            case R.id.btnSend:
                save(v);
                break;
            default:
                break;
        }
    }

    private void save(View v) {
        final DelayedMessageDatabase delayedMessage = DatabaseFactory.getDelayedMessageDatabase(DelayedMessageActivity.this);
        final String dt = dateText.getText().toString() + " " + timeText.getText().toString();
        final Calendar g = new GregorianCalendar();
        try {
            final Date dtDate = DateFormat.getDateTimeInstance().parse(dt);
            g.setTime(dtDate);
        } catch (final ParseException ignored) {}
        delayedMessageData.setText(textMessage.getText().toString());
        delayedMessageData.setDateForSending(g);
        delayedMessage.save(delayedMessageData);
        finish();
    }

    public void showTimePickerDialog(View v) {
        int hour = delayedMessageData.getDateForSending().get(Calendar.HOUR);
        int minute = delayedMessageData.getDateForSending().get(Calendar.MINUTE);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(final TimePicker view, final int hourOfDay, final int minute) {
                delayedMessageData.getDateForSending().set(Calendar.HOUR, hourOfDay);
                delayedMessageData.getDateForSending().set(Calendar.MINUTE, minute);
                timeText.setText(DateFormat.getTimeInstance().format(delayedMessageData.getDateForSending().getTime()));
            }
        }, hour, minute, android.text.format.DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    public void showDatePickerDialog(final View v) {
        int year = delayedMessageData.getDateForSending().get(Calendar.YEAR);
        int month = delayedMessageData.getDateForSending().get(Calendar.MONTH);
        int day = delayedMessageData.getDateForSending().get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                delayedMessageData.getDateForSending().set(Calendar.YEAR, year);
                delayedMessageData.getDateForSending().set(Calendar.MONTH, monthOfYear);
                delayedMessageData.getDateForSending().set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateText.setText(DateFormat.getDateInstance().format(delayedMessageData.getDateForSending().getTime()));
            }
        }, year, month, day);
        datePickerDialog.show();
    }
}
