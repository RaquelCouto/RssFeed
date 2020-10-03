package br.ufpe.cin.android.rss;

import android.content.Context;


/*
* Todo o código implemntado aqui foi baseado na aula de ROOM
*/

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities =  {Noticia.class}, version = 1)

public abstract class NoticiaDB extends RoomDatabase {

    private static final String DB_NAME = "noticia.db";
    abstract NoticiaDAO obterDAO();

    private static volatile NoticiaDB INSTANCE;
    synchronized static NoticiaDB getINSTANCE(Context c){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(c,NoticiaDB.class, DB_NAME).allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
