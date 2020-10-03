package br.ufpe.cin.android.rss;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.prof.rssparser.Article;
import com.squareup.picasso.Picasso;
import java.util.List;

/*
* O RssAdapter foi criado para satisfazer o requisito 1 do trabalho: Fazer com que o app deixasse de utilizar uma listview
* e passasse a utilizar uma recyclerView
*/


public class RssAdapter extends RecyclerView.Adapter {

    private List<Article> noticias;
    private Context context;


    public RssAdapter(Context context, List<Article> noticias) {
        this.context = context;
        this.noticias = noticias;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // cria uma view com o layout xml linha e passa para o holder
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.linha, parent, false);
        return new ItemRssHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        /*
        * Atrela os elementos da RecyclerView com os dados da lista de Artigos
        * Algumas imagens não são carregadas por conta da notícia não ter imagem
        */

        Article article = noticias.get(position);
        TextView titulo = holder.itemView.findViewById(R.id.titulo);
        titulo.setText(article.getTitle());
        TextView data = holder.itemView.findViewById(R.id.dataPublicacao);
        data.setText(article.getPubDate());
        Log.i("imagens", "links imagens: " + article.getImage());
        ImageView img = holder.itemView.findViewById(R.id.imagem);
        Picasso.get().load(article.getImage()).into(img);


        // Aqui é a ação de clique, ao clicar em uma notícia o usuário é redirecionado para a página web da notícia
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getLink()));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return noticias.size();
    }

    /*
    * Holder do recyclerview
    */

    static class ItemRssHolder extends RecyclerView.ViewHolder {

        public ItemRssHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
