package com.dexafree.incidencias;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.AbsListView;
import android.widget.ListAdapter;

/**
 * Created by Carlos on 19/05/13.
 */
public class Provincias extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.provincias);
    }

}
