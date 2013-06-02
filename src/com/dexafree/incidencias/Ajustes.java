package com.dexafree.incidencias;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import com.dexafree.incidencias.R;
import android.view.MenuItem;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.preference.Preference.OnPreferenceChangeListener;
import java.util.Calendar;


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
        updateTimePickerTime();
        updateIntervalPreference();

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


        updateTimePickerTime();
        updateIntervalPreference();
        SwitchPreference autoUpdate = (SwitchPreference) findPreference("autorefresh");
        autoUpdate.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if((Boolean) newValue == true){
                    doScheduledSearch();
                }else{
                    cancelSchedule();
                }
                return true;
            }
        });
        TimePickerDialog time = (TimePickerDialog) findPreference("hourtoSearch");
        time.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                doScheduledSearch();
                Log.d("TIMEPICKER", "PREFERENCE CHANGED");
                return false;
            }
        });


    }



    private void doScheduledSearch(){
        sendBroadcast(new Intent("INCIDENCIAS.START_AUTOUPDATE"));
    }

    private void cancelSchedule(){
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent("INCIDENCIAS.AUTOUPDATE");
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pi);
    }


    private void updateTimePickerTime(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int minutes = sp.getInt("minute", 0);
        String minutesStr;
        if(minutes < 10){
            minutesStr = "0"+minutes;
        }else{
            minutesStr = String.valueOf(minutes);
        }
        Preference tp = findPreference("hourtoSearch");
        tp.setTitle("Hora a la que buscar actualizaciones: "+sp.getInt("hour", 12)+":"+minutesStr);
    }

    private void updateIntervalPreference(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String interval = sp.getString("updateInterval", "86400000");
        String text = "";
        if(interval.equals("86400000")){
            text = "Diariamente";
        }else if(interval.equals("3600000")){
            text = "Cada hora";
        }else if(interval.equals("1800000")){
            text = "Cada media hora";
        }else if(interval.equals("900000")){
            text = "Cada 15 minutos";
        }else if(interval.equals("60000")){
            text = "Cada minuto";
        }
        Preference intervalPref = findPreference("updateInterval");
        intervalPref.setTitle("Intervalo entre busquedas: "+text);
        Log.d("Text", text);
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
