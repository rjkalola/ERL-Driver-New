package com.app.erldriver.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erldriver.R;
import com.app.erldriver.callback.SelectINavigationItemListener;
import com.app.erldriver.databinding.RowNavigationItemsListBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NavigationItemsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<String> listItemsTitle;
    private List<Integer> listItemsDrawable;
    private SelectINavigationItemListener listener;
    private TypedArray itemsDrawable;

    public NavigationItemsListAdapter(Context context, SelectINavigationItemListener listener) {
        this.mContext = context;
        this.listItemsTitle = new ArrayList<>(Arrays.asList(mContext.getResources().getStringArray(R.array.navigationItems)));
        this.listItemsDrawable = new ArrayList<>();
        itemsDrawable = mContext.getResources().obtainTypedArray(R.array.navigationItemsDrawable);
        for (int item : mContext.getResources().getIntArray(R.array.navigationItemsDrawable)) {
            listItemsDrawable.add(item);
        }

        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_navigation_items_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.binding.imgItemDrawable.setImageResource(itemsDrawable.getResourceId(position,0));
        itemViewHolder.getData(listItemsTitle.get(position));

        itemViewHolder.binding.routMainView.setOnClickListener(v -> {
            if(listener != null)
                listener.onSelectItem(position,listItemsTitle.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return listItemsTitle.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowNavigationItemsListBinding binding;

        public void getData(String title) {
            binding.setTitle(title);
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}
