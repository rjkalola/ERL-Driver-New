package com.app.erldriver.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.erldriver.R;
import com.app.erldriver.databinding.ActivitySignUpBinding;
import com.app.erldriver.model.entity.response.UserResponse;
import com.app.erldriver.util.AppUtils;
import com.app.erldriver.util.LoginViewModelFactory;
import com.app.erldriver.util.ResourceProvider;
import com.app.erldriver.viewModel.UserAuthenticationViewModel;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.ToastHelper;
import com.app.utilities.utils.ValidationUtil;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {
    private ActivitySignUpBinding binding;
    private UserAuthenticationViewModel userAuthenticationViewModel;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        mContext = this;
        userAuthenticationViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(UserAuthenticationViewModel.class);
        userAuthenticationViewModel.createView(this);
        userAuthenticationViewModel.mUserResponse()
                .observe(this, getUserResponse());
        binding.setUserAuthenticationViewModel(userAuthenticationViewModel);

        binding.txtSignUp.setOnClickListener(this);
        binding.txtLogin.setOnClickListener(this);

//        binding.edtName.addTextChangedListener(this);
//        binding.edtEmail.addTextChangedListener(this);
//        binding.edtPassword.addTextChangedListener(this);
//        binding.edtPhoneNumber.addTextChangedListener(this);
//        binding.edtConfirmPassword.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtLogin:
                finish();
                break;
            case R.id.txtSignUp:
                if (isValid()) {
                    if (AppUtils.isNetworkConnected(mContext))
                        userAuthenticationViewModel.sendSignUpRequest();
                    else
                        ToastHelper.error(mContext, getString(R.string.error_internet_connection), Toast.LENGTH_SHORT, false);
                }
                break;
        }
    }

    public Observer getUserResponse() {
        return (Observer<UserResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    AppUtils.setUserPrefrence(mContext, response.getInfo());
                    moveActivity(mContext, DashBoardActivity.class, true, true, null);
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

        if (!ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getSignUpRequest().getConfirmPassword())) {
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

        if (!ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getSignUpRequest().getPassword())) {
            binding.edtPassword.setError(null);
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtPassword, mContext.getString(R.string.error_empty_password));
            isValid = false;
        }

        if (!ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getSignUpRequest().getPhone())) {
            binding.edtPhoneNumber.setError(null);
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtPhoneNumber, mContext.getString(R.string.error_empty_phone));
            isValid = false;
        }

        if (!ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getSignUpRequest().getEmail())) {
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

        if (!ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getSignUpRequest().getName())) {
            binding.edtName.setError(null);
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtName, mContext.getString(R.string.error_empty_name));
            isValid = false;
        }

        return isValid;
    }

    public void isEnableButton() {
        boolean isValid = true;

        if (!ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getSignUpRequest().getConfirmPassword())) {
            if (!ValidationUtil.isValidConfirmPasswrod(binding.edtConfirmPassword.getText().toString().trim(), binding.edtPassword.getText().toString().trim())) {
                isValid = false;
            }
        } else {
            isValid = false;
        }

        if (ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getSignUpRequest().getPassword())) {
            isValid = false;
        }

        if (ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getSignUpRequest().getPhone())) {
            isValid = false;
        }

        if (!ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getSignUpRequest().getEmail())) {
            if (!ValidationUtil.isValidEmail(binding.edtEmail.getText().toString())) {
                isValid = false;
            }
        } else {
            isValid = false;
        }

        if (ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getSignUpRequest().getName())) {
            isValid = false;
        }

        if (isValid)
            binding.btnSignUp.setAlpha(1f);
        else
            binding.btnSignUp.setAlpha(0.5f);
    }
}
