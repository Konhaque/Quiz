package com.projetobeta.quizmusical.funcionalidades;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.core.Repo;
import com.projetobeta.quizmusical.R;
import com.projetobeta.quizmusical.bd.Repository;
import com.projetobeta.quizmusical.bd.Usuario;

import java.util.List;

public class Fim_de_Jogo extends Fragment {
    private Button raking;
    private Button jogar_Novamente;
    private TextView lbl_pontos;
    private Bundle args;
    private BottomNavigationView menu;
    private List<Usuario> usuario;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fim_de_jogo,container,false);
        return viewGroup;
    }

    @Override
    public void onStart() {
        iniciar_Objetos();
        setLbl_pontos();
        setRaking();
        setJogar_Novamente();
        super.onStart();
    }

    private void iniciar_Objetos(){
        raking = (Button) getActivity().findViewById(R.id.ir_a_ranking);
        jogar_Novamente = (Button) getActivity().findViewById(R.id.jogar_novamente);
        lbl_pontos = (TextView) getActivity().findViewById(R.id.lbl_final);
        args = getArguments();
        menu = (BottomNavigationView) getActivity().findViewById(R.id.nav_view);
        usuario = new Repository(getContext()).getUsuario();
    }

    private void setLbl_pontos(){
        lbl_pontos.setText("Parabéns "+usuario.get(0).getNome()+"! Você fez "+args.getString("pontos")+" pontos");
    }

    private void setRaking(){
        raking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.setSelectedItemId(R.id.ranking);
            }
        });
    }

    private void setJogar_Novamente(){
        jogar_Novamente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.setSelectedItemId(R.id.jogar);
            }
        });
    }


}
