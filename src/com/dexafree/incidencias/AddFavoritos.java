package com.dexafree.incidencias;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Xml;
import android.view.MenuItem;
import android.widget.*;
import org.xmlpull.v1.XmlSerializer;

import java.io.*;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.preference.Preference.OnPreferenceChangeListener;

import com.dexafree.incidencias.Favoritos;



/**
 * Created by Carlos on 24/05/13.
 */



public class AddFavoritos extends Activity implements OnPreferenceChangeListener {

    //public static ArrayList<Favoritos> FavoritosList;
    public String carretera;
    //public int pkInicial;
    //public int pkFinal;
    public String provincia;

    private Favoritos currentFavorito = new Favoritos();

    final String arrayName = "Lista";

    public String previo;

    private Button btnGuardar;
    private Button btnCancelar;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.crea_favoritos);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        /** Getting an instance of shared preferences, that is being used in this context */
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        /** Get the value stored in the share preferences corresponding to the key "lp_android_choice" */
        carretera = sp.getString("carretera_seleccionada", "Ninguna carretera seleccionada");
        provincia = sp.getString("provincia_seleccionada", "Ninguna provincia seleccionada");
       // pkFinal = 0;
       // pkInicial = 0;

        /** Getting an instance of the textview object corresponds to the layout in main.xml */
        TextView tv = (TextView) findViewById(R.id.selec_carr);

        /** Set the value to the textview object */
        tv.setText(carretera);

        /** Getting an instance of the textview object corresponds to the layout in main.xml */
        TextView tv2 = (TextView) findViewById(R.id.selec_prov);

        /** Set the value to the textview object */
        tv2.setText(provincia);



        btnGuardar = (Button)findViewById(R.id.btnGuardar);
        btnCancelar = (Button)findViewById(R.id.btnCancelar);


        btnGuardar.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0){

//                    Favoritos currentFavorito = new Favoritos();

                carretera = sp.getString("carretera_seleccionada", "None Selected");
                provincia = sp.getString("provincia_seleccionada", "None Selected");

                    Log.d("", "Carretera: " + carretera);
                    Log.d("", "Provincia: " + provincia);

                    currentFavorito.setCarretera(carretera);
                    currentFavorito.setProvincia(provincia);
                    currentFavorito.setTipo(1);


                    Favoritos.FavoritosList.add(currentFavorito);

                try
                {
                    try{
                    BufferedReader fin =
                            new BufferedReader(
                                    new InputStreamReader(
                                            openFileInput("Favoritos.xml")));

                    Log.d("","Archivo abierto");

                    int lines = load();

                        previo = fin.readLine();
                    for(int i=1; i<lines; i++){
                        previo = previo + "\n" + fin.readLine();
                        Log.d("","previo: " + previo);
                        }

                        fin.close();
                    }

                    catch (Exception ex)
                    {

                        Log.e("XmlTips", "No existe el xml. Creando uno");
                        OutputStreamWriter fcrear =
                                new OutputStreamWriter(
                                        openFileOutput("Favoritos.xml", Context.MODE_PRIVATE));
                        fcrear.close();

                    }

                    //Creamos un fichero en la memoria interna
                    Log.d("","Sale del primer try");
                    OutputStreamWriter fout =
                            new OutputStreamWriter(
                                    openFileOutput("Favoritos.xml", Context.MODE_PRIVATE));

                    StringBuilder sb = new StringBuilder();

                    Log.d("","Consigue crear el sb");


                    Log.d("", "Previo antes del if de previo: " + previo);
                    //Construimos el XML
                    if (previo != null){

                       // char ar[] = previo.toCharArray();
                        sb.append(previo + "\n");
                    }

                    Log.d("","Pasa el if");
                    Log.d("","Datos: " + carretera + "  " + provincia + "  ");
                    sb.append("<favorito>");
                    sb.append("<tipo>" + 1 + "</tipo>");
                    sb.append("<carretera>" + carretera + "</carretera>");
                    sb.append("<provincia>" + provincia + "</provincia>");
                    sb.append("<pkInicial>" + "0" + "</pkInicial>");
                    sb.append("<pkFinal>" + "0" + "</pkFinal>");
                    sb.append("</favorito>");

                    Log.d("", "sb antes de append: " + sb.toString());



                    //Escribimos el resultado a un fichero
                    fout.append(sb.toString());

                    fout.close();

                    Log.i("XmlTips", "Fichero XML creado correctamente.");

                    Context context = getApplicationContext();
                    CharSequence text = "Favorito añadido con la carretera " + carretera + ", provincia " + provincia;
                    int duration = Toast.LENGTH_SHORT;

                    finish();

                    ManageFavoritos.mf_lv.setAdapter((new FavoritosAdapter(AddFavoritos.this)));
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                catch (Exception ex)
                {
                    Log.e("XmlTips", "Error al escribir fichero XML.");
                }
            }
            });

        btnCancelar.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0){
                finish();

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, ManageFavoritos.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public int load() throws IOException
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



    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        Log.d("preference",preference.toString() );

        if(preference.toString().equalsIgnoreCase("HAZ CLICK AQUÍ PARA SELECCIONAR UNA CARRETERA")){

            TextView tv = (TextView) findViewById(R.id.selec_carr);
            tv.setText(newValue.toString());

        }

        else if (preference.toString().equalsIgnoreCase("HAZ CLICK AQUÍ PARA SELECCIONAR UNA PROVINCIA")){

            TextView tv = (TextView) findViewById(R.id.selec_prov);
            tv.setText(newValue.toString());

        }
        return true;
    }



}





