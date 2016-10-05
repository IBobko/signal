package ru.innopolis.messagino;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.spongycastle.asn1.cms.Time;
import org.thoughtcrime.securesms.R;


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * The delayed message card.
 *
 * @author Vyacheslav Stepanov
 */
public class DelayedMessageActivity extends Activity implements OnClickListener,DatePickerDialog.OnDateSetListener,
TimePickerDialog.OnTimeSetListener{
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day =  c.get(Calendar.DAY_OF_MONTH);

    int hour = 0;
    int minute = 0;

    TextView dateText;
    TextView timeText;
    EditText messageText;
    Button btnTime;
    Button btnDate;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final Calendar c = Calendar.getInstance();
        year = intent.getIntExtra("YEAR",c.get(Calendar.YEAR));
        month = intent.getIntExtra("MONTH",c.get(Calendar.MONTH));
        day = intent.getIntExtra("DAY",c.get(Calendar.DAY_OF_MONTH));
        hour = intent.getIntExtra("HOUR",c.get(Calendar.HOUR_OF_DAY));
        minute = intent.getIntExtra("MINUTE",c.get(Calendar.MINUTE));
        String text = intent.getStringExtra("TEXT");


        setContentView(R.layout.delayed_message_activity);

        btnTime = (Button) findViewById(R.id.btnTime);
        btnTime.setOnClickListener(this);

        btnDate = (Button) findViewById(R.id.btnDate);
        btnDate.setOnClickListener(this);

        timeText = (TextView) findViewById(R.id.textTime);
        dateText = (TextView) findViewById(R.id.textDate);
        messageText = (EditText) findViewById(R.id.textMessage);

        messageText.setText(text);
        updateDateFieldText();
        updateTimeFieldText();
    }
    private void updateDateFieldText(){
        Calendar c = new GregorianCalendar(year,month,day,hour,minute,0);

        String date1 = String.format("%1$te %1$tb ,%1$tY", c);
        //String date1 = year+"/"+(month+1)+"/"+day;
        dateText.setText(date1);
    }
    private void updateTimeFieldText() {
        Calendar c = new GregorianCalendar(year,month,day,hour,minute,0);
        String time1= String.format("%tT", c);
        timeText.setText(time1);
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
            default:
                break;
        }
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //do some stuff for example write on log and update TextField on activity
        //Log.w("DatePicker","Date = " + year);
        this.year = year;
        this.month = month;
        this.day = day;
        updateDateFieldText();

    }
    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        updateTimeFieldText();

    }

    public void showTimePickerDialog(View v) {

        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("YEAR", year);
        bundle.putInt("MONTH", month);
        bundle.putInt("DAY", day);
        newFragment.setArguments(bundle);
        newFragment.show(getFragmentManager(), "datePicker");
    }
}

