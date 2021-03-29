package com.projetobeta.quizmusical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.projetobeta.quizmusical.funcionalidades.Escolhe_Jogo;
import com.projetobeta.quizmusical.funcionalidades.ModoDeJogo;
import com.projetobeta.quizmusical.funcionalidades.Ranking;
import com.projetobeta.quizmusical.generalfunctions.Fullscreen;

public class Funcionalidades extends AppCompatActivity {
    private BottomNavigationView menu;
    private BottomNavigationView.OnNavigationItemSelectedListener itemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.jogar:
                abre_Tela(new ModoDeJogo());
                return true;
                case R.id.ranking:
                 abre_Tela(new Ranking());
                 return true;
                default:return false;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Fullscreen(this);
        setContentView(R.layout.activity_funcionalidades);
        iniciar_Objetos();
    }

    private void iniciar_Objetos(){
        menu = (BottomNavigationView) findViewById(R.id.nav_view);
        menu.setOnNavigationItemSelectedListener(itemSelectedListener);
        menu.setSelectedItemId(R.id.jogar);
    }

    private void abre_Tela(Fragment fragment){
        getSupportFragmentManager().beginTransaction().setCustomAnimations(
                android.R.anim.fade_in,android.R.anim.fade_out)
                .replace(R.id.settela,fragment).commit();
    }

}