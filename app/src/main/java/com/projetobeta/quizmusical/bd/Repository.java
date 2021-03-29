package com.projetobeta.quizmusical.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private DaoUtil databaseutil;

    public Repository(@NonNull Context context){
        databaseutil = new DaoUtil(context);
    }

    public void criar_Musicas(){
        String sql = "DROP TABLE IF EXISTS TB_MUSICAS";
        databaseutil.getConexaoDataBase().execSQL(sql);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE TB_MUSICAS(");
        stringBuilder.append("URL TEXT NOT NULL,");
        stringBuilder.append("OP_1 TEXT NOT NULL,");
        stringBuilder.append("OP_2 TEXT NOT NULL,");
        stringBuilder.append("OP_3 TEXT NOT NULL,");
        stringBuilder.append("OP_4 TEXT NOT NULL,");
        stringBuilder.append("SCORE TEXT NOT NULL,");
        stringBuilder.append("CORRECT TEXT NOT NULL)");
        databaseutil.getConexaoDataBase().execSQL(stringBuilder.toString());
    }

    public void criar_Usuario(){
        String sql = "DROP TABLE IF EXISTS TB_USER";
        databaseutil.getConexaoDataBase().execSQL(sql);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE TB_USER(");
        stringBuilder.append("ID_USER TEXT NOT NULL, ");
        stringBuilder.append("NOME TEXT NOT NULL, ");
        stringBuilder.append("EMAIL TEXT NOT NULL)");
        databaseutil.getConexaoDataBase().execSQL(stringBuilder.toString());
    }

    public void salvar_Musicas(Musicas musica){
        databaseutil.getConexaoDataBase().insert("TB_MUSICAS",null,carregarMusica(musica));
    }

    public void salvar_Usuario(Usuario usuario){
        databaseutil.getConexaoDataBase().insert("TB_USER",null,carregarUsuario(usuario));
    }

    private ContentValues carregarMusica(Musicas musicas){
        ContentValues contentValues = new ContentValues();
        contentValues.put("URL",musicas.getUrl_musica());
        contentValues.put("OP_1", musicas.getOp_1());
        contentValues.put("OP_2", musicas.getOp_2());
        contentValues.put("OP_3", musicas.getOp_3());
        contentValues.put("OP_4", musicas.getOp_4());
        contentValues.put("SCORE", musicas.getScore());
        contentValues.put("CORRECT", musicas.getCorrect());
        return contentValues;
    }

    private ContentValues carregarUsuario(Usuario usuario){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID_USER", usuario.getId());
        contentValues.put("NOME", usuario.getNome());
        contentValues.put("EMAIL", usuario.getEmail());
        return contentValues;
    }

    public List<Musicas> getMusicas(){
        List<Musicas> musicas = new ArrayList<>();
        String sql = "SELECT * FROM TB_MUSICAS";
        Cursor cursor = databaseutil.getConexaoDataBase().rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            musicas.add(carregaLista(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return musicas;
    }

    public List<Usuario> getUsuario(){
        String sql = "SELECT * FROM TB_USER";
        List<Usuario> usuarios = new ArrayList<>();
        Cursor cursor = databaseutil.getConexaoDataBase().rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            usuarios.add(carregarUser(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return usuarios;
    }

    private Musicas carregaLista(Cursor cursor){
        Musicas musicas = new Musicas();
        musicas.setUrl_musica(cursor.getString(cursor.getColumnIndex("URL")));
        musicas.setOp_1(cursor.getString(cursor.getColumnIndex("OP_1")));
        musicas.setOp_2(cursor.getString(cursor.getColumnIndex("OP_2")));
        musicas.setOp_3(cursor.getString(cursor.getColumnIndex("OP_3")));
        musicas.setOp_4(cursor.getString(cursor.getColumnIndex("OP_4")));
        musicas.setCorrect(cursor.getString(cursor.getColumnIndex("CORRECT")));
        musicas.setScore(cursor.getString(cursor.getColumnIndex("SCORE")));
        return musicas;
    }

    private Usuario carregarUser(Cursor cursor){
        Usuario usuario = new Usuario();
        usuario.setId(cursor.getString(cursor.getColumnIndex("ID_USER")));
        usuario.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
        usuario.setEmail(cursor.getString(cursor.getColumnIndex("EMAIL")));
        return usuario;
    }

    public String getIdUser(){
        String sql = "SELECT * FROM TB_USER";
        Cursor cursor = databaseutil.getConexaoDataBase().rawQuery(sql,null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("ID_USER"));
    }

}
