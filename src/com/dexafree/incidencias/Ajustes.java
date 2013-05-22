package com.dexafree.incidencias;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

/**
 * Created by Carlos on 19/05/13.
 */
public class Ajustes extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

      //  ActionBar actionBar = getActionBar();
      //  actionBar.setDisplayHomeAsUpEnabled(true);

        ListPreference splashList = (ListPreference) findPreference("hora_selecc");
        splashList.setSummary(splashList.getEntry());

        splashList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String nv = (String) newValue;

                if (preference.getKey().equals("hora_selecc")) {
                    ListPreference splashList = (ListPreference) preference;
                    splashList.setSummary(splashList.getEntries()[splashList.findIndexOfValue(nv)]);
                }
                return true;
            }

        });

    }

    /*@Override
   public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
*/
}
