package com.dexafree.incidencias;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

public class TimePickerDialog extends DialogPreference{

    private SharedPreferences sp;
    private TimePicker tp;

    public TimePickerDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View onCreateDialogView() {
        LayoutInflater li = LayoutInflater.from(getContext());
        View dialog = li.inflate(R.layout.time_picker, null);
        tp = (TimePicker) dialog.findViewById(R.id.timePicker);
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        tp.setIs24HourView(DateFormat.is24HourFormat(getContext()));
        tp.setCurrentHour(sp.getInt("hour",12));
        tp.setCurrentMinute(sp.getInt("minute", 0));
        Log.d("HOUR",tp.getCurrentHour()+":"+tp.getCurrentMinute());
        return dialog;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(positiveResult){
            Editor edit = sp.edit();
            edit.putInt("hour", tp.getCurrentHour());
            edit.putInt("minute", tp.getCurrentMinute());
            Log.d("HOUR",tp.getCurrentHour()+":"+tp.getCurrentMinute());
            edit.commit();
            int minutes = tp.getCurrentMinute();
            String minutesStr;
            if(minutes < 10){
                minutesStr = "0"+minutes;
            }else{
                minutesStr = String.valueOf(minutes);
            }
            setTitle("Hora a la que buscar actualizaciones: "+tp.getCurrentHour()+":"+minutesStr);
            callChangeListener(tp.getCurrentHour()+":"+tp.getCurrentMinute());
        }
        super.onDialogClosed(positiveResult);
    }
}
