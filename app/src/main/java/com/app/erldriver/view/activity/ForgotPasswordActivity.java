package com.app.erldriver.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.erldriver.R;
import com.app.erldriver.databinding.ActivityForgotPasswordBinding;
import com.app.erldriver.model.entity.response.ForgotPasswordResponse;
import com.app.erldriver.util.AppConstant;
import com.app.erldriver.util.AppUtils;
import com.app.erldriver.util.LoginViewModelFactory;
import com.app.erldriver.util.ResourceProvider;
import com.app.erldriver.viewModel.UserAuthenticationViewModel;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.ValidationUtil;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener {
    private ActivityForgotPasswordBinding binding;
    private Context mContext;
    private UserAuthenticationViewModel userAuthenticationViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        mContext = this;
        userAuthenticationViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(UserAuthenticationViewModel.class);
        userAuthenticationViewModel.createView(this);
        userAuthenticationViewModel.mForgotPasswordResponse()
                .observe(this, mForgotPasswordResponse());

        binding.txtResetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtResetPassword:
                if (isValid()) {
                    userAuthenticationViewModel.forgotPasswordRequest(binding.edtEmail.getText().toString().trim());
                }
                break;
        }
    }

    public Observer mForgotPasswordResponse() {
        return (Observer<ForgotPasswordResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(AppConstant.IntentKey.USER_ID, response.getUser_id());
                    bundle.putString(AppConstant.IntentKey.VERIFICATION_CODE, response.getAccess_code());
                    bundle.putString(AppConstant.IntentKey.EMAIL, binding.edtEmail.getText().toString().trim());
                    moveActivity(mContext, VerificationActivity.class, false, false, bundle);
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public boolean isValid() {
        boolean isValid = true;
        if (!ValidationUtil.isEmptyEditText(binding.edtEmail.getText().toString())) {
            if (ValidationUtil.isValidEmail(binding.edtEmail.getText().toString())) {
                binding.edtEmail.setError(null);
            } else {
                ValidationUtil.setErrorIntoEditext(binding.edtEmail, mContext.getString(R.string.error_invalid_email));
                isValid = false;
            }
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtEmail, mContext.getString(R.string.error_empty_email));
            isValid = false;
        }
        return isValid;
    }
}
