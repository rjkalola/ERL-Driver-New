package com.app.erldriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erldriver.R;
import com.app.erldriver.callback.SelectItemListener;
import com.app.erldriver.databinding.RowAddressListBinding;
import com.app.erldriver.model.entity.request.SaveAddressRequest;
import com.app.erldriver.util.AppConstant;

import java.util.List;

public class AddressListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<SaveAddressRequest> list;
    private SelectItemListener listener;
    private int position;

    public AddressListAdapter(Context context, List<SaveAddressRequest> list, SelectItemListener listener) {
        this.mContext = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_address_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        SaveAddressRequest info = list.get(position);
        itemViewHolder.getData(info);

        itemViewHolder.binding.routMainView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSelectItem(position, AppConstant.Action.SELECT_ADDRESS);
            }
        });

        itemViewHolder.binding.routMainView.setOnLongClickListener(v -> {
            this.position = position;
            showManageAddressMenu();
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowAddressListBinding binding;

        public void getData(SaveAddressRequest info) {
            binding.setInfo(info);
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void showManageAddressMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        String[] items = {mContext.getString(R.string.edit), mContext.getString(R.string.delete)};
        builder.setItems(items, (dialog, which) -> {
            switch (which) {
                case 0:
                    listener.onSelectItem(position, AppConstant.Action.EDIT_ADDRESS);
                    break;
                case 1:
                    listener.onSelectItem(position, AppConstant.Action.DELETE_ADDRESS);
                    break;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
