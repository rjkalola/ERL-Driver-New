package com.app.erldriver.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.app.erldriver.R;
import com.app.erldriver.callback.SelectTimeListener;
import com.app.erldriver.databinding.DialogSelectHourMinuteBinding;
import com.app.utilities.utils.StringHelper;

import java.util.Calendar;
import java.util.Locale;

public class SelectHourMinuteDialog extends DialogFragment {
    private DialogSelectHourMinuteBinding binding;
    private static Context context;
    private AlertDialog dialog;
    private static int hour = 0, minute = 0, identifier;
    private static String time;
    private static SelectTimeListener listener;

    public SelectHourMinuteDialog() {
    }

    public static SelectHourMinuteDialog newInstance(Context mContext, String t, int type, SelectTimeListener l) {
        SelectHourMinuteDialog frag = new SelectHourMinuteDialog();
        context = mContext;
        time = t;
        identifier = type;
        listener = l;

        if (!StringHelper.isEmpty(time)) {
            String[] times = time.split(":");
            if (times.length == 2) {
                hour = Integer.parseInt(times[0]);
                minute = Integer.parseInt(times[1]);
            }
        } else {
            hour = Calendar.getInstance().get(Calendar.HOUR);
            minute = Calendar.getInstance().get(Calendar.MINUTE);
        }

        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogFragmentStyle);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder ad = new AlertDialog.Builder(context, R.style.MyDialogFragmentStyle);
        ad.setPositiveButton(context.getString(R.string.lbl_select), (dialog, id1) -> {
            if (listener != null) {
                String time = String.format(Locale.ENGLISH, "%02d", binding.npHour.getValue())+":" +String.format(Locale.ENGLISH, "%02d", binding.npMinutes.getValue());
                listener.onSelectTime(time, "", identifier);
            }
        });

        ad.setNegativeButton(context.getString(R.string.lbl_cancel), (dialog, id1) -> {

        });

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_select_hour_minute, null);
        binding = DataBindingUtil.bind(view);

//        long timeInMilliseconds = totalMinutes * 60 * 1000;
//        int hours = (int) TimeUnit.MILLISECONDS.toHours(timeInMilliseconds);
//        int minute = (int) (TimeUnit.MILLISECONDS.toMinutes(timeInMilliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInMilliseconds)));

        binding.npHour.setMinValue(1);
        binding.npHour.setMaxValue(12);
        binding.npHour.setFormatter(i -> String.format(Locale.ENGLISH, "%02d", i));
        binding.npHour.setValue(hour);

        binding.npMinutes.setMinValue(0);
        binding.npMinutes.setMaxValue(59);
        binding.npMinutes.setFormatter(i -> String.format(Locale.ENGLISH, "%02d", i));
        binding.npMinutes.setValue(minute);

        ad.setView(view);
        dialog = ad.create();
        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
