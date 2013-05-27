package com.dexafree.incidencias;

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
    public int pkInicial;
    public int pkFinal;

    private Favoritos currentFavorito = new Favoritos();

    final String arrayName = "Lista";

    public String previo;

    private Button btnGuardar;
    private Button btnCancelar;

    public EditText Edit1;
    public EditText Edit2;




    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.crea_favoritos);


        /** Getting an instance of shared preferences, that is being used in this context */
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        /** Get the value stored in the share preferences corresponding to the key "lp_android_choice" */
        carretera = sp.getString("carretera_seleccionada", "None Selected");
        pkFinal = 0;
        pkInicial = 0;

        /** Getting an instance of the textview object corresponds to the layout in main.xml */
        TextView tv = (TextView) findViewById(R.id.selec_carr);

        /** Set the value to the textview object */
        tv.setText(carretera);





        btnGuardar = (Button)findViewById(R.id.btnGuardar);
        btnCancelar = (Button)findViewById(R.id.btnCancelar);

        Edit1   = (EditText)findViewById(R.id.pkIn);
        Edit2   = (EditText)findViewById(R.id.pkFin);






        btnGuardar.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0){

//                    Favoritos currentFavorito = new Favoritos();

                carretera = sp.getString("carretera_seleccionada", "None Selected");

                    String pkInicialString = Edit1.getText().toString();
                    String pkFinString = Edit2.getText().toString();

                    Log.d("", "Carretera: " + carretera);


                    int pkInicial = Integer.parseInt(pkInicialString);
                    int pkFinal = Integer.parseInt(pkFinString);

                    Log.d("", "pkInicial: " + pkInicial);
                    Log.d("", "pkFinal: " + pkFinal);

                    currentFavorito.setCarretera(carretera);
                    currentFavorito.setPkInicial(pkInicial);
                    currentFavorito.setPkFinal(pkFinal);

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

                    for(int i=0; i<lines; i++){
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
                    Log.d("","Datos: " + carretera + "  " + pkInicial + "  " + pkFinal);
                    sb.append("<favorito>");
                    sb.append("<carretera>" + carretera + "</carretera>");
                    sb.append("<pkInicial>" + pkInicial + "</pkInicial>");
                    sb.append("<pkFinal>" + pkFinal + "</pkFinal>");
                    sb.append("</favorito>");

                    Log.d("", "sb antes de append: " + sb.toString());



                    //Escribimos el resultado a un fichero
                    fout.append(sb.toString());

                    fout.close();

                    Log.i("XmlTips", "Fichero XML creado correctamente.");


                    Context context = getApplicationContext();
                    CharSequence text = "Favorito añadido con la carretera " + carretera + ", PkI " + pkInicial + ", pkF " +pkFinal;
                    int duration = Toast.LENGTH_SHORT;

                    finish();

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













  /* public void AddFav(String carretera, int pkInicial, int pkFinal){
            if (fav1[0] == null){

                fav1[0]  = carretera;
                fav1[1] = pkInicial;
                fav1[2] = pkFinal;
            }

            else if (fav1[0] != null){

                if (fav2[0] == null){

                    fav2[0] = carretera;
                    fav2[1] = pkInicial;
                    fav2[2] = pkFinal;
                }
                else if (fav2[0] != null){

                    if (fav3[0] == null){

                        fav3[0] = carretera;
                        fav3[1] = pkInicial;
                        fav3[2] = pkFinal;
                    }
                }
            }
        }*/

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


        public void save(){
                 try
                    {
                        //Creamos un fichero en la memoria interna
                        OutputStreamWriter fout =
                                new OutputStreamWriter(
                                        openFileOutput("Favoritos.xml", Context.MODE_PRIVATE));

                        StringBuilder sb = new StringBuilder();

                        //Construimos el XML
                        sb.append("<favorito>");
                        sb.append("<carretera>" + carretera + "</carretera>");
                        sb.append("<pkInicial>" + pkInicial + "</pkInicial>");
                        sb.append("<pkFinal>" + pkFinal + "</pkFinal>");
                        sb.append("</favorito>");



                        //Escribimos el resultado a un fichero
                        fout.write(sb.toString());
                        fout.close();

                        Log.i("XmlTips", "Fichero XML creado correctamente.");
                    }
                    catch (Exception ex)
                    {
                        Log.e("XmlTips", "Error al escribir fichero XML.");
                    }
                }



    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        TextView tv = (TextView) findViewById(R.id.selec_carr);
        tv.setText(newValue.toString());
        return true;
    }

    public void addTask(Favoritos currentFavorito) {
        assert(null != currentFavorito);
        if (null == Favoritos.FavoritosList) {
            Favoritos.FavoritosList = new ArrayList<Favoritos>();
        }
        Favoritos.FavoritosList.add(currentFavorito);

        //save the task list to preference
        SharedPreferences prefs = getSharedPreferences("FAVLIST", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString("FAVORITOS", ObjectSerializer.serialize(Favoritos.FavoritosList));
            Log.d("", "Añadido correctamente");
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }


}





