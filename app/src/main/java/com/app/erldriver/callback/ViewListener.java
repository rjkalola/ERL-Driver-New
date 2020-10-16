package com.app.erldriver.callback;

import com.app.erldriver.network.RetrofitException;

public interface ViewListener {

    void showProgress();

    void hideProgress();

    void showApiError(RetrofitException retrofitException, String errorCode);
}
