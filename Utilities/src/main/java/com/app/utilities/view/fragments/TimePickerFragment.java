package com.app.utilities.view.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.widget.TimePicker;

import com.app.utilities.callbacks.OnTimeSetCallback;
import com.app.utilities.utils.Constant;
import com.app.utilities.utils.DateHelper;
import com.app.utilities.utils.StringHelper;

import java.util.Calendar;

/**
 * Created by pc on 18-01-2017.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    OnTimeSetCallback onTimeSetCallback;
    String identifier;

    /** TODO : If you pass time then Must pass time format HH:mm */
    public static TimePickerFragment newInstance(String identifier ,String time ,String timeFormat) {

        Bundle args = new Bundle();
        args.putString(Constant.IntentKey.TIME_PICKER_IDENTIFIER , identifier);
        args.putString(Constant.IntentKey.TIME ,time);
        args.putString(Constant.IntentKey.TIME_FORMAT ,timeFormat);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof Activity) {
            try {
                onTimeSetCallback = (OnTimeSetCallback) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " must implement MyInterface ");
            }
        }
    }

    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            identifier = bundle.getString(Constant.IntentKey.TIME_PICKER_IDENTIFIER, "");
            String timeStr = bundle.getString(Constant.IntentKey.TIME, "");
            String timeFormat = bundle.getString(Constant.IntentKey.TIME_FORMAT ,"");
            Calendar c = Calendar.getInstance();

            if (!StringHelper.isEmpty(timeStr) && !StringHelper.isEmpty(timeFormat)) {
                try {
                    c.setTime(DateHelper.stringToDate(timeStr ,timeFormat));
                } catch (Exception e) {
                    Log.e(TimePickerFragment.this.getClass().getSimpleName(), "error in onCreateDialog(): " + e.getMessage());
                }
            }
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, false);
        }
        return super.onCreateDialog(savedInstanceState);
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        view.setTag(identifier);
        onTimeSetCallback.onTimeSet(view, hourOfDay, minute);
    }
}
