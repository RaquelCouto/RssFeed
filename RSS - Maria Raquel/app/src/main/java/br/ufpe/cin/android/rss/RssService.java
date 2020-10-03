package br.ufpe.cin.android.rss;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.prof.rssparser.Article;
import com.prof.rssparser.Channel;
import com.prof.rssparser.OnTaskCompleted;
import com.prof.rssparser.Parser;

import java.util.List;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

/*
* O service contém o código de download e processamento do xml e criação do banco de dados
*/

public class RssService extends Service {

    public List<Article> noticias;
    private SharedPreferences prefs;
    private String RSS_FEED;
    private static String rssfeed;

    @Override
    public void onCreate() {
        super.onCreate();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /*
        * Código anteriormente implementado pela mainActivity
        * A seguir o código de dowload e processamento do xml
        */

        prefs = getDefaultSharedPreferences(this);
        RSS_FEED = prefs.getString(rssfeed,getResources().getString(R.string.feed_padrao));

        String rss_feed = intent.getDataString();

        Parser p = new Parser.Builder().build();
        p.onFinish(
                new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Channel channel) {
                        //aqui é onde ele pega os elementos
                        noticias = channel.getArticles();

                        /*
                        * Uma nova thread é criada para realizar as inserções das notícias no banco de dados
                        * Como operações que envolvem bd são custosas é indicado realiza-las em uma nova Thread
                        */

                        new Thread(
                                () ->{

                                    /*
                                    * Criação da instancia do banco de dados
                                    */

                                    NoticiaDB db = NoticiaDB.getINSTANCE(getApplicationContext());
                                    NoticiaDAO dao = db.obterDAO();

                                    /*
                                    * Este for foi implementado para pegar as posicões de cada notícia na lista de noticias
                                    * e assim poder acessar os atributos: título, link, descrição e etc
                                    * Logo, o for é feito do índice 0 até o tamanho da lista de notícias
                                    * Ao final de cada iteração, uma nova notícia é inserida no banco de dados
                                    */
                                    for (int i = 0; i < noticias.size(); i++){

                                        Article artigo = noticias.get(i);
                                        String link = artigo.getLink();
                                        String title = artigo.getTitle();
                                        String description = artigo.getDescription();
                                        List<String> categ = artigo.getCategories();
                                        String date = artigo.getPubDate();
                                        String image = artigo.getImage();

                                        Noticia noticia = new Noticia(link, title, description, categ, date, image);
                                        dao.inserir(noticia);
                                    }

                                }
                        ).start();



                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("RSS_APP",e.getMessage());
                    }
                }
        );
        p.execute(AtualizaFeed());



        return super.onStartCommand(intent, flags, startId);
    }

    /*
    * Método criado para facilitar a atualização do valor de RSS_FEED.
    */

    private String AtualizaFeed (){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String teste = preferences.getString("linkxml",getResources().getString(R.string.feed_padrao));
        preferences.edit().putString(rssfeed,teste).apply();
        return RSS_FEED = teste;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
