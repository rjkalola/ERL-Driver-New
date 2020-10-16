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
import androidx.fragment.app.FragmentManager;

import com.app.erldriver.R;
import com.app.erldriver.callback.SelectTimeListener;
import com.app.erldriver.databinding.DialogSelectTimeBinding;
import com.app.erldriver.util.AppConstant;
import com.app.erldriver.view.activity.BaseActivity;
import com.app.utilities.utils.StringHelper;


public class SelectTimeDialog extends DialogFragment implements SelectTimeListener {
    private DialogSelectTimeBinding binding;
    private static Context mContext;
    private static AlertDialog dialog;
    private static String fromTime, toTime;
    private static SelectTimeListener listener;

    public SelectTimeDialog() {
    }

    public static SelectTimeDialog newInstance(Context context, String from, String to, SelectTimeListener l) {
        SelectTimeDialog frag = new SelectTimeDialog();
        mContext = context;
        fromTime = from;
        toTime = to;
        listener = l;
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
        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_select_time, null);
        binding = DataBindingUtil.bind(view);

        if (!StringHelper.isEmpty(fromTime))
            binding.edtFromTime.setText(fromTime);

        if (!StringHelper.isEmpty(toTime))
            binding.edtToTime.setText(toTime);

        ad.setPositiveButton(mContext.getString(R.string.lbl_select),
                (dialog, whichButton) -> {
                    if (listener != null)
                        listener.onSelectTime(binding.edtFromTime.getText().toString(), binding.edtToTime.getText().toString(), 0);
                });
        ad.setNegativeButton(mContext.getString(R.string.lbl_cancel),
                (dialog, whichButton) -> {
                });
        ad.setView(view);
        dialog = ad.create();

        dialog.setOnShowListener(d -> {
            if (!StringHelper.isEmpty(fromTime) && !StringHelper.isEmpty(toTime)) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            } else {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        });

        binding.edtFromTime.setOnClickListener(v -> {
            selectTimeDialog(binding.edtFromTime.getText().toString().trim(), AppConstant.DialogIdentifier.SELECT_FROM_TIME);
        });

        binding.edtToTime.setOnClickListener(v -> {
            selectTimeDialog(binding.edtToTime.getText().toString().trim(), AppConstant.DialogIdentifier.SELECT_TO_TIME);
        });

        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void checkButtonVisibility() {
        if (!StringHelper.isEmpty(binding.edtFromTime.getText().toString())
                && !StringHelper.isEmpty(binding.edtToTime.getText().toString())) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        } else {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        }
    }

    public void selectTimeDialog(String time, int identifier) {
        FragmentManager fm = ((BaseActivity) mContext).getSupportFragmentManager();
        SelectHourMinuteDialog selectHourMinuteDialog = SelectHourMinuteDialog.newInstance(mContext, time, identifier, this);
        selectHourMinuteDialog.show(fm, "selectTimeDialog");
    }

    @Override
    public void onSelectTime(String fromTime, String toTime, int identifier) {
        if (identifier == AppConstant.DialogIdentifier.SELECT_FROM_TIME) {
            binding.edtFromTime.setText(fromTime);
        } else if (identifier == AppConstant.DialogIdentifier.SELECT_TO_TIME) {
            binding.edtToTime.setText(fromTime);
        }
        checkButtonVisibility();
    }
}
