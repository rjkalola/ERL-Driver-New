package com.app.erldriver.network;

import com.app.erldriver.util.AppConstant;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @AutherBy Dhaval Jivani
 */
public abstract class RXRetroManager<T> {

    public RXRetroManager() {
    }

    protected abstract void onSuccess(T response);

    protected void onFailure(RetrofitException retrofitException, String errorCode) {
    }

    public void rxSingleCall(Observable<T> observable) {
        Observable<DisposableObserver<T>> disposable = Observable.just(observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<T>() {
                    @Override
                    public void onNext(T value) {
                        onSuccess(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            if (e instanceof RetrofitException) {
                                onFailure((RetrofitException) e, "");
                            } else {
                                onFailure(null, AppConstant.ERROR_UNKNOWN);
                            }
                        } catch (Exception ex) {
                            onFailure(null, AppConstant.ERROR_UNKNOWN);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

}
