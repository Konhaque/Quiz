package com.projetobeta.quizmusical.funcionalidades.multiplayer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.projetobeta.quizmusical.bd.Repository;
import com.projetobeta.quizmusical.generalfunctions.GeneralFunctions;

public class SalasCriadas extends Fragment {

    private LinearLayout salas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.salas_criadas,container,false);
    }

    @Override
    public void onStart() {
        iniciarObjetos();
        setSalas();
        super.onStart();
    }

    private void iniciarObjetos(){
        salas = (LinearLayout) getActivity().findViewById(R.id.salas);
    }

    private void setSalas(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Salas");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    salas(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void salas(final DataSnapshot ss){
        FrameLayout frameLayout = new FrameLayout(getContext());
        TextView textView = new TextView(getContext());
        frameLayout.setBackground(getActivity().getDrawable(R.drawable.container));
        frameLayout.setPadding(20,10,20,20);
        textView.setText(ss.child("nomeSala").getValue().toString());
        textView.setTextColor(Color.BLACK);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Salas/"+ss.getKey());
              databaseReference.child("idUser2").setValue(new Repository(getContext()).getIdUser()).addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                      Fragment fragment = new Lobby();
                      Bundle bundle = new Bundle();
                      bundle.putString("id_Sala",ss.getKey());
                      fragment.setArguments(bundle);
                      new GeneralFunctions().abreTela(getActivity().getSupportFragmentManager(),fragment,R.id.settela);
                  }
              });
            }
        });
        frameLayout.addView(textView);
        salas.addView(frameLayout);
    }

}
