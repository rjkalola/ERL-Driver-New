package com.app.erldriver.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.erldriver.ERLApp;
import com.app.erldriver.R;
import com.app.erldriver.databinding.ActivitySplashBinding;
import com.app.erldriver.util.AppConstant;
import com.app.utilities.utils.StringHelper;

public class SplashActivity extends BaseActivity {
    private ActivitySplashBinding binding;
    private int SPLASH_TIME_OUT = 2500;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        mContext = this;
        callTimerCount();

//        Log.e("test","TimeZone1:"+ TimeZone.getDefault().getID());
//
//        Calendar cal = Calendar.getInstance();
//        TimeZone tz = cal.getTimeZone();
//        Log.e("test","TimeZone2"+tz.getID());
//        Log.e("test","TimeZone2"+tz.getDisplayName(true, TimeZone.SHORT));


    }

    public void callTimerCount() {
        new Handler().postDelayed(() -> {
            String userInfo = ERLApp.preferenceGetString(AppConstant.SharedPrefKey.USER_INFO, "");
            if (StringHelper.isEmpty(userInfo) || userInfo.equalsIgnoreCase("null")) {
                moveActivity(mContext, LoginActivity.class, true, true, null);
            } else {
                moveActivity(mContext, DashBoardActivity.class, true, true, null);
            }
        }, SPLASH_TIME_OUT);
    }
}
