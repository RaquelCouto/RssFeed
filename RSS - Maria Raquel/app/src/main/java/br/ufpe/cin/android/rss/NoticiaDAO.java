package br.ufpe.cin.android.rss;

/*
* DAO de notícia, no arquivo estão listadas as consultas e inserções que são utilizadas no banco de dado
*/


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoticiaDAO {

    // O OnConflictStrategy foi utilizado porque quando a aplicação recarregava, o banco de dados tentava inserir as notícias já inseridas
    // anteriormente e gerava conflito
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserir(Noticia noticia);

    @Update
    void atualizarFeed(Noticia noticia);

    //Consulta para devolver todas as notícias
    @Query("SELECT * FROM noticias")
    List<Noticia> todasNoticias();


}
