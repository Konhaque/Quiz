package com.projetobeta.quizmusical.generalfunctions;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Fullscreen {
    public Fullscreen(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View tela = activity.getWindow().getDecorView();
        tela.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN+View.SYSTEM_UI_FLAG_HIDE_NAVIGATION+View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
