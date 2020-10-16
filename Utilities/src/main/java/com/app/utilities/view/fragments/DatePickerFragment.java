package com.app.utilities.view.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import com.app.utilities.callbacks.OnDateSetListener;
import com.app.utilities.utils.Constant;
import com.app.utilities.utils.DateHelper;
import com.app.utilities.utils.StringHelper;

import java.util.Calendar;
import java.util.Locale;

/**
 * @Auther Dhaval Jivani
 */

/**
 * TODO : must implement OnDateSetListener into your activity
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private OnDateSetListener onDateSetCallback;

    public DatePickerFragment() {
    }

    /**
     * @param minDate
     * @param maxDate
     * @param date(dd-MM-yyyy) = date want to show to picker view (if you pass 20-04-2017 then date picker show 20 April into date picker)
     * @param identifier       = if multiple date picker in same screen
     * @return
     */

    //TODO : If you pass date parameter then must pass date format
    public static DatePickerFragment newInstance(long minDate, long maxDate, String date, String dateFormat, String identifier) {

        Bundle args = new Bundle();
        args.putString(Constant.IntentKey.DATE, date);
        args.putLong(Constant.IntentKey.MIN_DATE, minDate);
        args.putLong(Constant.IntentKey.MAX_DATE, maxDate);
        args.putString(Constant.IntentKey.DATE_FORMAT, dateFormat);
        args.putString(Constant.IntentKey.DATE_PICKER_IDENTIFIER, identifier);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            try {
                onDateSetCallback = (OnDateSetListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " must implement MyInterface ");
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String date = bundle.getString(Constant.IntentKey.DATE, "");
            String dateFormat = bundle.getString(Constant.IntentKey.DATE_FORMAT, "");
            long minDate = bundle.getLong(Constant.IntentKey.MIN_DATE, 0);
            long maxDate = bundle.getLong(Constant.IntentKey.MAX_DATE, 0);
            String identifier = bundle.getString(Constant.IntentKey.DATE_PICKER_IDENTIFIER, "");

            Calendar c = Calendar.getInstance(Locale.US);
            if (!StringHelper.isEmpty(date) && !StringHelper.isEmpty(dateFormat)) {
                try {
                    c.setTime(DateHelper.stringToDate(date, dateFormat));
                } catch (Exception e) {
                    Log.e(DatePickerFragment.this.getClass().getSimpleName(), "error in onCreateDialog(): " + e.getMessage());
                }
            }
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setTag(identifier);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
            }

            // if (StringHelper.isEmpty(date)){
            if (minDate != 0) {
                dialog.getDatePicker().setMinDate(minDate);
            }

            if (maxDate != 0) {
                dialog.getDatePicker().setMaxDate(maxDate);
            }
            //}
            return dialog;

        }

        return super.onCreateDialog(savedInstanceState);
    }

    /**
     * TODO DHAVAL : If multiple date picker in screen then manage using identifier parameter
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        onDateSetCallback.onDateSet(datePicker, year, month, day);
    }
}
