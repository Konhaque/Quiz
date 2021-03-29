package com.projetobeta.quizmusical.funcionalidades.multiplayer;

import android.os.Bundle;
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
import com.google.firebase.database.ValueEventListener;
import com.projetobeta.quizmusical.R;
import com.projetobeta.quizmusical.bd.Musicas;
import com.projetobeta.quizmusical.bd.Repository;
import com.projetobeta.quizmusical.funcionalidades.Jogar;
import com.projetobeta.quizmusical.generalfunctions.GeneralFunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Lobby extends Fragment {
    private TextView user1;
    private TextView user2;
    private TextView nomeSala;
    private Button jogar;
    private Bundle bundle;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lobby,container,false);
    }

    @Override
    public void onStart() {
        inciarObjetos();
        setUsers();
        setJogar();
        verificaInicioJogo();
        super.onStart();
    }

    private void inciarObjetos(){
        nomeSala = (TextView) getActivity().findViewById(R.id.nome_sala);
        user1 = (TextView) getActivity().findViewById(R.id.jogador1);
        user2 = (TextView) getActivity().findViewById(R.id.jogador2);
        jogar = (Button) getActivity().findViewById(R.id.jogar);
        bundle = getArguments();
        databaseReference = FirebaseDatabase.getInstance().getReference("Salas/"+bundle.getString("id_Sala"));
    }

    private void setUsers(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               nomeSala.setText(snapshot.child("nomeSala").getValue().toString());
               user1.setText(snapshot.child("idUser1").getValue().toString());
               user2.setText(snapshot.child("idUser2").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setJogar(){
        jogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user1.getText().toString().equals(new Repository(getContext()).getIdUser()))
                databaseReference.child("jogador1ok").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        jogar.setText("Pronto!");
                        jogar.setEnabled(false);
                    }
                });
                else databaseReference.child("jogador2ok").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        jogar.setText("Pronto!");
                        jogar.setEnabled(false);
                    }
                });
            }
        });
    }

    private void verificaInicioJogo(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("jogador1ok").getValue() != null){
                    if(snapshot.child("jogador2ok").getValue() != null){
                            Fragment fragment = new Jogar();
                            Bundle args = new Bundle();
                            args.putString("id",bundle.getString("id_Sala"));
                            fragment.setArguments(args);
                            new GeneralFunctions().abreTela(getActivity().getSupportFragmentManager(),fragment,R.id.settela);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }







}
