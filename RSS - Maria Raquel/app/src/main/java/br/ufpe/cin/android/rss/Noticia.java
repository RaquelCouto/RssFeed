package br.ufpe.cin.android.rss;

/*
* Nesta classe, o objeto notícia, utilizado pelo banco de dados, é criado
*/

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Entity(tableName = "noticias")
public class Noticia {
    @PrimaryKey @NonNull //a chave primária é o link da notícia
    String link;
    String titulo;
    String descricao;

    /*
    * Não foi possível passar uma lista para o construtor de notícias no momento de inserção ao banco de dados
    * Portanto foi necessário converter o tipo de categorias para string utilizando um gson com TypeConvertrs
    */
    @TypeConverters(Converters.class)
    List<String> categorias;
    String data;
    String image;

    /*
    * O toString foi utilizado apenas para fins de testes no momento da implementação do requisito 5 do trabalho
    */

    @Override
    public String toString() {
        return "Noticia{" +
                "link='" + link + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", categorias=" + categorias +
                ", data='" + data + '\'' +
                '}';
    }

    /*
    * O construtor de Notícias
    * Uma alteração foi realizada no construtor, no caso a String img foi adicionada com a intenção de salvar o link da imagem da notícia
    * no banco de dados
    */

    public Noticia(@NonNull String link, String titulo, String descricao, List<String> categorias, String data, String image) {
        this.link = link;
        this.titulo = titulo;
        this.descricao = descricao;
        this.categorias = categorias;
        this.data = data;
        this.image = image;

    }


    /*
    * Função implementada a partir de um exemplo do github para fazer a conversão do tipo List para string
    */


    public static class Converters {

        static Gson gson = new Gson();

        @TypeConverter
        public static List<String> stringsToSomeObjectList (String categ){
            if(categ == null){
                return Collections.emptyList();
            }

            Type listType = new TypeToken<List<String>>() {}.getType();

            return gson.fromJson(categ, listType);
        }

        @TypeConverter
        public static String someObjectListToString(List<String> strings){
            return gson.toJson(strings);
        }

    }


}
