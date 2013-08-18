package com.dexafree.incidencias;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.util.Log;
import android.view.Display;

import java.io.*;

import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.google.gson.Gson;

public class Evento {

    public final static ArrayList<Evento> Eventos = new ArrayList<Evento>();

    private String date;
    private String hour;
    private String mensaje;

    public Evento(String mensaje) {
        setHour();
        setDate();
        setMensaje(mensaje);
        Eventos.add(this);
    }

    public void setDate(){
        //Obtenemos la fecha actual
        Date cDate = new Date();
        String aDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        this.date = aDate;
    }

    public String getDate(){
        return date;
    }

    public void setHour(){
        //Obtenemos la hora actual
        Calendar c = Calendar.getInstance();
        int minutes = c.get(Calendar.MINUTE);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        String hora = hours + ":" + minutes;
        this.hour = hora;
    }

    public String getHour(){
        return hour;
    }

    public void setMensaje(String mensaje){
        this.mensaje = mensaje;
    }

    public String getMensaje(){
        return mensaje;
    }

    public static String generar(){
        Gson gson = new Gson();
        String json = gson.toJson(Eventos);
        //Log.d("STRING", json);
        return json;


    }



}