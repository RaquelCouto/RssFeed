package br.ufpe.cin.android.rss;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

public class PrefsFragment extends PreferenceFragmentCompat {

    /*
    * Adiciona as preferencias do arquivo xml ao fragmento
    */

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferencias);
    }

}
