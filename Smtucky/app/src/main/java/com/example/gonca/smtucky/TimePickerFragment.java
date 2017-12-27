package com.example.gonca.smtucky;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextClock;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by filipemendes on 26/12/17.
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private ISelectedData mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();

        String time = args.getString("time");
        Calendar c = Calendar.getInstance();
        if(time != "") {
            Log.d("dialog", time);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a", Locale.ENGLISH);
            try {
                c.setTime(sdf.parse(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // Use the current time as the default values for the picker
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa");

        try {
            Date date = dateFormat.parse(Integer.toString(hourOfDay) + ':' + Integer.toString(minute));
            String out = dateFormat2.format(date);
            mCallback.onSelectedData(out);
        } catch (ParseException e) {
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (ISelectedData) activity;
        }
        catch (ClassCastException e) {
            Log.d("MyDialog", "Activity doesn't implement the ISelectedData interface");
        }
    }

}
