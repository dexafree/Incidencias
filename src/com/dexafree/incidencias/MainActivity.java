package com.dexafree.incidencias;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.view.MenuItem;
import android.widget.Toast;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {

    ListView lv1;
    ProgressDialog ShowProgress;
    public ArrayList<Incidencia> IncidenciaList = new ArrayList<Incidencia>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
  /*      SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(
                        MainActivity.this);
        Log.i("", "Opcion: " + pref.getBoolean("provincias_sel", false));*/




        lv1 = (ListView) findViewById(R.id.listView1);

        ShowProgress = ProgressDialog.show(MainActivity.this, "",
                "Cargando. Espere por favor...", true);
        new loadingTask().execute("http://dgt.es/incidencias.xml");

        lv1.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri
                        .parse(IncidenciaList.get(position).getFechahora())); // AQUI IBA GETURL, PERO NO SE QUE HACE!
                startActivity(intent);

            }
        });





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    //GESTIONANDO EL CLICK DE LA ACTIONBAR

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_provincias:
                startActivity(new Intent(this, Provincias.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, Ajustes.class));
                return true;
            case R.id.actualizar:
                actualizar();
                Toast.makeText(getApplicationContext(), "Actualizado", Toast.LENGTH_LONG).show();
                return true;
            case R.id.probando:
                prueba();
                Toast.makeText(getApplicationContext(), "Hecho", Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    class loadingTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {

            SAXHelper sh = null;
            try {
                sh = new SAXHelper(urls[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            sh.parseContent("");
            return "";

        }

        protected void onPostExecute(String s) {
            lv1.setAdapter(new EfficientAdapter(MainActivity.this, IncidenciaList));
            ShowProgress.dismiss();

        }
    }

    class SAXHelper {
        public HashMap<String, String> userList = new HashMap<String, String>();
        private URL url2;

        public SAXHelper(String url1) throws MalformedURLException {
            this.url2 = new URL(url1);
        }

        public RSSHandler parseContent(String parseContent) {
            RSSHandler df = new RSSHandler();
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

    public void actualizar() {
        IncidenciaList.clear();
        lv1 = (ListView) findViewById(R.id.listView1);

        ShowProgress = ProgressDialog.show(MainActivity.this, "",
                "Cargando. Espere por favor...", true);
        new loadingTask().execute("http://dgt.es/incidencias.xml");

        lv1.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri
                        .parse(IncidenciaList.get(position).getFechahora())); // AQUI IBA GETURL, PERO NO SE QUE HACE!
                startActivity(intent);

            }
        });

    }

    public void prueba() {

        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

        Toast.makeText(getApplicationContext(), fDate, Toast.LENGTH_LONG).show();

    }


    public boolean comparaFecha(String fechahora){

        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

        //CREAMOS EL STRING YEAR, MONTH... PARA SACAR SOLO LOS PRIMEROS DIGITOS PARA INICIAR LA COMPARACION
        String year = new SimpleDateFormat("yyyy").format(cDate);
        String month = new SimpleDateFormat("MM").format(cDate);
        String day = new SimpleDateFormat("dd").format(cDate);

        Log.i("", "year month day: " + year + " " + month + " " + day);

        String yearpas = fechahora.substring(0,4);
        String monthpas = fechahora.substring(5,7);
        String daypas = fechahora.substring(8,10);

        Log.i("", "yearpas monthpas daypas: " + yearpas + " " + monthpas + " " + daypas);

        if (year == yearpas)
         {

            if (month == monthpas)
                {

                    if (day == daypas)
                     {
                        return true;
                     }
                    else
                     {
                        return false;
                     }
                }
            else
                {
                    return false;
                }
         }
        else
         {
          return false;
         }









    }

    public boolean checkProvincia(String provincia) {

        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(
                        MainActivity.this);

        Log.i("", provincia + ": " + pref.getBoolean(provincia, false));

        if ( pref.getBoolean(provincia, false) == true) {
            Log.i("", "Es true");
            return true;
        }
        else {
            Log.i("", "Es false");
            return false;
        }
    }


    class RSSHandler extends DefaultHandler {

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
               // Log.i("", "Funciona: " + currentIncidencia.getProvincia());
                if (checkProvincia(currentIncidencia.getProvincia()) == true)
                {
                 //   Log.i("", "Pasado el primer if");
                    if (comparaFecha(currentIncidencia.getFechahora().trim()) == true)

                    {
                   //     Log.i("", "Añadida la provincia: " + currentIncidencia.getProvincia());
                        IncidenciaList.add(currentIncidencia);
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

}