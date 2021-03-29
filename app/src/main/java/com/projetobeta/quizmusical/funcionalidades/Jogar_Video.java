package com.projetobeta.quizmusical.funcionalidades;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.projetobeta.quizmusical.R;
import com.projetobeta.quizmusical.bd.Musicas;

import java.util.List;
import java.util.Random;

public class Jogar_Video extends Fragment {
    private VideoView videoView;
    private TextView pontos;
    private Button op_1;
    private Button op_2;
    private Button op_3;
    private Button op_4;
    private Musicas musicas;
    private AlertDialog dialog;
    private List<Musicas> musicas_jogadas;
    private Random random;
    private int musica;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.jogar_video,container,false);
        return viewGroup;
    }

    @Override
    public void onStart() {
        iniciarObjetos();
        setVideoView();
        super.onStart();
    }

    private void iniciarObjetos(){
        videoView = (VideoView) getActivity().findViewById(R.id.player_video);
        pontos = (TextView) getActivity().findViewById(R.id.pontosV);
        op_1 = (Button) getActivity().findViewById(R.id.op1V);
        op_2 = (Button) getActivity().findViewById(R.id.op2V);
        op_3 = (Button) getActivity().findViewById(R.id.op3V);
        op_4 = (Button) getActivity().findViewById(R.id.op4V);
        musicas = new Musicas();
        random = new Random();
    }

    private void setVideoView(){
        videoView.setVideoURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/quiz-musical-a6251.appspot.com/o/Cenas_Filmes%2FToothless%20and%20his%20Kids%20visit%20New%20Berk%20Scene%20-%20HOW%20TO%20TRAIN%20YOUR%20DRAGON-%20Homecoming%20(2019).mp4?alt=media&token=f1d0d122-f756-4389-a09c-cf4c7008f975"));
        videoView.start();
    }
}
