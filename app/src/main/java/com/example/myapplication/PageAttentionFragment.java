package com.example.myapplication;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class PageAttentionFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private AttentionAdapter adapter;
    private List<User> userList = new ArrayList<>();
    private DAO userDao;
    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    public static PageAttentionFragment newInstance() {
        return new PageAttentionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_attention, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);

        userDao = new DAO(getContext());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        loadDataFromDatabase();
    }

    private void setupRecyclerView() {
        adapter = new AttentionAdapter(userList, new AttentionAdapter.OnUserClickListener() {
            @Override
            public void onAttentionClick(int position) {
                User user = userList.get(position);
                toggleUserStatus(user, position);
            }

            @Override
            public void onMoreClick(int position) {
                User user = userList.get(position);
                showUserDetailDialog(user);
            }

        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
    private void showUserDetailDialog(User user) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.user_detail, null);
        bottomSheetDialog.setContentView(dialogView);

        TextView tvUserName = dialogView.findViewById(R.id.tv_user_name);
        TextView tvDyNumber = dialogView.findViewById(R.id.tv_dy_number);
        Switch switchSpecialAttention = dialogView.findViewById(R.id.switch_special_attention);
//        Button btnSetRemark = dialogView.findViewById(R.id.layout_set_remark).findViewById(R.id.btn_set_remark);
        TextView tvUnattention = dialogView.findViewById(R.id.tv_unattention);
        EditText etRemark = dialogView.findViewById(R.id.et_remark);

        tvUserName.setText(user.getName());
        tvDyNumber.setText("抖音号：" + user.getDyName());
        loadSpecialSwitch(user.getId(), switchSpecialAttention);
        executor.execute(() -> {
            String remark = userDao.getRemarkContent(user.getId(), 1);
            mainHandler.post(() -> etRemark.setText(remark));
        });
        bottomSheetDialog.setOnDismissListener(dialogInterface -> {
            String remark = etRemark.getText().toString();
            executor.execute(() -> userDao.saveOrUpdateRemark(user.getId(), 1, remark));
        });

        switchSpecialAttention.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSpecialAttention(user.getId(), isChecked ? 1 : 0);
        });


        tvUnattention.setOnClickListener(v -> {
            user.setStatus(0); // 设置为未关注状态

            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).getId() == user.getId()) {
                    userList.get(i).setStatus(0);
                    adapter.notifyItemChanged(i);
                    break;
                }
            }

            executor.execute(() -> {
                try {
                    userDao.updateUser(user);
                    mainHandler.post(() -> {
                        Toast.makeText(getContext(), "已取消关注", Toast.LENGTH_SHORT).show();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    mainHandler.post(() -> {
                        Toast.makeText(getContext(), "操作失败", Toast.LENGTH_SHORT).show();
                        user.setStatus(1);
                        for (int i = 0; i < userList.size(); i++) {
                            if (userList.get(i).getId() == user.getId()) {
                                userList.get(i).setStatus(1);
                                adapter.notifyItemChanged(i);
                                break;
                            }
                        }
                    });
                }
            });

            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }
    private void loadSpecialSwitch(int userId, Switch switchSpecialAttention) {
        executor.execute(() -> {
            try {
                int ifSpecial = userDao.getIfSpecial(userId, 1);
                mainHandler.post(() -> {
                    switchSpecialAttention.setChecked(ifSpecial == 1);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void updateSpecialAttention(int userId, int ifSpecial) {
        executor.execute(() -> {
            try {
                userDao.updateSpecialAttention(userId, 1, ifSpecial); // pId默认为1
                mainHandler.post(() -> {
                    String message = ifSpecial == 1 ? "已设置为特别关注" : "已取消特别关注";
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> {
                    Toast.makeText(getContext(), "操作失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void loadDataFromDatabase() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        executor.execute(() -> {
            try {
                List<User> users = userDao.getUserList();
                mainHandler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    userList.clear();
                    userList.addAll(users);
                    adapter.notifyDataSetChanged();

                    if (userList.isEmpty()) {
                        showEmptyView();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "加载数据失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void toggleUserStatus(User user, int position) {
        executor.execute(() -> {
            try {
                int newUserStatus = 1 - user.getStatus();
                user.setStatus(newUserStatus);
                userDao.updateUser(user);

                mainHandler.post(() -> {
                    if (newUserStatus == 1) {
                        if (position >= 0 && position < userList.size()) {
                            userList.set(position, user);
                            adapter.notifyItemChanged(position);
                        }
                        Toast.makeText(getContext(), "已关注", Toast.LENGTH_SHORT).show();
                    } else {
                        if (position >= 0 && position < userList.size()) {
                            userList.set(position, user);
                            adapter.notifyItemChanged(position);
                            Toast.makeText(getContext(), "已取消关注", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> {
                    Toast.makeText(getContext(), "操作失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    

    private void showEmptyView() {
        // 显示空数据提示
        Toast.makeText(getContext(), "暂无关注用户", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataFromDatabase();
    }

    @Override
    public void onDestroy() {
        userDao.close();
        super.onDestroy();
    }
}