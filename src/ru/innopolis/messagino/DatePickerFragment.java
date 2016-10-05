package ru.innopolis.messagino;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;


import java.util.Calendar;


/**
 * The date picker dialog.
 *
 * @author Vyacheslav Stepanov
 */
public class DatePickerFragment extends DialogFragment  {
    int i;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker

        int year=0;
        int month=0;
        int day=0;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            year = bundle.getInt("YEAR",0);
            month = bundle.getInt("MONTH",0);
            day = bundle.getInt("DAY",0);
        }
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), (DelayedMessageActivity)getActivity(), year, month, day);
    }
}

