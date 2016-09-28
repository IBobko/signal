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
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        /*Bundle bnd = new Bundle();
        bnd.putInt("Year", year);
        bnd.putInt("Month", monthOfYear);
        bnd.putInt("Day", dayOfMonth);
        this.setArguments(bnd);*/

    }
}

