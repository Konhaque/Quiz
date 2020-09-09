package com.projetobeta.quizmusical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.projetobeta.quizmusical.generalfunctions.Fullscreen;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer musica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Fullscreen(this);
        setContentView(R.layout.activity_main);
        run();
        //setMusica();
    }

    private void run(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,Login.class));
                finish();
            }
        },3000);
    }

    /*private void setMusica(){
        try {
            Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/quiz-musical-a6251.appspot.com/o/Detonautas%20-%20O%20Amanh%C3%A3.mp3?alt=media&token=0e4d5c70-be5a-475a-a65d-99f3f732a9f9");
            musica = new MediaPlayer();
            musica.setAudioStreamType(AudioManager.STREAM_MUSIC);
            musica.setDataSource(this,uri);
            musica.prepare();
            musica.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
}