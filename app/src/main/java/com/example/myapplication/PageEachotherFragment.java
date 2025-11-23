package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class PageEachotherFragment extends Fragment {
    public static PageEachotherFragment newInstance(){
        return new PageEachotherFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_page_eachother,viewGroup,false);
    }
}
