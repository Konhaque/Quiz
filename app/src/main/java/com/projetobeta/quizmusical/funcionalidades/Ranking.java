package com.projetobeta.quizmusical.funcionalidades;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.projetobeta.quizmusical.R;

public class Ranking extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.ranking,container,false);
        return viewGroup;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
