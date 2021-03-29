package com.projetobeta.quizmusical.funcionalidades.multiplayer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
import com.projetobeta.quizmusical.generalfunctions.GeneralFunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CriarSala extends Fragment {
    private EditText nomeSala;
    private EditText senhaSala;
    private Spinner modoJogo;
    private Button criarSala;
    private String id;
    private List<Musicas> musicasEscolhidas;
    private AlertDialog dialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.criar_sala,container,false);
    }

    @Override
    public void onStart() {
        iniciarObjetos();
        setModoJogo();
        setCriarSala();
        super.onStart();
    }

    private void iniciarObjetos(){
        nomeSala = (EditText) getActivity().findViewById(R.id.nome);
        senhaSala = (EditText) getActivity().findViewById(R.id.senha);
        modoJogo = (Spinner) getActivity().findViewById(R.id.spineer);
        criarSala = (Button) getActivity().findViewById(R.id.criarSala);
        musicasEscolhidas = new ArrayList<>();
    }

    private void setModoJogo(){
        List<String> opcoes = new ArrayList<>();
        opcoes.add("Nacionais");
        opcoes.add("Internacionais");
        ArrayAdapter<String> modos = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,opcoes);
        modos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modoJogo.setAdapter(modos);
    }

    private void criarSala(){
        com.projetobeta.quizmusical.bd.CriarSala criarSala = new com.projetobeta.quizmusical.bd.CriarSala();
        criarSala.setNomeSala(nomeSala.getText().toString());
        criarSala.setSenha(senhaSala.getText().toString());
        criarSala.setModoDeJogo(modoJogo.getSelectedItem().toString());
        criarSala.setIdUser1(new Repository(getContext()).getIdUser());
        criarSala.setIdUser2("");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Salas");
        id = databaseReference.push().getKey();
        databaseReference.child(id).setValue(criarSala).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                escolheMusicas();

            }
        });
    }

    private void setCriarSala(){
        criarSala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.activity_main,null));
                builder.setCancelable(true);
                dialog = builder.create();
                dialog.show();
                criarSala();
            }
        });
    }

    private void escolheMusicas(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Musicas/"+modoJogo.getSelectedItem().toString());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                new Repository(getContext()).criar_Musicas();
                Musicas musicas = new Musicas();
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
                musicasPartida();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void musicasPartida(){
        List<Musicas> musicas = new Repository(getContext()).getMusicas();
        Random random = new Random();
        int musica = random.nextInt(musicas.size());
        if(!musicasEscolhidas.contains(musicas.get(musica).getCorrect()) && musicasEscolhidas.size() < 5){
            musicasEscolhidas.add(musicas.get(musica));
            musicasPartida();
        }else if(musicasEscolhidas.size() < 5) musicasPartida();
        else salvarMusicas(musicasEscolhidas);
    }

    private void salvarMusicas(List<Musicas> musicas){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Salas/"+id);
        databaseReference.child("Musicas").setValue(musicas).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.dismiss();
                Fragment fragment = new Lobby();
                Bundle bundle = new Bundle();
                bundle.putString("id_Sala",id);
                fragment.setArguments(bundle);
                new GeneralFunctions().abreTela(getActivity().getSupportFragmentManager(),fragment,R.id.settela);
            }
        });
    }




}
