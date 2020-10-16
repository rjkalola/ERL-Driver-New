package com.app.erldriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erldriver.R;
import com.app.erldriver.callback.SelectItemListener;
import com.app.erldriver.model.entity.info.MessageInfo;
import com.app.erldriver.util.AppConstant;
import com.app.erldriver.util.AppUtils;
import com.app.utilities.utils.Constant;
import com.app.utilities.utils.GlideUtil;
import com.app.utilities.utils.StringHelper;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<MessageInfo> messageList;
    private int currentUserId = 0;
    private SelectItemListener listener;

    public ChatAdapter(Context context, List<MessageInfo> list, SelectItemListener listener) {
        this.mContext = context;
        currentUserId = AppUtils.getUserPrefrence(mContext).getId();
        this.messageList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == AppConstant.Type.ME) {
            itemView = inflater.inflate(R.layout.row_chat_right_item, parent, false);
        } else if (viewType == AppConstant.Type.FRIEND) {
            itemView = inflater.inflate(R.layout.row_chat_left_item, parent, false);
        }
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        MessageInfo info = messageList.get(position);

        itemViewHolder.txtTime.setText(info.getCreated_date());
        setUserImage(itemViewHolder.imgUserPic, info.getUser_image());

        if (!StringHelper.isEmpty(info.getImage())) {
            itemViewHolder.txtMsg.setVisibility(View.GONE);
            itemViewHolder.imgChat.setVisibility(View.VISIBLE);
            setImage(itemViewHolder.imgChat, info.getImage());
        } else {
            itemViewHolder.txtMsg.setVisibility(View.VISIBLE);
            itemViewHolder.imgChat.setVisibility(View.GONE);
            itemViewHolder.txtMsg.setText(info.getMessage());
        }

        itemViewHolder.parentView.setOnClickListener(v -> {
            if (listener != null)
                listener.onSelectItem(position, AppConstant.Action.PREVIEW_IMAGE);
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getFrom_user_id() == currentUserId) {
            return AppConstant.Type.ME;
        } else {
            return AppConstant.Type.FRIEND;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView txtMsg, txtTime;
        private RelativeLayout parentView;
        private ImageView imgUserPic, imgChat;

        public ItemViewHolder(View itemView) {
            super(itemView);
            txtMsg = itemView.findViewById(R.id.txtMsg);
            txtTime = itemView.findViewById(R.id.txtTime);
            imgUserPic = itemView.findViewById(R.id.imgUserImage);
            imgChat = itemView.findViewById(R.id.imgChat);
            parentView = itemView.findViewById(R.id.parentView);
        }
    }

    private void setUserImage(ImageView imageView, String url) {
        if (!StringHelper.isEmpty(url)) {
            GlideUtil.loadImageUsingGlideTransformation(url, imageView, Constant.TransformationType.CIRCLECROP_TRANSFORM, null, null, Constant.ImageScaleType.CENTER_CROP, 0, 0, "", 0, null);
        }
    }

    private void setImage(ImageView imageView, String url) {
        if (!StringHelper.isEmpty(url)) {
            GlideUtil.loadImage(url, imageView, null, null, 0, null);
        }
    }

    public void addMessage(MessageInfo info) {
        messageList.add(info);
        notifyDataSetChanged();
    }

    public void addMessages(List<MessageInfo> list) {
        messageList.addAll(list);
        notifyDataSetChanged();
    }

    public List<MessageInfo> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageInfo> messageList) {
        this.messageList = messageList;
    }
}