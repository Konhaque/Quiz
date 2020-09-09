package com.projetobeta.quizmusical.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DaoUtil extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "QUIZ.db";
    private static final int VERSAO = 1;

    public DaoUtil(@Nullable Context context){
        super(context,NOME_BANCO,null,VERSAO);
    }
    public SQLiteDatabase getConexaoDataBase(){
        return this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
