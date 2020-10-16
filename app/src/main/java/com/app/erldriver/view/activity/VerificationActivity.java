package com.app.erldriver.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.erldriver.R;
import com.app.erldriver.databinding.ActivityVerificationBinding;
import com.app.erldriver.model.entity.response.BaseResponse;
import com.app.erldriver.util.AppConstant;
import com.app.erldriver.util.AppUtils;
import com.app.erldriver.util.LoginViewModelFactory;
import com.app.erldriver.util.ResourceProvider;
import com.app.erldriver.viewModel.UserAuthenticationViewModel;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.StringHelper;
import com.app.utilities.utils.ToastHelper;

public class VerificationActivity extends BaseActivity implements View.OnClickListener, View.OnKeyListener {
    private ActivityVerificationBinding binding;
    private Context mContext;
    private String email, code;
    private int userId;
    private UserAuthenticationViewModel userAuthenticationViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verification);
        mContext = this;
        userAuthenticationViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(UserAuthenticationViewModel.class);
        userAuthenticationViewModel.createView(this);
        userAuthenticationViewModel.mBaseResponse()
                .observe(this, mBaseResponse());
        userAuthenticationViewModel.mForgotPasswordResponse()
                .observe(this, mForgotPasswordResponse());

        binding.txtVerify.setOnClickListener(this);
        binding.txtResend.setOnClickListener(this);

        binding.edtVerifyCode1.addTextChangedListener(new GenericTextWatcher(binding.edtVerifyCode1));
        binding.edtVerifyCode2.addTextChangedListener(new GenericTextWatcher(binding.edtVerifyCode2));
        binding.edtVerifyCode3.addTextChangedListener(new GenericTextWatcher(binding.edtVerifyCode3));
        binding.edtVerifyCode4.addTextChangedListener(new GenericTextWatcher(binding.edtVerifyCode4));

        binding.edtVerifyCode1.setOnKeyListener(this);
        binding.edtVerifyCode2.setOnKeyListener(this);
        binding.edtVerifyCode3.setOnKeyListener(this);
        binding.edtVerifyCode4.setOnKeyListener(this);

        getIntentData();
    }

    public void getIntentData() {
        if (getIntent().getExtras() != null && getIntent().hasExtra(AppConstant.IntentKey.VERIFICATION_CODE)) {
            email = getIntent().getStringExtra(AppConstant.IntentKey.EMAIL);
            userId = getIntent().getIntExtra(AppConstant.IntentKey.USER_ID, 0);
            binding.txtVerificationCodeHint.setText(String.format(getString(R.string.lbl_display_verification_code_hint), email));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtVerify:
                if (validateCode())
                    userAuthenticationViewModel.verifyCode(userId, code);
                break;
            case R.id.txtResend:
                userAuthenticationViewModel.resendCode(userId);
                break;
        }
    }

    public Observer mBaseResponse() {
        return (Observer<BaseResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(AppConstant.IntentKey.USER_ID,userId);
                    moveActivity(mContext, CreateNewPasswordActivity.class, false, false, bundle);
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public Observer mForgotPasswordResponse() {
        return (Observer<BaseResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {

                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private class GenericTextWatcher implements TextWatcher {

        private View view;

        public GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()) {
                case R.id.edtVerifyCode1:
                    if (!StringHelper.isEmpty(text)) {
                        binding.edtVerifyCode2.requestFocus();
                    }
                    break;
                case R.id.edtVerifyCode2:
                    if (StringHelper.isEmpty(text)) {
                        binding.edtVerifyCode1.requestFocus();
                    } else {
                        binding.edtVerifyCode3.requestFocus();
                    }
                    break;
                case R.id.edtVerifyCode3:
                    if (StringHelper.isEmpty(text)) {
                        binding.edtVerifyCode2.requestFocus();
                    } else {
                        binding.edtVerifyCode4.requestFocus();
                    }
                    break;
                case R.id.edtVerifyCode4:
                    if (StringHelper.isEmpty(text)) {
                        binding.edtVerifyCode3.requestFocus();
                    } else {
                        binding.edtVerifyCode4.requestFocus();
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() != KeyEvent.ACTION_DOWN)
            return false;

        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (v.getId() == R.id.edtVerifyCode2) {
                binding.edtVerifyCode1.requestFocus();
            } else if (v.getId() == R.id.edtVerifyCode3) {
                binding.edtVerifyCode2.requestFocus();
            } else if (v.getId() == R.id.edtVerifyCode4) {
                binding.edtVerifyCode3.requestFocus();
            }
        } else if (keyCode == KeyEvent.KEYCODE_0 || keyCode == KeyEvent.KEYCODE_1 || keyCode == KeyEvent.KEYCODE_2
                || keyCode == KeyEvent.KEYCODE_3 || keyCode == KeyEvent.KEYCODE_4 || keyCode == KeyEvent.KEYCODE_5
                || keyCode == KeyEvent.KEYCODE_6 || keyCode == KeyEvent.KEYCODE_7 || keyCode == KeyEvent.KEYCODE_8
                || keyCode == KeyEvent.KEYCODE_9) {
            if (v.getId() == R.id.edtVerifyCode1) {
                if (binding.edtVerifyCode1.getText().length() > 0) {
                    binding.edtVerifyCode1.setText(String.valueOf(getNumberKey(keyCode)));
                    binding.edtVerifyCode2.requestFocus();
                    binding.edtVerifyCode1.setSelection(binding.edtVerifyCode1.getText().length());
                }
            } else if (v.getId() == R.id.edtVerifyCode2) {
                if (binding.edtVerifyCode2.getText().length() > 0) {
                    binding.edtVerifyCode2.setText(String.valueOf(getNumberKey(keyCode)));
                    binding.edtVerifyCode3.requestFocus();
                    binding.edtVerifyCode2.setSelection(binding.edtVerifyCode2.getText().length());
                }
            } else if (v.getId() == R.id.edtVerifyCode3) {
                if (binding.edtVerifyCode3.getText().length() > 0) {
                    binding.edtVerifyCode3.setText(String.valueOf(getNumberKey(keyCode)));
                    binding.edtVerifyCode4.requestFocus();
                    binding.edtVerifyCode3.setSelection(binding.edtVerifyCode3.getText().length());
                }
            } else if (v.getId() == R.id.edtVerifyCode4) {
                if (binding.edtVerifyCode4.getText().length() > 0) {
                    binding.edtVerifyCode4.setText(String.valueOf(getNumberKey(keyCode)));
                    binding.edtVerifyCode4.setSelection(binding.edtVerifyCode4.getText().length());
                }
            }
        }
        return false;
    }

    public int getNumberKey(int keycode) {
        int key = 0;
        switch (keycode) {
            case KeyEvent.KEYCODE_0:
                key = 0;
                break;
            case KeyEvent.KEYCODE_1:
                key = 1;
                break;
            case KeyEvent.KEYCODE_2:
                key = 2;
                break;
            case KeyEvent.KEYCODE_3:
                key = 3;
                break;
            case KeyEvent.KEYCODE_4:
                key = 4;
                break;
            case KeyEvent.KEYCODE_5:
                key = 5;
                break;
            case KeyEvent.KEYCODE_6:
                key = 6;
                break;
            case KeyEvent.KEYCODE_7:
                key = 7;
                break;
            case KeyEvent.KEYCODE_8:
                key = 8;
                break;
            case KeyEvent.KEYCODE_9:
                key = 9;
                break;

        }
        return key;
    }

    private boolean validateCode() {
        if (StringHelper.isEmpty(binding.edtVerifyCode1.getText().toString().trim())) {
            ToastHelper.error(mContext, getString(R.string.error_verify_code), Toast.LENGTH_SHORT, false);
            return false;
        } else if (StringHelper.isEmpty(binding.edtVerifyCode2.getText().toString().trim())) {
            ToastHelper.error(mContext, getString(R.string.error_verify_code), Toast.LENGTH_SHORT, false);
            return false;
        } else if (StringHelper.isEmpty(binding.edtVerifyCode3.getText().toString().trim())) {
            ToastHelper.error(mContext, getString(R.string.error_verify_code), Toast.LENGTH_SHORT, false);
            return false;
        } else if (StringHelper.isEmpty(binding.edtVerifyCode4.getText().toString().trim())) {
            ToastHelper.error(mContext, getString(R.string.error_verify_code), Toast.LENGTH_SHORT, false);
            return false;
        } else {
            code = binding.edtVerifyCode1.getText().toString()
                    + binding.edtVerifyCode2.getText().toString()
                    + binding.edtVerifyCode3.getText().toString()
                    + binding.edtVerifyCode4.getText().toString();
//            setOtp(code);
        }
        return true;

    }
}
