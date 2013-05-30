package com.dexafree.incidencias;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.Menu;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.parsers.*;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
import android.widget.AdapterView.OnItemClickListener;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.view.MenuItem;
import android.widget.Toast;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Carlos on 26/05/13.
 */
public class DelFavoritos extends Activity {

    public static ListView df_lv;
    String texto;
    InputStream ins;

    BufferedReader fav;

    //private List<Favoritos> favList = Favoritos.FavoritosList;


    public void onCreate(Bundle savedInstanceState) {

        Favoritos.FavoritosList.clear();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_favoritos);

        df_lv = (ListView) findViewById(R.id.mf_lv);
        df_lv.setAdapter(new FavoritosAdapter(DelFavoritos.this));




        df_lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                try {

                    BufferedReader fichero =
                            new BufferedReader(
                                    new InputStreamReader(
                                            openFileInput("Favoritos.xml")));


                    int lineas = load("Favoritos.xml");

                    Log.d("lineas","" + lineas);

                    String text;

                    StringBuilder bui = new StringBuilder();

                    Log.d("position", "" +position);

                    for (int i = 0; i < lineas; i++) {

                        Log.d("","i: " + i);


                        if (i != position) {
                            text = fichero.readLine();
                            Log.d("Text", text);

                            bui.append(text + "\n");
                            Log.d("bui", bui.toString());
                        }
                        else{
                            fichero.readLine();
                        }

                    }

                    fichero.close();

                    OutputStreamWriter nuevoxml =
                            new OutputStreamWriter(
                                    openFileOutput("Favoritos.xml", Context.MODE_PRIVATE));

                    nuevoxml.append(bui.toString());
                    nuevoxml.close();


                } catch (Exception exce) {
                    Log.e("Ficheros", "Pues no ha funcionado");
                }

                Favoritos.FavoritosList.remove(position);
                df_lv.setAdapter(new FavoritosAdapter(DelFavoritos.this));
                ManageFavoritos.mf_lv.setAdapter((new FavoritosAdapter(DelFavoritos.this)));


            }
        });

        try
        {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput("Favoritos.xml")));

            //ins = openFileInput("Favoritos.xml");

            int lines = load("Favoritos.xml");

            for(int i=0; i<lines; i++){

                texto = fin.readLine();
                Log.d("", "XML: " + texto);
                AndroidParseXMLActivity axa = new AndroidParseXMLActivity();
                Log.d("","Vamos a parsear");
                axa.parseXML(texto);
            }
            fin.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
            Log.e("Ficheros", "Creando uno");

            try{
                OutputStreamWriter fcrear =
                        new OutputStreamWriter(
                                openFileOutput("Favoritos.xml", Context.MODE_PRIVATE));
                fcrear.close();
                Log.e("", "Creado XML");
            }
            catch (Exception exc)
            {
                Log.e("","Ni siquiera se puede crear");
            }
        }

        // mf_lv.setAdapter(new FavoritosAdapter(ManageFavoritos.this));


    }



    public int load(String fichero) throws IOException
    {

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(fichero)));
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




    public class AndroidParseXMLActivity {

        private void parseXML(String contenido) {

            String carretera;
            int pkI;
            int pkF;



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
