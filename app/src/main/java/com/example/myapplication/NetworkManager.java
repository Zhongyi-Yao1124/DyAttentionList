package com.example.myapplication;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import okhttp3.*;

public class NetworkManager {
    private static final String TAG = "NetworkManager";
    private static NetworkManager instance;
    private OkHttpClient client;
    private Gson gson;

    private static final String BASE_URL = "https://4cae045b-6953-475b-a887-f2a8eba82e6f.mock.pstmn.io";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private Map<String, Call> ongoingCalls = new ConcurrentHashMap<>();
    private NetworkManager() {
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        gson = new GsonBuilder().create();
    }

    public static synchronized NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    public void cancelAllCalls() {
        for (Call call : ongoingCalls.values()) {
            if (call != null && !call.isCanceled()) {
                call.cancel();
            }
        }
        ongoingCalls.clear();
    }

    public interface ApiCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }


    public void getAttentionList(int page, int size, ApiCallback<List<User>> callback) {
        String url = BASE_URL + "/api/users?page=" + page + "&size=" + size;
        Log.d(TAG, "request: " + url);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "获取关注列表失败: " + e.getMessage());
                if (callback != null) {
                    callback.onError("网络请求失败: " + e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    String errorMsg = "请求失败，状态码: " + response.code();
                    Log.e(TAG, errorMsg);
                    if (callback != null) {
                        callback.onError(errorMsg);
                    }
                    return;
                }

                String responseData = response.body().string();
                Log.d(TAG, "response: " + responseData);

                try {
                    // json解析
                    ApiResponse apiResponse = gson.fromJson(responseData, ApiResponse.class);

                    if (apiResponse != null && apiResponse.getUsersData() != null) {
                        List<User> userList = apiResponse.getUsersData().getData();
                        Log.d(TAG, "解析成功，获取到 " + userList.size() + " 个用户");

                        if (callback != null) {
                            callback.onSuccess(userList);
                        }
                    } else {
                        if (callback != null) {
                            callback.onError("数据解析错误：响应格式不正确");
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "JSON解析错误: " + e.getMessage());
                    if (callback != null) {
                        callback.onError("数据解析错误: " + e.getMessage());
                    }
                }
            }
        });
    }
}