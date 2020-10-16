package com.app.erldriver.binding;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erldriver.BR;


/**
 * Created by Dhaval on 29.06.17.
 */
public class RecyclerConfiguration extends BaseObservable {

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.ItemAnimator itemAnimator;
    private RecyclerView.Adapter adapter;

    @Bindable
    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        notifyPropertyChanged(BR.layoutManager);
    }

    @Bindable
    public RecyclerView.ItemAnimator getItemAnimator() {
        return itemAnimator;
    }

    public void setItemAnimator(RecyclerView.ItemAnimator itemAnimator) {
        this.itemAnimator = itemAnimator;
        notifyPropertyChanged(BR.itemAnimator);
    }

    @Bindable
    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @BindingAdapter("configuration")
    public static void configureRecyclerView(RecyclerView recyclerView, RecyclerConfiguration configuration) {
        recyclerView.setLayoutManager(configuration.getLayoutManager());
        recyclerView.setItemAnimator(configuration.getItemAnimator());
        recyclerView.setAdapter(configuration.getAdapter());
    }
}
