package com.dexafree.incidencias;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class UpdateService extends Service {


    public ArrayList<Incidencia> IncidenciaList3 = MainActivity.IncidenciaList;
    public ArrayList<Favoritos> favorList = Favoritos.FavoritosList;

    private boolean inciFavExist = false;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Thread updateThread = new Thread(new Runnable() {

            @Override
            public void run() {
                Log.d("THREAD", "THREAD STARTED");
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                Log.d("Favoritos size"," " + Favoritos.FavoritosList.size());

                if(Favoritos.FavoritosList.size() > 0 && sp.getBoolean("autorefresh", false) == true){
                    new loadingTask3().execute("http://dgt.es/incidenciasXY.xml");
                }
                else if(Favoritos.FavoritosList.size() == 0){
                    try
                    {
                        BufferedReader fin =
                                new BufferedReader(
                                        new InputStreamReader(
                                                openFileInput("Favoritos.xml")));

                        //ins = openFileInput("Favoritos.xml");

                        int lines = load3();

                        for(int i=0; i<lines; i++){

                            String texto = fin.readLine();
                            Log.d("", "XML: " + texto);
                            AndroidParseXMLActivity4 axa = new AndroidParseXMLActivity4();
                            Log.d("","Vamos a parsear");
                            axa.parseXML(texto);
                            Log.d("Favoritos size post parse", " " + Favoritos.FavoritosList.size());
                        }
                        fin.close();

                        Log.d("", "FavoritosList llenada");
                        if(sp.getBoolean("autorefresh", false) == true){
                        new loadingTask3().execute("http://dgt.es/incidenciasXY.xml");
                        }
                    }
                    catch (Exception favlist0){

                        Log.d("SERVICE", "No hay favoritos añadidos");

                    }
                }





                Log.d("THREAD","JSON READ at "+new Time(System.currentTimeMillis()).toGMTString());


                Log.d("AutoRefresh: ", "" + sp.getBoolean("autorefresh", false));

                Log.d("inciFavExist: " , "" + inciFavExist);
                if(inciFavExist == true && sp.getBoolean("autorefresh", false) == true){
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Notification.Builder builder = new Notification.Builder(getApplicationContext());
                    builder.setTicker("Incidencias encontradas");
                    builder.setContentTitle("Se han encontrado incidencias");
                    builder.setContentText("Hay incidencias en tus sitios favoritos");
                    builder.setSmallIcon(R.drawable.yaos_small);
                    builder.setWhen(System.currentTimeMillis());
                    builder.setAutoCancel(true);
                    Intent i = new Intent(getApplicationContext(), MainFavoritos.class);
                    //i.putExtra("jsonFile", json.getJsonFile());
                    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pi);
                    nm.notify(0, builder.getNotification());
                    Log.d("NOTIFICACION","Notificacion mostrada");
                }

            }
        });
        updateThread.start();
        super.onStart(intent, startId);
    }

    class loadingTask3 extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {

            SAXHelper4 sh = null;
            try {
                sh = new SAXHelper4(urls[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            sh.parseContent("");
            return "";

        }

        protected void onPostExecute(String s) {
            //   lv1.setAdapter(new EfficientAdapter(MainActivity.this, IncidenciaList));

            Toast.makeText(getApplicationContext(), "Actualizado", Toast.LENGTH_LONG).show();

        }
    }

    public int load3() throws IOException
    {

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("Favoritos.xml")));
            String line;


            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');

                lineCount++;
            }
            Log.d("", "Lines: " + lineCount);

            return lineCount;

        }
        catch (IOException e) {
            Log.d("", "Ha saltado la excepcion de load");
            return 0;
        }

    }


    class SAXHelper4 {
        public HashMap<String, String> userList = new HashMap<String, String>();
        private URL url2;

        public SAXHelper4(String url1) throws MalformedURLException {
            this.url2 = new URL(url1);
        }

        public RSSHandler4 parseContent(String parseContent) {
            RSSHandler4 df = new RSSHandler4();
            try {

                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();
                XMLReader xr = sp.getXMLReader();
                xr.setContentHandler(df);
                xr.parse(new InputSource(url2.openStream()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return df;
        }
    }


    class RSSHandler4 extends DefaultHandler {

        private Incidencia currentIncidencia = new Incidencia();
        StringBuffer chars = new StringBuffer();

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes atts) {

            chars = new StringBuffer();
            if (localName.equalsIgnoreCase("incidencia")) {

            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {

            if (localName.equalsIgnoreCase("tipo")
                    && currentIncidencia.getTipo() == null) {
                currentIncidencia.setTipo(chars.toString().trim());

            }
            if (localName.equalsIgnoreCase("autonomia")
                    && currentIncidencia.getAutonomia() == null) {
                currentIncidencia.setAutonomia(chars.toString().trim());

            }
            if (localName.equalsIgnoreCase("provincia")
                    && currentIncidencia.getProvincia() == null) {
                currentIncidencia.setProvincia(chars.toString().trim());

            }
            if (localName.equalsIgnoreCase("causa")
                    && currentIncidencia.getCausa() == null) {
                currentIncidencia.setCausa(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("poblacion")
                    && currentIncidencia.getPoblacion() == null) {
                currentIncidencia.setPoblacion(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("fechahora_ini")
                    && currentIncidencia.getFechahora() == null) {
                currentIncidencia.setFechahora(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("nivel")
                    && currentIncidencia.getNivel() == null) {
                currentIncidencia.setNivel(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("carretera")
                    && currentIncidencia.getCarretera() == null) {
                currentIncidencia.setCarretera(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("pk_inicial")
                    && currentIncidencia.getPkInicio() == null) {
                currentIncidencia.setPkInicio(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("pk_final")
                    && currentIncidencia.getPkFin() == null) {
                currentIncidencia.setPkFin(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("sentido")
                    && currentIncidencia.getSentido() == null) {
                currentIncidencia.setSentido(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("hacia")
                    && currentIncidencia.getHacia() == null) {
                currentIncidencia.setHacia(chars.toString().trim());
            }

            if (localName.equalsIgnoreCase("incidencia")) {

               //Log.d("","Paso 1");

                if (comparaFecha(currentIncidencia.getFechahora().trim()) == true) {

                 //   Log.d("","Paso 2");

                    for (int i = 0; i < favorList.size(); i++){

                   //     Log.d("","Paso 3");
                        if ((favorList.get(i).getProvincia()).equalsIgnoreCase(currentIncidencia.getProvincia())){
                     //       Log.d("","Paso 4");
                            if ((favorList.get(i).getCarretera()).equalsIgnoreCase(currentIncidencia.getCarretera())){
                                Log.d("","ENCONTRADA INCIDENCIA!");
                                inciFavExist = true;
                                break;
                            }
                        }
                    }
                }
                currentIncidencia = new Incidencia();
            }
        }

        @Override
        public void characters(char ch[], int start, int length) {
            chars.append(new String(ch, start, length));
        }

    }


    public boolean comparaFecha(String fechahora){

        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

        //CREAMOS EL STRING YEAR, MONTH... PARA SACAR SOLO LOS PRIMEROS DIGITOS PARA INICIAR LA COMPARACION
        String year = new SimpleDateFormat("yyyy").format(cDate);
        String month = new SimpleDateFormat("MM").format(cDate);
        String day = new SimpleDateFormat("dd").format(cDate);

        //   Log.i("", "year month day: " + year + " " + month + " " + day);

        String yearpas = fechahora.substring(0,4);
        String monthpas = fechahora.substring(5,7);
        String daypas = fechahora.substring(8,10);

        //  Log.i("", "yearpas monthpas daypas: " + yearpas + " " + monthpas + " " + daypas);

        if (year.equals(yearpas)) {

            //  Log.i("", "COINCIDE YEAR");

            if (month.equals(monthpas)) {
                //  Log.i("", "COINCIDE MONTH");
                if (day.equals(daypas)) {
                    //    Log.i("", "COINCIDE DAY");
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else {
            //   Log.i("", "No coincide year");
            return false;
        }


    }


    public class AndroidParseXMLActivity4 {

        private void parseXML(String contenido) {




            try {

                Log.w("AndroidParseXMLActivity", "Start");
                /** Handling XML */
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();
                XMLReader xr = sp.getXMLReader();

                ItemXMLHandler myXMLHandler = new ItemXMLHandler();
                xr.setContentHandler(myXMLHandler);
                InputSource inStream = new InputSource();
                Log.w("AndroidParseXMLActivity", "Parse1");


                inStream.setCharacterStream(new StringReader(contenido.toString()));
                Log.w("AndroidParseXMLActivity", "Parse2");

                xr.parse(inStream);
                Log.w("AndroidParseXMLActivity", "Parse3");


                Log.w("AndroidParseXMLActivity", "Done");
            }
            catch (Exception e) {
                Log.w("AndroidParseXMLActivity",e );
            }


        }

    }





}
