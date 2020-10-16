package com.app.erldriver.callback;

import android.content.Context;

public interface BaseCallBack {

    void showProgress(Context context, String message);

    void hideProgress();
}