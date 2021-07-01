package com.app.erldriver.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.app.erldriver.R;
import com.app.erldriver.callback.SubmitOrderListener;
import com.app.erldriver.databinding.DialogSubmitOrderBinding;
import com.app.erldriver.util.AppConstant;
import com.app.utilities.utils.ToastHelper;


public class SubmitOrderDialog extends DialogFragment {
    private DialogSubmitOrderBinding binding;
    private static Context mContext;
    private static AlertDialog dialog;
    private static int orderType;
    private static boolean is_payment_received;
    private static SubmitOrderListener listener;

    public SubmitOrderDialog() {
    }

    public static SubmitOrderDialog newInstance(Context context, int type, SubmitOrderListener l, boolean payment_received) {
        SubmitOrderDialog frag = new SubmitOrderDialog();
        mContext = context;
        orderType = type;
        listener = l;
        is_payment_received = payment_received;
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
                binding.txtPaymentCollectedTitle.setVisibility(View.GONE);
                binding.rgPC.setVisibility(View.GONE);
                break;
            case AppConstant.Type.ORDER_DROPS:
                binding.edtNote.setHint(getString(R.string.hint_enter_drop_note));
                if (!is_payment_received) {
                    binding.txtTitle.setText(getString(R.string.lbl_drop_note));
                    binding.txtPaymentCollectedTitle.setVisibility(View.VISIBLE);
                    binding.rgPC.setVisibility(View.VISIBLE);
                } else {
                    binding.txtTitle.setText(getString(R.string.lbl_pick_up_note));
                    binding.txtPaymentCollectedTitle.setVisibility(View.GONE);
                    binding.rgPC.setVisibility(View.GONE);
                }
                break;
        }
        ad.setView(view);
        dialog = ad.create();

        binding.txtSubmit.setOnClickListener(v -> {
            if (listener != null) {
                switch (orderType) {
                    case AppConstant.Type.ORDER_PICKUPS:
                        listener.onSubmitOrder(binding.edtNote.getText().toString().trim(), orderType, 0);
                        dismiss();
                        break;
                    case AppConstant.Type.ORDER_DROPS:
                        if (!is_payment_received) {
                            int paymentCollected = 0;
                            if (binding.rgPC.getCheckedRadioButtonId() == R.id.rbYes)
                                paymentCollected = 1;
                            else if (binding.rgPC.getCheckedRadioButtonId() == R.id.rbNo)
                                paymentCollected = 2;

                            if (paymentCollected != 0) {
                                listener.onSubmitOrder(binding.edtNote.getText().toString().trim(), orderType, paymentCollected);
                                dismiss();
                            } else {
                                ToastHelper.normal(mContext, getString(R.string.error_empty_payment_collection), Toast.LENGTH_SHORT, false);
                            }
                        } else {
                            listener.onSubmitOrder(binding.edtNote.getText().toString().trim(), orderType, 0);
                            dismiss();
                            break;
                        }
                }
            }

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
