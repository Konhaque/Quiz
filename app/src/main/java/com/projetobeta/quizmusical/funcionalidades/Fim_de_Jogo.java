package com.projetobeta.quizmusical.funcionalidades;

import android.app.AlertDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private AlertDialog dialog;
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
        setDialog();
        getFim();
        //setLbl_pontos();
        //setRaking();
        //setJogar_Novamente();
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

    private void setDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_main,null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }

    private void getFim(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Salas/"+args.getString("id"));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("jogador1fim").getValue() != null){
                    if(snapshot.child("jogador2fim").getValue() != null){
                        if(snapshot.child("jogador1fim").getValue().toString().equals("true")
                                && snapshot.child("jogador2fim").getValue().toString().equals("true")){
                            if(snapshot.child("idUser1").getValue().toString().equals(new Repository(getContext()).getIdUser()))
                                getUsuarios(snapshot,snapshot.child("idUser2").getValue().toString(),2);
                            else getUsuarios(snapshot,snapshot.child("idUser1").getValue().toString(),1);
                            //setFinal(snapshot);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setFinal(DataSnapshot snapshot, int id, String user){
        if(snapshot.child("pontosJogador1").getValue() != null && snapshot.child("pontosJogador2").getValue() != null) {
            int pontosJogador1 = Integer.parseInt(snapshot.child("pontosJogador1").getValue().toString());
            int pontosJogador2 = Integer.parseInt(snapshot.child("pontosJogador2").getValue().toString());
            if (pontosJogador1 > pontosJogador2){
                if(id == 2){
                    lbl_pontos.setText("Parabéns "+new Repository(getContext()).getUsuario().get(0).getNome()+" Você ganhou! Você fez "
                            + pontosJogador1 + " pontos. Enquanto seu adversário "+ user+" fez "+pontosJogador2+" pontos");
                    dialog.dismiss();
                }else{
                    lbl_pontos.setText("Parabéns "+user+" Você ganhou! Você fez "
                            + pontosJogador1 + " pontos. Enquanto seu adversário "+ new Repository(getContext()).getUsuario().get(0).getNome()+" fez "+pontosJogador2+" pontos");
                    dialog.dismiss();
                }
            }else{
                if(id == 2){
                    lbl_pontos.setText("Parabéns "+user+" Você ganhou! Você fez "
                            + pontosJogador2 + " pontos. Enquanto seu adversário "+ new Repository(getContext()).getUsuario().get(0).getNome()+" fez "+pontosJogador1+" pontos");
                    dialog.dismiss();
                }else{
                    lbl_pontos.setText("Parabéns "+new Repository(getContext()).getUsuario().get(0).getNome()+" Você ganhou! Você fez "
                            + pontosJogador2 + " pontos. Enquanto seu adversário "+ user+" fez "+pontosJogador1+" pontos");
                    dialog.dismiss();
                }
            }

        }
    }

    private void getUsuarios(final DataSnapshot ss ,String id, final int value){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Usuarios/"+id);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setFinal(ss,value,snapshot.child("nome").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
