package com.app.erldriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erldriver.R;
import com.app.erldriver.callback.SelectItemListener;
import com.app.erldriver.databinding.RowServiceItemsTabBinding;
import com.app.erldriver.model.entity.info.ClientDashBoardInfo;

import java.util.List;

public class ServiceItemsTabAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ClientDashBoardInfo> list;
    private SelectItemListener listener;
    private int position;

    public ServiceItemsTabAdapter(Context context, List<ClientDashBoardInfo> list, int position, SelectItemListener listener) {
        this.mContext = context;
        this.list = list;
        this.listener = listener;
        this.position = position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_service_items_tab, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        ClientDashBoardInfo info = list.get(position);
        itemViewHolder.getData(info);

        if (this.position == position) {
            itemViewHolder.binding.txtTitle.setBackgroundResource(R.drawable.img_active_order_item_tab_bg);
            itemViewHolder.binding.txtTitle.setTextColor(mContext.getResources().getColor(R.color.colorYellowDark));
        } else {
            itemViewHolder.binding.txtTitle.setBackgroundResource(R.drawable.img_in_active_order_item_tab_bg);
            itemViewHolder.binding.txtTitle.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryText));
        }

        itemViewHolder.binding.txtTitle.setText(String.format(mContext.getString(R.string.lbl_display_tab_name_and_count), info.getName(), info.getCount_order_items()));
        itemViewHolder.binding.routMainView.setOnClickListener(v -> {
            if (listener != null) {
                setPosition(position);
                listener.onSelectItem(position, 0);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowServiceItemsTabBinding binding;

        public void getData(ClientDashBoardInfo info) {

        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void refreshAdapter(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

    public void refreshTabCount(int position,int count) {
        list.get(position).setCount_order_items(count);
        notifyDataSetChanged();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
