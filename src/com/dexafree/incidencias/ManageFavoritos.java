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
import android.view.Menu;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.parsers.*;

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
public class ManageFavoritos extends Activity {

    ListView mf_lv;
    String texto;
    InputStream ins;

    BufferedReader fav;

    //private List<Favoritos> favList = Favoritos.FavoritosList;

    public void onCreate(Bundle savedInstanceState) {

        Favoritos.FavoritosList.clear();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_favoritos);

        mf_lv = (ListView) findViewById(R.id.mf_lv);
        try
        {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput("Favoritos.xml")));

            ins = openFileInput("Favoritos.xml");
            texto = fin.readLine();
            Log.d("", "XML: " + texto);
            AndroidParseXMLActivity axa = new AndroidParseXMLActivity();
            Log.d("","Vamos a parsear");
            axa.parseXML(texto);
            fin.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }

        mf_lv.setAdapter(new FavoritosAdapter(ManageFavoritos.this));


        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.favs_menu, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.fav_add:
                startActivity(new Intent(this, AddFavoritos.class));
                return true;
            case R.id.fav_del:
                int d = Favoritos.FavoritosList.size();
                Context context = getApplicationContext();
                CharSequence text = "Long FavoritosList: " + d;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                return true;
            case R.id.pruebafa:




            default:
                return super.onOptionsItemSelected(item);
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
