package com.projetobeta.quizmusical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projetobeta.quizmusical.bd.Repository;
import com.projetobeta.quizmusical.bd.Usuario;
import com.projetobeta.quizmusical.generalfunctions.Fullscreen;

import java.util.Calendar;

public class Registro extends AppCompatActivity {
    private EditText nome;
    private EditText email;
    private EditText dt_Nascimento;
    private EditText senha;
    private EditText confirmar_Senha;
    private Button cadastrar;
    private Usuario usuario;
    private AlertDialog dialog;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Fullscreen(this);
        setContentView(R.layout.activity_registro);
        inciar_Objetos();
        setCadastrar();
        calendar();
    }

    private void inciar_Objetos(){
        nome = (EditText) findViewById(R.id.nome);
        email = (EditText) findViewById(R.id.email);
        dt_Nascimento = (EditText) findViewById(R.id.dt_nascimento);
        senha = (EditText) findViewById(R.id.senha);
        confirmar_Senha = (EditText) findViewById(R.id.confirmar_senha);
        cadastrar = (Button) findViewById(R.id.cadastrar);
        usuario = new Usuario();
    }

    private void setCadastrar(){
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Registro.this);
                LayoutInflater layoutInflater = getLayoutInflater();
                alert.setView(layoutInflater.inflate(R.layout.activity_main,null));
                alert.setCancelable(true);
                dialog = alert.create();
                dialog.show();
                verificar();
            }
        });
    }

    private void verificar(){
        if(nome.getText().length() == 0){
            nome.setError("Este campo precisa ser preenchido");
            nome.requestFocus();
            dialog.dismiss();
        }else if(email.getText().length() == 0){
            email.setError("Este campo precisa ser preenchido");
            email.requestFocus();
            dialog.dismiss();
        }else if(dt_Nascimento.getText().length() == 0){
            dt_Nascimento.setError("Este campo precisa ser preenchido");
            dt_Nascimento.requestFocus();
            dialog.dismiss();
        }else if(senha.getText().length() == 0){
            senha.setError("Este campo precisa ser preenchido");
            senha.requestFocus();
            dialog.dismiss();
        }else if(!confirmar_Senha.getText().toString().equals(senha.getText().toString())){
            confirmar_Senha.setError("Os campos precisam coincidir");
            confirmar_Senha.requestFocus();
            senha.requestFocus();
            dialog.dismiss();
        }else verifica_Email();

    }

    private void salvar(){
        carregarUsuario();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        final String id =  databaseReference.push().getKey();
        databaseReference.child(id).setValue(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                usuario.setId(id);
                new Repository(Registro.this).criar_Usuario();
                new Repository(Registro.this).salvar_Usuario(usuario);
                startActivity(new Intent(Registro.this,Funcionalidades.class));
               finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
                builder.setTitle("Algo deu errado");
                builder.setMessage(e.getMessage());
                builder.setNeutralButton("Ok!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void carregarUsuario(){
        usuario.setNome(nome.getText().toString());
        usuario.setEmail(email.getText().toString());
        usuario.setDt_nascimento(dt_Nascimento.getText().toString());
        usuario.setSenha(senha.getText().toString());
    }

    private void calendar(){
        Calendar dt = Calendar.getInstance();
        int ano = dt.get(Calendar.YEAR);
        int mes = dt.get(Calendar.MONTH);
        int dia = dt.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dt_Nascimento.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        },ano,mes,dia);

        dt_Nascimento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        nome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                datePickerDialog.dismiss();
            }
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                datePickerDialog.dismiss();
            }
        });
        senha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                datePickerDialog.dismiss();
            }
        });
        confirmar_Senha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                datePickerDialog.dismiss();
            }
        });
    }

    private void verifica_Email(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean existe = false;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    existe = email.getText().toString().equalsIgnoreCase(ds.child("email").getValue().toString());
                }
                if(!existe) salvar();
                else{
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
                    builder.setTitle("Email já Cadastrado!");
                    builder.setMessage("Este email já consta na nossa base de dados!");
                    builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog = builder.create();
                    dialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}