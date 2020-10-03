package br.ufpe.cin.android.rss;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/*
*  Activity criada para exibir o fragment criado
* Ela é chamada explicitamente no momento que o usuário clica no ícone de "+" para adicionar um novo link xml
*/

public class PreferenciasActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);
        //Após criar o fragmento, use o código abaixo para exibir

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings,new PrefsFragment())
                .commit();


    }


}