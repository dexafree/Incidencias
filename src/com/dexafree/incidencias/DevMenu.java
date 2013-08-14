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
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Carlos on 19/07/13.
 */
public class DevMenu extends Activity {

    private Button btnDGT;
    private Button btnDGT2;
    private Button force;
    private TextView txtv;
    private TextView txtv2;
    private ProgressDialog dialog;
    public String asda;
    public String asda2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dev_menu);

        btnDGT = (Button)findViewById(R.id.btnTestDGT);
        btnDGT2 = (Button)findViewById(R.id.btnTestDGT2);
        force = (Button)findViewById(R.id.force);
        txtv = (TextView)findViewById(R.id.DGTFH);
        txtv2 = (TextView)findViewById(R.id.DGTFH2);

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

                new MiTarea2().execute("http://dexafree.quijost.com/incidencias/InciDGT.xml");

            }


        });

        force.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://dexafree.quijost.com/incidencias/actualizar.php"));
                startActivity(browserIntent);

            }


        });



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
                HttpGet httppost = new HttpGet("http://dexafree.quijost.com/incidencias/InciDGT.xml");

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


}