package com.app.erldriver.callback;

public interface Presenter<T> {

    void createView(T view);

    void destroyView();
}
