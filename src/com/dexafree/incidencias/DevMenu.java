package com.dexafree.incidencias;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URLEncoder;

/**
 * Created by Carlos on 19/07/13.
 */
public class DevMenu extends Activity {

    private Button btnDGT;
    private Button btnDGT2;
    private Button force;
    private Button sendAlert;
    private Button sendJson;
    private EditText alertET;
    private TextView txtv;
    private TextView txtv2;
    private TextView estadoalerta;
    private ProgressDialog dialog;
    public String asda;
    public String asda2;
    String url = "http://www.dexa-dev.es/incidencias/php/webservice4.php";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dev_menu);

        btnDGT = (Button)findViewById(R.id.btnTestDGT);
        btnDGT2 = (Button)findViewById(R.id.btnTestDGT2);
        force = (Button)findViewById(R.id.force);
        sendAlert = (Button)findViewById(R.id.sendAlert);
        sendJson = (Button)findViewById(R.id.sendJson);

        alertET = (EditText)findViewById(R.id.alerta);

        txtv = (TextView)findViewById(R.id.DGTFH);
        txtv2 = (TextView)findViewById(R.id.DGTFH2);
        estadoalerta = (TextView)findViewById(R.id.estadoAlerta);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Descargando...");
        dialog.setTitle("Progreso");
        dialog.setCancelable(false);
        //Realizamos cualquier otra operación necesaria
        //Creamos una nueva instancia y llamamos al método ejecutar
        //pasándole el string.


        btnDGT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0){

                new MiTarea().execute("http://www.dgt.es/incidenciasXY.xml");

            }


        });

        btnDGT2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0){

                new MiTarea2().execute("http://www.dexa-dev.es/incidencias/InciDGT.xml");

            }


        });

        force.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dexa-dev.es/incidencias/actualizar.php"));
                startActivity(browserIntent);

            }


        });

        sendAlert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                String alertaescrita = alertET.getText().toString();

                new sendAlert().execute(alertaescrita);

            }


        });

        sendJson.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                String json = Evento.generar();

                //getInputStreamFromUrl(url, json);

                new envioJson().execute(json);



            }


        });

    }

    public static InputStream getInputStreamFromUrl(String url, String params) {
        InputStream content = null;
        try {
            String newUrl = url + "?data=" + URLEncoder.encode(params, "UTF-8");


            Log.d("DEXA", ""+newUrl);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(newUrl));

            Log.d("DEXA", "Esto se ejecuta?");
            content = response.getEntity().getContent();
        } catch (Exception e) {
            Log.d("[GET REQUEST]", "Network exception", e);
        }
        return content;
    }

    public void guardar(Context context, String json){
        try {
            FileOutputStream fos = context.openFileOutput("registro.json", Context.MODE_PRIVATE);
            Writer out = new OutputStreamWriter(fos);
            out.write(json);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void guardar2(Context context, String json){
        try {
            FileOutputStream fos = context.openFileOutput("registro.json", Context.MODE_PRIVATE);
            Writer out = new OutputStreamWriter(fos);
            out.write(json);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void enviar(){

        String json = Evento.generar();

        new envioJson().execute(json);

    }


    private class MiTarea extends AsyncTask<String, Float, Integer>{

        protected void onPreExecute() {
            dialog.setProgress(0);
            dialog.setMax(100);
            dialog.show(); //Mostramos el diálogo antes de comenzar
        }

        protected Integer doInBackground(String... urls) {
            try{

                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet("http://www.dgt.es/incidenciasXY.xml");

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity ht = response.getEntity();

                BufferedHttpEntity buf = new BufferedHttpEntity(ht);

                InputStream is = buf.getContent();


                BufferedReader r = new BufferedReader(new InputStreamReader(is));

                StringBuilder total = new StringBuilder();
                String line;
                /*while ((line = r.readLine()) != null) {
                    total.append(line + "\n");
                }*/

                line = r.readLine();


                asda = line.substring(72,88);



            }
            catch(IOException e){
                Log.d("", "EXCEPCION");
            }

            return 0;
        }

        protected void onProgressUpdate (Float... valores) {
            int p = Math.round(100*valores[0]);
            dialog.setProgress(p);
        }

        protected void onPostExecute(Integer bytes) {
            dialog.dismiss();
            txtv.setText(asda);
        }
    }


    private class MiTarea2 extends AsyncTask<String, Float, Integer>{

        protected void onPreExecute() {
            dialog.setProgress(0);
            dialog.setMax(100);
            dialog.show(); //Mostramos el diálogo antes de comenzar
        }

        protected Integer doInBackground(String... urls) {
            try{

                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet("http://www.dexa-dev.es/incidencias/InciDGT.xml");

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity ht = response.getEntity();

                BufferedHttpEntity buf = new BufferedHttpEntity(ht);

                InputStream is = buf.getContent();


                BufferedReader r = new BufferedReader(new InputStreamReader(is));

                StringBuilder total = new StringBuilder();
                String line;
                /*while ((line = r.readLine()) != null) {
                    total.append(line + "\n");
                }*/

                line = r.readLine();


                asda2 = line.substring(72,88);



            }
            catch(IOException e){
                Log.d("", "EXCEPCION");
            }

            return 0;
        }

        protected void onProgressUpdate (Float... valores) {
            int p = Math.round(100*valores[0]);
            dialog.setProgress(p);
        }

        protected void onPostExecute(Integer bytes) {
            dialog.dismiss();
            txtv2.setText(asda2);
        }
    }

    public static class envioJson extends AsyncTask<String, Float, Integer>{

        protected void onPreExecute() {
        }

        protected Integer doInBackground(String... params) {
            try{

                String newUrl = "http://www.dexa-dev.es/incidencias/php/webservice4.php" + "?data=" + URLEncoder.encode(params[0], "UTF-8");

                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet(newUrl);

                HttpResponse response = httpclient.execute(httppost);

            }
            catch(IOException e){
                Log.d("", "EXCEPCION");
            }

            return 0;
        }

        protected void onProgressUpdate (Float... valores) {
            int p = Math.round(100*valores[0]);
        }

        protected void onPostExecute(Integer bytes) {
        }
    }

    private class sendAlert extends AsyncTask<String, Float, Integer>{

        protected void onPreExecute() {
        }

        protected Integer doInBackground(String... params) {
            try{

                String newUrl = "http://www.dexa-dev.es/incidencias/php/status.php" + "?msg=" + URLEncoder.encode(params[0], "UTF-8");

                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet(newUrl);

                HttpResponse response = httpclient.execute(httppost);

            }
            catch(IOException e){
                Log.d("", "EXCEPCION");
            }

            return 0;
        }

        protected void onProgressUpdate (Float... valores) {
            int p = Math.round(100*valores[0]);
        }

        protected void onPostExecute(Integer bytes) {
            estadoalerta.setText("Alerta enviada");
        }
    }


}