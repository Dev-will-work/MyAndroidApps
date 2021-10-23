package com.example.helloandroid;

import android.app.Fragment;
import android.os.Bundle;

import java.util.Locale;
import java.util.TimeZone;

public class RetainedFragment extends Fragment {

    // data object we want to retain
    private String message;

    //this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setData(String message) {
        this.message = message;
    }

    public String getData() {
        return message;
    }
}