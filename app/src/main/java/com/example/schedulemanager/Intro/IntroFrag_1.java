package com.example.schedulemanager.Intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.schedulemanager.R;

public class IntroFrag_1 extends Fragment {

    public IntroFrag_1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intro_frag_1, container, false);
    }
}