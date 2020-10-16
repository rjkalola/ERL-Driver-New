package com.app.erldriver.view.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.erldriver.R;
import com.app.erldriver.databinding.ActivityPrivacyPolicyBinding;
import com.app.erldriver.model.entity.response.PrivacyPolicyResponse;
import com.app.erldriver.util.AppConstant;
import com.app.erldriver.util.AppUtils;
import com.app.erldriver.util.LoginViewModelFactory;
import com.app.erldriver.util.ResourceProvider;
import com.app.erldriver.viewModel.DashBoardViewModel;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.StringHelper;

public class PrivacyPolicyActivity extends BaseActivity implements View.OnClickListener {
    private ActivityPrivacyPolicyBinding binding;
    private Context mContext;
    private int action;
    private DashBoardViewModel dashBoardViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy);
        mContext = this;

        dashBoardViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(DashBoardViewModel.class);
        dashBoardViewModel.createView(this);
        dashBoardViewModel.privacyPolicyResponse()
                .observe(this, getPrivacyPolicyResponse());

        binding.imgBack.setOnClickListener(this);

        getIntentData();
    }

    public void getIntentData() {
        if (getIntent().hasExtra(AppConstant.IntentKey.TYPE)) {
            action = getIntent().getIntExtra(AppConstant.IntentKey.TYPE, 0);
            if (action == AppConstant.Type.TERMS_CONDITIONS) {
                binding.txtTitle.setText(getString(R.string.terms_and_conditions));
            } else if (action == AppConstant.Type.PRIVACY_POLICY) {
                binding.txtTitle.setText(getString(R.string.privacy_policy));
            }
            dashBoardViewModel.getPrivacyPolicyRequest(action);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
        }
    }

    public Observer getPrivacyPolicyResponse() {
        return (Observer<PrivacyPolicyResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    if (!StringHelper.isEmpty(response.getInfo().getDescription())){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            binding.txtDescription.setText(Html.fromHtml(response.getInfo().getDescription(), Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            binding.txtDescription.setText(Html.fromHtml(response.getInfo().getDescription()));
                        }
                    }
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

}
