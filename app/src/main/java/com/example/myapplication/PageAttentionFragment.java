package com.example.myapplication;

import android.os.Bundle;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
    private NetworkManager networkManager;

    // 分页
    private int currentPage = 1;
    private static final int PAGE_SIZE = 10;
    private boolean hasMore = true;
    private boolean isLoading = false;

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
        networkManager = NetworkManager.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        loadFirstPage();
    }

    private void setupRecyclerView() {
        adapter = new AttentionAdapter(userList, new AttentionAdapter.OnUserClickListener() {
            @Override
            public void onAttentionClick(int position) {
                User user = userList.get(position);

            }

            @Override
            public void onMoreClick(int position) {
                User user = userList.get(position);

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // 滚动监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!isLoading && hasMore && dy > 0) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    assert layoutManager != null;
                    int totalItemCount = layoutManager.getItemCount();
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                    // 当滚动到倒数第3个item时开始加载下一页
                    if ((lastVisibleItemPosition + 3) >= totalItemCount) {
                        loadNextPage();
                    }
                }
            }
        });
    }

    private void loadFirstPage() {
        currentPage = 1;
        hasMore = true;
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        loadDataFromServer(currentPage);
    }

    private void loadNextPage() {
        if (isLoading || !hasMore) return;

        isLoading = true;
        adapter.setLoading(true);
        currentPage++;

        loadDataFromServer(currentPage);
    }

    private void loadDataFromServer(int page) {
        networkManager.getAttentionList(page, PAGE_SIZE, new NetworkManager.ApiCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                mainHandler.post(() -> {
                    isLoading = false;
                    adapter.setLoading(false);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    if (page == 1) {
                        userList.clear();
                        adapter.clear();
                    }

                    if (users != null && !users.isEmpty()) {
                        userList.addAll(users);
                        adapter.addUsers(users);

                        hasMore = users.size() >= PAGE_SIZE;
                    } else {
                        hasMore = false;
                    }

                    if (userList.isEmpty() && page == 1) {
                        showEmptyView();
                    }
                });
            }

            @Override
            public void onError(String error) {
                mainHandler.post(() -> {
                    isLoading = false;
                    adapter.setLoading(false);
                    progressBar.setVisibility(View.GONE);

                    if (page == 1) {
                        Toast.makeText(getContext(), "加载数据失败: " + error, Toast.LENGTH_SHORT).show();
                        showEmptyView();
                    } else {
                        currentPage--;
                        Toast.makeText(getContext(), "加载更多失败: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }



    private void showEmptyView() {
        Toast.makeText(getContext(), "暂无关注用户", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (recyclerView != null) {
            recyclerView.setAdapter(null);
        }
        networkManager.cancelAllCalls();
        if (getContext() != null) {
            Glide.with(this).pauseAllRequests();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        // 重新加载第一页数据，确保数据最新
        if (userList.isEmpty()) {
            loadFirstPage();
        }
    }
}