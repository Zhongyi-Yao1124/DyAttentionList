package com.example.myapplication;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AttentionAdapter extends RecyclerView.Adapter<AttentionAdapter.ViewHolder>{
    private List<User> userList;
    private OnUserClickListener listener;

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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvName.setText(user.getName());

        if (user.getStatus() == 1) {
            holder.btnAttention.setText("已关注");
            holder.btnAttention.setBackgroundColor(Color.parseColor("#F5F5F5"));//这个色号看上去不错。。
            holder.btnAttention.setTextColor(Color.BLACK);
        } else {
            holder.btnAttention.setText("关注");
            holder.btnAttention.setBackgroundColor(Color.RED);
            holder.btnAttention.setTextColor(Color.WHITE);
        }

        holder.btnAttention.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAttentionClick(position);
            }
        });
        holder.btnMore.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMoreClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        Button btnAttention;
        ImageButton btnMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            btnAttention = itemView.findViewById(R.id.btn_attention);
            btnMore = itemView.findViewById(R.id.btn_more);
        }
    }
}
