package com.app.erldriver.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.erldriver.R;
import com.app.erldriver.databinding.ActivityCreateNewPasswordBinding;
import com.app.erldriver.databinding.ActivityLoginBinding;
import com.app.erldriver.model.entity.response.BaseResponse;
import com.app.erldriver.util.AppConstant;
import com.app.erldriver.util.AppUtils;
import com.app.erldriver.util.LoginViewModelFactory;
import com.app.erldriver.util.ResourceProvider;
import com.app.erldriver.viewModel.UserAuthenticationViewModel;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.ValidationUtil;

public class CreateNewPasswordActivity extends BaseActivity implements View.OnClickListener {
    private ActivityCreateNewPasswordBinding binding;
    private Context mContext;
    private UserAuthenticationViewModel userAuthenticationViewModel;
    private int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_new_password);
        mContext = this;
        userAuthenticationViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(UserAuthenticationViewModel.class);
        userAuthenticationViewModel.createView(this);
        userAuthenticationViewModel.mBaseResponse()
                .observe(this, mBaseResponse());

        binding.txtResetPassword.setOnClickListener(this);

        getIntentData();
    }

    public void getIntentData() {
        if (getIntent().getExtras() != null && getIntent().hasExtra(AppConstant.IntentKey.USER_ID)) {
            userId = getIntent().getIntExtra(AppConstant.IntentKey.USER_ID, 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtResetPassword:
                if (isValid()) {
                    userAuthenticationViewModel.resetPassword(userId, binding.edtPassword.getText().toString().trim());
                }
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
                    moveActivity(mContext, LoginActivity.class, true, true, null);
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
        if (!ValidationUtil.isEmptyEditText(binding.edtConfirmPassword.getText().toString().trim())) {
            if (ValidationUtil.isValidConfirmPasswrod(binding.edtConfirmPassword.getText().toString().trim(), binding.edtPassword.getText().toString().trim())) {
                binding.edtConfirmPassword.setError(null);
            } else {
                ValidationUtil.setErrorIntoEditext(binding.edtConfirmPassword, mContext.getString(R.string.error_not_match_password));
                isValid = false;
            }
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtConfirmPassword, mContext.getString(R.string.error_empty_confirm_password));
            isValid = false;
        }

        if (!ValidationUtil.isEmptyEditText(binding.edtPassword.getText().toString().trim())) {
            binding.edtPassword.setError(null);
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtPassword, mContext.getString(R.string.error_empty_password));
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void onBackPressed() {
        moveActivity(mContext, LoginActivity.class, true, true, null);
    }
}
