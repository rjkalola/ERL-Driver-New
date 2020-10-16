package com.app.erldriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erldriver.R;
import com.app.erldriver.callback.SelectedServiceItemListener;
import com.app.erldriver.databinding.RowServiceSelectedItemsTitleListBinding;
import com.app.erldriver.model.entity.info.ItemInfo;
import com.app.erldriver.model.entity.info.ServiceItemInfo;

import java.util.List;

public class ServiceSelectedItemsTitleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ItemInfo> list;
    private SelectedServiceItemListener listener;
    private int position;

    public ServiceSelectedItemsTitleListAdapter(Context context, List<ItemInfo> list, SelectedServiceItemListener listener) {
        this.mContext = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_service_selected_items_title_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        ItemInfo info = list.get(position);
        itemViewHolder.getData(info);
        itemViewHolder.binding.txtTitle.setText(info.getName());
        setSelectedItemsAdapter(itemViewHolder.binding.rvSelectedItems, info.getServiceList(), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowServiceSelectedItemsTitleListBinding binding;

        public void getData(ItemInfo info) {
            binding.setInfo(info);
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    private void setSelectedItemsAdapter(RecyclerView recyclerView, List<ServiceItemInfo> service_item, int position) {
        if (service_item != null && service_item.size() > 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            recyclerView.setHasFixedSize(true);
            ServiceSelectedItemsListAdapter adapter = new ServiceSelectedItemsListAdapter(mContext, service_item, position, listener);
            recyclerView.setAdapter(adapter);
        }
    }

//    @Override
//    public void onSelectItem(int position, int quantity) {
//        Log.e("test", "position:" + position + " Quantity:" + position);
//    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
