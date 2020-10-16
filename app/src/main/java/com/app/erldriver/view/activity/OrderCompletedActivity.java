package com.app.erldriver.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.erldriver.R;
import com.app.erldriver.databinding.ActivityOrderCompletedBinding;

public class OrderCompletedActivity extends BaseActivity implements View.OnClickListener {
    private ActivityOrderCompletedBinding binding;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_completed);
        mContext = this;
        binding.txtHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtHome:
                moveActivity(mContext, DashBoardActivity.class, true, true, null);
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }

}
