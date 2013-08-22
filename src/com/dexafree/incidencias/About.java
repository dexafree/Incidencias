package com.dexafree.incidencias;

import android.net.Uri;
import android.os.Bundle;
import android.app.ActionBar;
import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.MenuItem;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.preference.Preference.OnPreferenceChangeListener;

/**
 * Created by Carlos on 20/05/13.
 */
public class About extends PreferenceActivity {

    private int DEVTOUCH = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        Preference dev = findPreference("dev_menu");
        dev.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                if(DEVTOUCH == 7){

                    Log.d("DEVMENU", "Llevas 7!");

                    DEVTOUCH = 0;
                    iniciar();
                    return false;
                }

                else if(DEVTOUCH != 7){
                    DEVTOUCH++;
                    Log.d("DEVMENU", ""+DEVTOUCH);
                    return false;
                }

                return false;
            }
        });


        Preference politica = findPreference("politica");
        politica.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                String url = "http://www.dexa-dev.es/prueba/privacy.htm";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
            }
        });


    }

    public void iniciar(){
        startActivity(new Intent(this, Seguridad.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
