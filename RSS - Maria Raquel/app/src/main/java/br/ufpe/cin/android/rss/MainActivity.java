package br.ufpe.cin.android.rss;

/*
 * A MainActivity é a tela inicial do aplicativo, nela estão implementados:
 *  A chamada do service que faz o download e processamento do xml e banco de dados.
 *  A recyclerView que lista as notícias a partir do banco de dados
 */



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.prof.rssparser.Article;
import java.util.List;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity {
    private String RSS_FEED;
    private static String rssfeed;
    private SharedPreferences prefs;

    RecyclerView conteudoRSS;
    List<Article> noticias;
    Article artigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conteudoRSS = findViewById(R.id.conteudoRSS);

        prefs = getDefaultSharedPreferences(this);
        RSS_FEED = prefs.getString(rssfeed,getResources().getString(R.string.feed_padrao));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_preferences, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.add_preferences){
            Intent preferences = new Intent(this, PreferenciasActivity.class);
            startActivity(preferences);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*
        * Intente explícito para fazer a chamada do service na Main Activity
        */

        Intent service = new Intent(this, RssService.class);
        startService(service);

        /*
        * Operações com o Banco de dados geralmente são muito custosas, portanto o uso de uma nova Thread é feito para
        * que o app tenha um melhor desempenho;
        */

        new Thread(

                () -> {

                    /*
                    * Instância o banco de dados para ser utilizado na runOnUiThread
                    * A runOnUiThread contém o código do Adapter da RecyclerView que usa o banco de dados como fonte
                    */

                    NoticiaDB db = NoticiaDB.getINSTANCE(MainActivity.this);
                    NoticiaDAO dao = db.obterDAO();
                    List<Noticia> notes = dao.todasNoticias();

                    runOnUiThread(
                            () -> {
                                conteudoRSS.setAdapter(new DataBaseAdapter(getApplicationContext(), notes));
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                conteudoRSS.setLayoutManager(layoutManager);
                            }
                    );

                }

        ).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Código do service chamado dentro de onResume para atualizar o feed
        Intent service = new Intent(this, RssService.class);
        startService(service);
    }

    /*
    * O método AtualizaFeed foi criado para satisfazer o requisito 3 do trabalho, a criação do método facilitou
    * a sua chamada no método onResume e no código de download e processamento do xml (anteriormente implementados na MainActivity)
    * Quando chamado em onResume, imediatamente a activity era atualizada com as notícias do novo link. Porém, após a utilização do BD
    * essa função passou a não ser tão importante, visto que a recyclerView lista todas as notícias do banco de dados e dessa forma as novas notícias
    * passam a serem listadas ao final do feed
    * A variável RSS_FEED é atualizada com o valor salvo pelo usuário nas preferences
    */

    private String AtualizaFeed (){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String teste = preferences.getString("linkxml",getResources().getString(R.string.feed_padrao));
        preferences.edit().putString(rssfeed,teste).apply();
        return RSS_FEED = teste;
    }

}