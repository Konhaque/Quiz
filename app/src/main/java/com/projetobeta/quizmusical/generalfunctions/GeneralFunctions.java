package com.projetobeta.quizmusical.generalfunctions;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class GeneralFunctions {
    public GeneralFunctions(){

    }

    public void abreTela(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @NonNull int id){
        fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(id,fragment).commit();
    }
}
