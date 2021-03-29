package com.projetobeta.quizmusical.funcionalidades.multiplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.projetobeta.quizmusical.R;
import com.projetobeta.quizmusical.generalfunctions.GeneralFunctions;

public class Salas extends Fragment {

    private FrameLayout criarSala;
    private FrameLayout salasCriadas;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.salas,container,false);
    }

    @Override
    public void onStart() {
        iniciar_Objetos();
        setCriarSala();
        setSalasCriadas();
        super.onStart();
    }

    private void iniciar_Objetos(){
        salasCriadas = (FrameLayout) getActivity().findViewById(R.id.salas_criadas);
        criarSala = (FrameLayout) getActivity().findViewById(R.id.criar_sala);
    }

    private void setCriarSala(){
        criarSala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GeneralFunctions().abreTela(getActivity().getSupportFragmentManager(),new CriarSala(),R.id.settela);
            }
        });
    }

    private void setSalasCriadas(){
        salasCriadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            new GeneralFunctions().abreTela(getActivity().getSupportFragmentManager(),new SalasCriadas(),R.id.settela);
            }
        });
    }

}
