package com.dexafree.incidencias;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.preference.Preference.OnPreferenceChangeListener;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class About extends PreferenceActivity {

    private int DEVTOUCH = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        Preference dev = findPreference("dev_menu");
        dev.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                if(DEVTOUCH == 7){

                    Log.d("DEVMENU", "Llevas 7!");

                    DEVTOUCH = 0;
                    iniciar();
                    return false;
                }

                else if(DEVTOUCH != 7){
                    DEVTOUCH++;
                    Log.d("DEVMENU", ""+DEVTOUCH);
                    return false;
                }

                return false;
            }
        });


        Preference politica = findPreference("politica");
        politica.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                String url = "http://www.dexa-dev.es/prueba/privacy.htm";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
            }
        });


        Preference feedback = findPreference("feedback");
        feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                AlertDialog.Builder alert = new AlertDialog.Builder(About.this);
                LayoutInflater inflater = About.this.getLayoutInflater();
                //this is what I did to added the layout to the alert dialog
                View layout=inflater.inflate(R.layout.alert,null);
                alert.setView(layout);
                final EditText userName = (EditText)layout.findViewById(R.id.nameField);
                final EditText userEmail = (EditText)layout.findViewById(R.id.emailField);
                final EditText message = (EditText)layout.findViewById(R.id.commentField);

                alert.setTitle("Feedback");
                alert.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String nombre = userName.getText().toString();
                        String email = userEmail.getText().toString();
                        String mensaje = message.getText().toString();

                        Log.d("Nombre", nombre);
                        Log.d("email", email);
                        Log.d("mensaje", mensaje);
                        Log.d("mensaje l", mensaje.length()+"");

                        if(nombre.length() != 0 && mensaje.length() != 0){
                            new envioEmail().execute(nombre, email, mensaje);
                            Context context = getApplicationContext();
                            CharSequence text = "Sugerencia enviada\nGracias por tu colaboración";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                        else{
                            Context context = getApplicationContext();
                            CharSequence text = "Por favor, rellena los campos antes de darle a Enviar";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }
                });

                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.dismiss();

                    }
                });

                alert.create().show();




                return true;

            }
        });


    }

    public void iniciar(){
        startActivity(new Intent(this, Seguridad.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void enviarMail(String nombre, String email, String mensaje){



    }


    public static class envioEmail extends AsyncTask<String, Float, Integer> {

        protected void onPreExecute() {
        }

        protected Integer doInBackground(String... params) {
            try{

                //OBTENEMOS HORA
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


                //OBTENEMOS FECHA
                Date cDate = new Date();
                String aDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

                //GENERAMOS ID
                // Usually this can be a field rather than a method variable
                Random rand = new Random();

                // nextInt is normally exclusive of the top value,
                // so add 1 to make it inclusive
                int ID = rand.nextInt((999999 - 100000) + 1) + 100000;


                String newUrl = "http://www.dexa-dev.es/incidencias/php/mail.php" +
                        "?fecha=" + URLEncoder.encode(aDate, "UTF-8") +
                        "&hora=" + URLEncoder.encode(hora, "UTF-8") +
                        "&autor=" + URLEncoder.encode(params[0], "UTF-8") +
                        "&emautor=" + URLEncoder.encode(params[1], "UTF-8") +
                        "&mensaje=" + URLEncoder.encode(params[2], "UTF-8") +
                        "&ID=" + ID ;

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

}
