package ru.innopolis.messagino;


import android.app.DialogFragment;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;


/**
 * The time picker dialog.
 *
 * @author Vyacheslav Stepanov
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private int minute = 0;
    private int hour = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the current time as the default values for the picker

        //hour = savedInstanceState.getInt("Hour");
        //minute = savedInstanceState.getInt("Minute");


        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public int getMinute() {
        return minute;
    }

    public int getHour() {
        return hour;
    }
    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.minute = minute;
        this.hour = hour;
//        Bundle bnd = new Bundle();
//        bnd.putInt("Hour", hourOfDay);
//        bnd.putInt("Minute", minute);
//        this.setArguments(bnd);
    }
}


