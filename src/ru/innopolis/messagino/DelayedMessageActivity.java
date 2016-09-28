package ru.innopolis.messagino;


import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.widget.TextView;

import org.thoughtcrime.securesms.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * The delayed message card.
 *
 * @author Vyacheslav Stepanov
 */
public class DelayedMessageActivity extends Activity implements OnClickListener {
    int myYear = 2011;
    int myMonth = 02;
    int myDay = 03;

    int myHour = 4;
    int myMinute = 30;

    TextView dateText;
    TextView timeText;
    Button btnTime;
    Button btnDate;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delayed_message_activity);

        btnTime = (Button) findViewById(R.id.btnTime);
        btnTime.setOnClickListener(this);

        btnDate = (Button) findViewById(R.id.btnDate);
        btnDate.setOnClickListener(this);

        timeText = (TextView) findViewById(R.id.textTime);
        dateText = (TextView) findViewById(R.id.textDate);
        updateTextView();


    }
    public void updateTextView(){
        Calendar cal = new GregorianCalendar(myYear, myMonth, myDay);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        dateText.setText(df.format(cal.getTime()));
        timeText.setText("Time "+Integer.toString(myHour)+":"+Integer.toString(myMinute));
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

    public void showTimePickerDialog(View v) {

        TimePickerFragment newFragment = new TimePickerFragment();
//        Bundle bnd = new Bundle();
//        bnd.putInt("Hour", myHour);
//        bnd.putInt("Minute", myMinute);
//        newFragment.setArguments(bnd);
        newFragment.setHour(myHour);
        newFragment.setMinute(myMinute);
        //newFragment.setTargetFragment(this.getContext(), 1);
        newFragment.show(getFragmentManager(), "timePicker");

        myMinute = newFragment.getMinute();
        myHour = newFragment. getHour();
        //Bundle bundle= newFragment.getArguments();
       // myHour=bundle.getInt("Hour");
       // myMinute=bundle.getInt("Minute");
        updateTextView();
    }
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
        //Bundle bundle= newFragment.getArguments();
        //myYear=bundle.getInt("Year");
        //myMonth=bundle.getInt("Month");
       // myDay=bundle.getInt("Day");
        //updateTextView();
    }
}

