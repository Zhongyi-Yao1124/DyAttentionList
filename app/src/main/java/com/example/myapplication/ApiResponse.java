package com.example.myapplication;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiResponse {
    @SerializedName("users")
    private UsersData usersData;

    public UsersData getUsersData() { return usersData; }
    public void setUsersData(UsersData usersData) { this.usersData = usersData; }

    public static class UsersData {
        @SerializedName("data")
        private List<User> data;

        @SerializedName("page")
        private int page;

        @SerializedName("size")
        private int size;

        @SerializedName("total")
        private int total;

        public List<User> getData() { return data; }
        public int getPage() { return page; }
        public int getSize() { return size; }
        public int getTotal() { return total; }

        public void setData(List<User> data) { this.data = data; }
        public void setPage(int page) { this.page = page; }
        public void setSize(int size) { this.size = size; }
        public void setTotal(int total) { this.total = total; }
    }
}