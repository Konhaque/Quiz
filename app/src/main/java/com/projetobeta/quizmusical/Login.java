package com.projetobeta.quizmusical;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;
import com.projetobeta.quizmusical.bd.Musicas;
import com.projetobeta.quizmusical.bd.Repository;
import com.projetobeta.quizmusical.bd.Usuario;
import com.projetobeta.quizmusical.generalfunctions.Fullscreen;

public class Login extends AppCompatActivity {
    private int RC_SING_IN = 0;
    private EditText email;
    private EditText senha;
    private Button logar;
    private TextView registrar;
    private SignInButton loginGoogle;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    private ImageView logo;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Fullscreen(this);
        setContentView(R.layout.activity_login);
        inicia_objetos();
        setLoginGoogle();
        loginGoogle();
        setRegistrar();
        setLogar();
        click();
    }

    private void inicia_objetos(){
        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);
        logar = (Button) findViewById(R.id.login);
        loginGoogle = (SignInButton) findViewById(R.id.loginG);
        registrar = (TextView) findViewById(R.id.registrar);
        logo = (ImageView) findViewById(R.id.logo);
    }

    private void click(){
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upar_musica();
            }
        });
    }

    private void setLoginGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        callbackManager = CallbackManager.Factory.create();
    }

    private void singIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SING_IN);
    }

    private void loginGoogle(){
        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singIn();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SING_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            logGoogle(account);
            //Toast.makeText(this,account.getDisplayName(),Toast.LENGTH_LONG).show();
        } catch (ApiException e) {
            Toast.makeText(this,"signInResult:failed code=" + e.getStatusCode(),Toast.LENGTH_LONG).show();
        }
    }

    private void setRegistrar(){
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Registro.class));
                finish();
            }
        });
    }

    private void logGoogle(final GoogleSignInAccount account){
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        LayoutInflater layoutInflater = getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.activity_main,null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean existe = false;
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.child("dt_nascimento").getValue().toString().equalsIgnoreCase(account.getId())){
                        existe = true;
                        Usuario usuario = new Usuario();
                        usuario.setNome(ds.child("nome").getValue().toString());
                        usuario.setEmail(ds.child("email").getValue().toString());
                        usuario.setId(ds.getKey());
                        new Repository(Login.this).criar_Usuario();
                        new Repository(Login.this).salvar_Usuario(usuario);
                        startActivity(new Intent(Login.this,Funcionalidades.class));
                        finish();
                        break;
                    }
                }
                if(!existe) cadastrarContaGoogle(account);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void cadastrarContaGoogle(GoogleSignInAccount account){
        final Usuario usuario = new Usuario();
        usuario.setNome(account.getDisplayName());
        usuario.setEmail(account.getEmail());
        usuario.setDt_nascimento(account.getId());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        final String id = databaseReference.push().getKey();
        databaseReference.child(id).setValue(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                usuario.setId(id);
                new Repository(Login.this).criar_Usuario();
                new Repository(Login.this).salvar_Usuario(usuario);
                startActivity(new Intent(Login.this,Funcionalidades.class));
                finish();
            }
        });

    }


    private void setLogar(){
        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                LayoutInflater layoutInflater = getLayoutInflater();
                builder.setView(layoutInflater.inflate(R.layout.activity_main,null));
                builder.setCancelable(true);
                dialog = builder.create();
                dialog.show();
                logar();
            }
        });
    }

    private void upar_musica(){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Musicas/Games");
        Musicas musicas = new Musicas();
        musicas.setUrl_musica("url");
        musicas.setOp_1("Asura's Wrath");
        musicas.setOp_2("God of War 2");
        musicas.setOp_3("Dante's Inferno");
        musicas.setOp_4("GoldenEye 007");
        musicas.setCorrect("GoldenEye 007");
        musicas.setScore("60");
        db.child(db.push().getKey()).setValue(musicas).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Login.this,"Sucesso",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void logar(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean existe = false;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.child("email").getValue().toString().equalsIgnoreCase(email.getText().toString()) &&
                    ds.child("senha").getValue().toString().equals(senha.getText().toString())){
                        existe = true;
                        Usuario usuario = new Usuario();
                        usuario.setNome(ds.child("nome").getValue().toString());
                        usuario.setEmail(ds.child("email").getValue().toString());
                        usuario.setId(ds.getKey());
                        new Repository(Login.this).criar_Usuario();
                        new Repository(Login.this).salvar_Usuario(usuario);
                        startActivity(new Intent(Login.this,Funcionalidades.class));
                        finish();
                        break;
                    }
                }
                if(!existe){
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setTitle("Algo deu Errado!");
                    builder.setMessage("Verifique o seu email e senha e tente novamente!");
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