package com.projetobeta.quizmusical.funcionalidades;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.projetobeta.quizmusical.R;
import com.projetobeta.quizmusical.funcionalidades.multiplayer.Salas;
import com.projetobeta.quizmusical.generalfunctions.GeneralFunctions;

public class ModoDeJogo extends Fragment {

    private FrameLayout multiplayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.modo_de_jogo,container,false);
    }

    @Override
    public void onStart() {
        iniciarObejetos();
        setMultiplayer();
        super.onStart();
    }

    private void iniciarObejetos(){
        multiplayer = (FrameLayout) getActivity().findViewById(R.id.multiplayer);
    }

    private void setMultiplayer(){
        multiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GeneralFunctions().abreTela(getActivity().getSupportFragmentManager(),new Salas(),R.id.settela);
            }
        });
    }
}
