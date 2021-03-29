package com.projetobeta.quizmusical.funcionalidades;

import android.app.AlertDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.projetobeta.quizmusical.R;
import com.projetobeta.quizmusical.bd.Musicas;
import com.projetobeta.quizmusical.bd.Repository;
import com.projetobeta.quizmusical.generalfunctions.GeneralFunctions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jogar extends Fragment {

    private int pontuacao;
    private TextView pontos;
    private TextView lbl_jogo;
    private Button op_1;
    private Button op_2;
    private Button op_3;
    private Button op_4;
    private Bundle args;
    private Musicas musicas;
    private MediaPlayer mediaPlayer;
    private AlertDialog dialog;
    private List<Musicas> musicas_jogadas;
    private Random random;
    private int musica;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.jogar,container,false);
        return viewGroup;
    }

    @Override
    public void onStart() {
        iniciar_Objetos();
        //setLbl_jogo();
        setPontos();
        pegar_Musica();
        opcoes();
        super.onStart();
    }

    private void iniciar_Objetos(){
        pontuacao = 0;
        pontos = (TextView) getActivity().findViewById(R.id.pontos);
        lbl_jogo = (TextView) getActivity().findViewById(R.id.lbl_game);
        op_1 = (Button) getActivity().findViewById(R.id.op1);
        op_2 = (Button) getActivity().findViewById(R.id.op2);
        op_3 = (Button) getActivity().findViewById(R.id.op3);
        op_4 = (Button) getActivity().findViewById(R.id.op4);
        args = getArguments();
        new Repository(getContext()).criar_Musicas();
        musicas = new Musicas();
        musica = 0;
        musicas_jogadas = new ArrayList<>();
        random = new Random();
    }

    /*private void setLbl_jogo(){
        if(args.getString("tipo").equalsIgnoreCase("games")) lbl_jogo.setText("De que game é esta música?");
    }*/

    private void setPontos(){
        pontos.setText("Pontos: "+pontuacao);
    }

    private void pegar_Musica(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_main,null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Salas/"+args.getString("id"));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<List<Musicas>> genericTypeIndicator = new GenericTypeIndicator<List<Musicas>>() {
                };
                musicas_jogadas = snapshot.child("Musicas").getValue(genericTypeIndicator);
                setMusica();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*private void pegar_Musica(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_main,null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
        DatabaseReference bd = FirebaseDatabase.getInstance().getReference("Musicas/Games");
        bd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                        musicas = new Musicas();
                        musicas.setUrl_musica(ds.child("url_musica").getValue().toString());
                        musicas.setOp_1(ds.child("op_1").getValue().toString());
                        musicas.setOp_2(ds.child("op_2").getValue().toString());
                        musicas.setOp_3(ds.child("op_3").getValue().toString());
                        musicas.setOp_4(ds.child("op_4").getValue().toString());
                        musicas.setScore(ds.child("score").getValue().toString());
                        musicas.setCorrect(ds.child("correct").getValue().toString());
                        new Repository(getContext()).salvar_Musicas(musicas);
                }
                setMusica();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }*/

    private void setMusica(){
        //musicas_jogadas = new Repository(getContext()).getMusicas();
        //musica = random.nextInt(musicas_jogadas.size());
        op_1.setText(musicas_jogadas.get(musica).getOp_1());
        op_2.setText(musicas_jogadas.get(musica).getOp_2());
        op_3.setText(musicas_jogadas.get(musica).getOp_3());
        op_4.setText(musicas_jogadas.get(musica).getOp_4());
        dialog.dismiss();
        try {
            Uri uri = Uri.parse(musicas_jogadas.get(musica).getUrl_musica());
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(getContext(),uri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void opcoes(){
        op_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                if(op_1.getText().toString().equalsIgnoreCase(musicas_jogadas.get(musica).getCorrect())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.certo,null));
                    builder.setCancelable(true);
                    dialog = builder.create();
                    dialog.show();
                    pontuacao = pontuacao + Integer.parseInt(musicas_jogadas.get(musica).getScore());
                    pontos.setText("Pontos: "+pontuacao);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if((musica+1) < musicas_jogadas.size()){
                                musica++;
                                dialog.dismiss();
                                setMusica();
                            }else{
                                setFim();
                                dialog.dismiss();
                            }
                        }
                    },2000);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.errado,null));
                    builder.setCancelable(true);
                    dialog = builder.create();
                    dialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if((musica+1) < musicas_jogadas.size()){
                                musica++;
                                dialog.dismiss();
                                setMusica();
                            }else{
                               setFim();
                                dialog.dismiss();
                            }

                        }
                    },2000);
                }
            }
        });

        op_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                if(op_2.getText().toString().equalsIgnoreCase(musicas_jogadas.get(musica).getCorrect())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.certo,null));
                    builder.setCancelable(true);
                    dialog = builder.create();
                    dialog.show();
                    pontuacao = pontuacao + Integer.parseInt(musicas_jogadas.get(musica).getScore());
                    pontos.setText("Pontos: "+pontuacao);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if((musica+1) < musicas_jogadas.size()){
                                musica++;
                                dialog.dismiss();
                                setMusica();
                            }else{
                               setFim();
                                dialog.dismiss();
                            }
                        }
                    },2000);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.errado,null));
                    builder.setCancelable(true);
                    dialog = builder.create();
                    dialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if((musica+1) < musicas_jogadas.size()){
                                musica++;
                                dialog.dismiss();
                                setMusica();
                            }else{
                                setFim();
                                dialog.dismiss();
                            }
                        }
                    },2000);
                }
            }
        });

        op_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                if(op_3.getText().toString().equalsIgnoreCase(musicas_jogadas.get(musica).getCorrect())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.certo,null));
                    builder.setCancelable(true);
                    dialog = builder.create();
                    dialog.show();
                    pontuacao = pontuacao + Integer.parseInt(musicas_jogadas.get(musica).getScore());
                    pontos.setText("Pontos: "+pontuacao);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if((musica+1) < musicas_jogadas.size()){
                                musica++;
                                dialog.dismiss();
                                setMusica();
                            }else{
                               setFim();
                                dialog.dismiss();
                            }
                        }
                    },2000);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.errado,null));
                    builder.setCancelable(true);
                    dialog = builder.create();
                    dialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if((musica+1) < musicas_jogadas.size()){
                                musica++;
                                dialog.dismiss();
                                setMusica();
                            }else{
                                setFim();
                                dialog.dismiss();
                            }
                        }
                    },2000);
                }
            }
        });

        op_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                if(op_4.getText().toString().equalsIgnoreCase(musicas_jogadas.get(musica).getCorrect())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.certo,null));
                    builder.setCancelable(true);
                    dialog = builder.create();
                    dialog.show();
                    pontuacao = pontuacao + Integer.parseInt(musicas_jogadas.get(musica).getScore());
                    pontos.setText("Pontos: "+pontuacao);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if((musica+1) < musicas_jogadas.size()){
                                musica++;
                                dialog.dismiss();
                                setMusica();
                            }else{
                                setFim();
                                dialog.dismiss();
                            }
                        }
                    },2000);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.errado,null));
                    builder.setCancelable(true);
                    dialog = builder.create();
                    dialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if((musica+1) < musicas_jogadas.size()){
                                musica++;
                                dialog.dismiss();
                                setMusica();
                            }else{
                                setFim();
                                dialog.dismiss();
                            }
                        }
                    },2000);
                }
            }
        });
    }

    private void setFim(){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Salas/"+args.getString("id"));
        databaseReference.child("jogador1ok").setValue(null);
        databaseReference.child("jogador2ok").setValue(null);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("idUser1").getValue().toString().equals(new Repository(getContext()).getIdUser())){
                    databaseReference.child("jogador1fim").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            databaseReference.child("pontosJogador1").setValue(pontuacao).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Fragment fragment = new Fim_de_Jogo();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id",args.getString("id"));
                                    fragment.setArguments(bundle);
                                    new GeneralFunctions().abreTela(getActivity().getSupportFragmentManager(),fragment,R.id.settela);
                                }
                            });
                        }
                    });
                }else {
                    databaseReference.child("jogador2fim").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            databaseReference.child("pontosJogador2").setValue(pontuacao).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Fragment fragment = new Fim_de_Jogo();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id",args.getString("id"));
                                    fragment.setArguments(bundle);
                                    new GeneralFunctions().abreTela(getActivity().getSupportFragmentManager(),fragment,R.id.settela);
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void abre_Tela(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                android.R.anim.fade_in,android.R.anim.fade_out)
                .replace(R.id.settela,fragment).commit();
    }
}
