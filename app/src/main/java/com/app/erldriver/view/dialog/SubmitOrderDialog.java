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
import com.app.erldriver.callback.SubmitOrderListener;
import com.app.erldriver.databinding.DialogSubmitOrderBinding;
import com.app.erldriver.util.AppConstant;


public class SubmitOrderDialog extends DialogFragment {
    private DialogSubmitOrderBinding binding;
    private static Context mContext;
    private static AlertDialog dialog;
    private static int orderType;
    private static SubmitOrderListener listener;

    public SubmitOrderDialog() {
    }

    public static SubmitOrderDialog newInstance(Context context, int type, SubmitOrderListener l) {
        SubmitOrderDialog frag = new SubmitOrderDialog();
        mContext = context;
        orderType = type;
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
        View view = layoutInflater.inflate(R.layout.dialog_submit_order, null);
        binding = DataBindingUtil.bind(view);

        switch (orderType) {
            case AppConstant.Type.ORDER_PICKUPS:
                binding.txtTitle.setText(getString(R.string.lbl_pick_up_note));
                binding.edtNote.setHint(getString(R.string.hint_enter_pick_up_note));
                break;
            case AppConstant.Type.ORDER_DROPS:
                binding.txtTitle.setText(getString(R.string.lbl_drop_note));
                binding.edtNote.setHint(getString(R.string.hint_enter_drop_note));
                break;
        }
        ad.setView(view);
        dialog = ad.create();


        binding.txtSubmit.setOnClickListener(v -> {
            if (listener != null)
                listener.onSubmitOrder(binding.edtNote.getText().toString().trim(), orderType);
            dismiss();
        });

        binding.txtCancel.setOnClickListener(v -> {
            dismiss();
        });

        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
