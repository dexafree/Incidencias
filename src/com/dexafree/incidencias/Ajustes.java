package com.dexafree.incidencias;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Carlos on 19/05/13.
 */
public class Ajustes extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
