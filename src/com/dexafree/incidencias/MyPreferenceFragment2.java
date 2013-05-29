package com.dexafree.incidencias;

/**
 * Created by Carlos on 26/05/13.
 */
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;

public class MyPreferenceFragment2 extends PreferenceFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.provinciasfav);

        /** Defining PreferenceChangeListener */
        OnPreferenceChangeListener onPreferenceChangeListener = new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                OnPreferenceChangeListener listener = ( OnPreferenceChangeListener) getActivity();
                listener.onPreferenceChange(preference, newValue);
                return true;
            }
        };

        /** Getting the ListPreference from the Preference Resource */
        ListPreference lp = (ListPreference ) getPreferenceManager().findPreference("provincia_seleccionada");
        /** Setting Preference change listener for the ListPreference */
        lp.setOnPreferenceChangeListener(onPreferenceChangeListener);
    }
}