package com.app.erldriver.view.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.app.erldriver.R;
import com.app.erldriver.databinding.ActivityOurServiceDetailsBinding;
import com.app.erldriver.model.entity.info.PrivacyPolicyInfo;
import com.app.erldriver.model.entity.response.PrivacyPolicyResponse;
import com.app.erldriver.util.AppConstant;
import com.app.erldriver.util.AppUtils;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.Constant;
import com.app.utilities.utils.GlideUtil;
import com.app.utilities.utils.StringHelper;

import org.parceler.Parcels;

public class OurServiceDetailsActivity extends BaseActivity implements View.OnClickListener {
    private ActivityOurServiceDetailsBinding binding;
    private Context mContext;
    private PrivacyPolicyInfo info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_our_service_details);
        mContext = this;

        binding.imgBack.setOnClickListener(this);

        getIntentData();
    }

    public void getIntentData() {
        if (getIntent().getExtras() != null
                && Parcels.unwrap(getIntent().getExtras().getParcelable(AppConstant.IntentKey.OUR_SERVICE_INFO)) != null) {
            setInfo(Parcels.unwrap(getIntent().getExtras().getParcelable(AppConstant.IntentKey.OUR_SERVICE_INFO)));

            binding.txtServiceName.setText(getInfo().getName());

            if (!StringHelper.isEmpty(getInfo().getDescription())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.txtDescription.setText(Html.fromHtml(getInfo().getDescription(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    binding.txtDescription.setText(Html.fromHtml(getInfo().getDescription()));
                }
            }

            if (!StringHelper.isEmpty(info.getImage()))
                GlideUtil.loadImage(info.getImage(), binding.imgService, null, null, Constant.ImageScaleType.CENTER_CROP, null);
        } else {
            finish();
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
                    if (!StringHelper.isEmpty(response.getInfo().getDescription())) {
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

    public PrivacyPolicyInfo getInfo() {
        return info;
    }

    public void setInfo(PrivacyPolicyInfo info) {
        this.info = info;
    }
}
