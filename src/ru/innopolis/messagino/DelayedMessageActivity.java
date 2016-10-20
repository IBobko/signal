package ru.innopolis.messagino;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.thoughtcrime.securesms.BaseActionBarActivity;
import org.thoughtcrime.securesms.R;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.DelayedMessageDatabase;
import org.thoughtcrime.securesms.database.ThreadDatabase;
import org.thoughtcrime.securesms.recipients.Recipients;

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
public class DelayedMessageActivity extends BaseActionBarActivity implements OnClickListener {
    private DelayedMessageData delayedMessageData;
    private TextView dateText;
    private TextView timeText;
    private EditText textMessage;
    private TextView textRecipient;

    @Override
    protected void onResume() {
        super.onResume();
        final Intent intent = getIntent();
        if (intent.getExtras() != null) {
            final Bundle ex = intent.getExtras();
            delayedMessageData = (DelayedMessageData) ex.getSerializable("DelayedMessage");
            if (delayedMessageData != null) {
                textMessage.setText(delayedMessageData.getText());
                final Calendar calendar = delayedMessageData.getDateForSending();
                if (calendar != null) {
                    dateText.setText(DateFormat.getDateInstance().format(calendar.getTime()));
                    timeText.setText(DateFormat.getTimeInstance().format(calendar.getTime()));
                }

                final ThreadDatabase threadDatabase = DatabaseFactory.getThreadDatabase(this);
                final Recipients res = threadDatabase.getRecipientsForThreadId(delayedMessageData.getThreadId());
                if (res != null && res.getPrimaryRecipient() != null) {
                    textRecipient.setText(res.getPrimaryRecipient().getName());
                }
            }
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
        textRecipient = (TextView) findViewById(R.id.textRecipient);
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

    @SuppressWarnings("UnusedParameters")
    private void save(final View v) {
        final DelayedMessageDatabase delayedMessage = DatabaseFactory.getDelayedMessageDatabase(DelayedMessageActivity.this);
        final String dt = dateText.getText().toString() + " " + timeText.getText().toString();
        final Calendar g = new GregorianCalendar();
        try {
            final Date dtDate = DateFormat.getDateTimeInstance().parse(dt);
            g.setTime(dtDate);
        } catch (final ParseException ignored) {
        }
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
