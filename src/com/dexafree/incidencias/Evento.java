package com.dexafree.incidencias;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.google.gson.Gson;
import android.provider.Settings.Secure;



public class Evento {

    public final static ArrayList<Evento> Eventos = new ArrayList<Evento>();

    private String date;
    private String hour;
    private String mensaje;
    private String ID;

    public Evento(String mensaje) {
        setHour();
        setDate();
        setMensaje(mensaje);
        setID();
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
        String minutesString = minutes + "";
        if (minutes <= 9){
            minutesString = "0"+ minutes;
        }
        int hours = c.get(Calendar.HOUR_OF_DAY);
        String hoursString = hours + "";
        if (hours <= 9){
            hoursString = "0"+hours;
        }

        String hora = hoursString + ":" + minutesString;
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

    public void setID(){
        this.ID = Utils.ID;
    }



}