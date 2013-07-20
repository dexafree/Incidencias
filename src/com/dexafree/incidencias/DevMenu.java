package com.dexafree.incidencias;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
    private TextView txtv;
    private ProgressDialog dialog;
    public String asda;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dev_menu);

        btnDGT = (Button)findViewById(R.id.btnTestDGT);
        txtv = (TextView)findViewById(R.id.DGTFH);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Descargando...");
        dialog.setTitle("Progreso");
        dialog.setCancelable(false);
        //Realizamos cualquier otra operación necesaria
        //Creamos una nueva instancia y llamamos al método ejecutar
        //pasándole el string.


        btnDGT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0){

                new MiTarea().execute("http://www.dexa-dev.es/incidencias/InciDGT.xml");

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


}