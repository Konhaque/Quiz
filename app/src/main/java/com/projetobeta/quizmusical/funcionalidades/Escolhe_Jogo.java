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

public class Escolhe_Jogo extends Fragment {
    private FrameLayout games;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.escolhe_jogo,container,false);
        return viewGroup;
    }

    @Override
    public void onStart() {
        iniciar_Objetos();
        click_Games();
        super.onStart();
    }

    private void iniciar_Objetos(){
        games = (FrameLayout) getActivity().findViewById(R.id.games);
    }

    private void click_Games(){
        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Jogar();
                Bundle args = new Bundle();
                args.putString("tipo","Games");
                fragment.setArguments(args);
                abre_Tela(fragment);
            }
        });
    }

    private void abre_Tela(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                android.R.anim.fade_in,android.R.anim.fade_out)
                .replace(R.id.settela,fragment).commit();
    }
}
