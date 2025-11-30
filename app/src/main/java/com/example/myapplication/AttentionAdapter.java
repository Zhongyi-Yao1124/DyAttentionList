package com.example.myapplication;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;

public class AttentionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_LOADING = 1;

    private List<User> userList;
    private OnUserClickListener listener;
    private boolean isLoading = false;

    public interface OnUserClickListener {
        void onAttentionClick(int position);
        void onMoreClick(int position);
    }

    public AttentionAdapter(List<User> userList, OnUserClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_list, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            User user = userList.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.tvName.setText(user.getName());

            //头像
            if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                Glide.with(viewHolder.ivAvatar.getContext())
                        .load(user.getAvatarUrl())
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .placeholder(R.drawable.ic_avator_placeholder) // 添加默认头像占位图
                        .into(viewHolder.ivAvatar);
            } else {
               //默认
                viewHolder.ivAvatar.setImageResource(R.drawable.ic_avator_placeholder);
            }

            if (user.getStatus()==1) {
                viewHolder.btnAttention.setText("已关注");
                viewHolder.btnAttention.setBackgroundColor(Color.parseColor("#F5F5F5"));
                viewHolder.btnAttention.setTextColor(Color.BLACK);
            } else {
                viewHolder.btnAttention.setText("关注");
                viewHolder.btnAttention.setBackgroundColor(Color.RED);
                viewHolder.btnAttention.setTextColor(Color.WHITE);
            }

            viewHolder.btnAttention.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAttentionClick(position);
                }
            });

            viewHolder.btnMore.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMoreClick(position);
                }
            });
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size() + (isLoading ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == userList.size() && isLoading) {
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    public void setLoading(boolean loading) {
        if (isLoading != loading) {
            isLoading = loading;
            if (loading) {
                notifyItemInserted(userList.size());
            } else {
                notifyItemRemoved(userList.size());
            }
        }
    }

    public void addUsers(List<User> newUsers) {
        int startPosition = userList.size();
        userList.addAll(newUsers);
        notifyItemRangeInserted(startPosition, newUsers.size());
    }

    public void clear() {
        userList.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvName;
        Button btnAttention;
        ImageButton btnMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            btnAttention = itemView.findViewById(R.id.btn_attention);
            btnMore = itemView.findViewById(R.id.btn_more);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}