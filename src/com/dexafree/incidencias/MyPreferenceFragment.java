package com.dexafree.incidencias;

/**
 * Created by Carlos on 26/05/13.
 */
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;

public class MyPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.carreteras);

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
        ListPreference p = (ListPreference ) getPreferenceManager().findPreference("carretera_seleccionada");
        /** Setting Preference change listener for the ListPreference */
        p.setOnPreferenceChangeListener(onPreferenceChangeListener);
    }
}