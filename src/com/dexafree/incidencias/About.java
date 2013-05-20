package com.dexafree.incidencias;

import android.os.Bundle;
import android.app.ActionBar;
import android.content.Intent;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

/**
 * Created by Carlos on 20/05/13.
 */
public class About extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about);

        //  ActionBar actionBar = getActionBar();
        //  actionBar.setDisplayHomeAsUpEnabled(true);
    }

}
