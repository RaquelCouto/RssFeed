package br.ufpe.cin.android.rss;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/*
* A classe DataBaseAdapter foi criada para implementar o requisito 7: o recyclerview utiliza o banco de dados como fonte
*/

public class DataBaseAdapter extends RecyclerView.Adapter{


    private List<Noticia> noticias; //Cria uma lista do tipo Notícias
    private Context context;

    //Construtor da classe, utilizado na MainActivity
    public DataBaseAdapter (Context context, List<Noticia> noticias){

        this.context = context;
        this.noticias = noticias;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // cria uma view com o layout linha e passa para o holder
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.linha, parent, false);
        return new ItemRssHolder(viewCriada);
    }
    /*
     * Holder do recyclerview
     */
    static class ItemRssHolder extends RecyclerView.ViewHolder{

        public ItemRssHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        /*
        * As linhas a seguir atrelam os elementos da interface com os dados a serem exibidos e setam seus valores
        * Algumas imagens não são carregadas por conta da notícia não ter imagem
        */

        TextView titulo = holder.itemView.findViewById(R.id.titulo);
        titulo.setText(noticias.get(position).titulo);

        ImageView img = holder.itemView.findViewById(R.id.imagem);
        Picasso.get().load(noticias.get(position).image).into(img);


        /*
        * Como a data estava com um formato não muito agradável. Portanto, foi necessário criar um método,
        *  que pode ser encontrado logo a baixo, para formata-la. A chamada do método encontra-se dentro do bloco try/catch
        */

        TextView data = holder.itemView.findViewById(R.id.dataPublicacao);

        try {
            String dataCorreta = formataData(noticias.get(position).data);
            data.setText(dataCorreta);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*
        * A seguir a função de clique: Ao clicar em uma notícia o app abre uma intent implícita que redireciona o
        * usuário para a página(web) da notícia em questão.
        */


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(noticias.get(position).link));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

    }

    /*
    * Método para modificar a data do formato Wed, 4 Jul 2001 12:08:56 -0700 para o formato dia/mês/ano e data hora:minutos:segundos
    */


    private String formataData(String inputDate) throws ParseException{
        Date date = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH).parse(inputDate);
        return new SimpleDateFormat(" dd-MM-yyyy HH:mm:ss").format(date);
    }


    @Override
    public int getItemCount() {
        return noticias.size();
    }
}