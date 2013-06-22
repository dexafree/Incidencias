package com.dexafree.incidencias;

/**
 * Created by Carlos on 27/05/13.
 */
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.preference.ListPreference;
import android.util.Log;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ItemXMLHandler extends DefaultHandler {

    Boolean currentElement = false;
    String currentValue = "";
    Favoritos favoritoActual = null;



    public ArrayList<Favoritos> getItemsList() {
        return Favoritos.FavoritosList;
    }

    // Called when tag starts
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        currentElement = true;
        currentValue = "";
        if (localName.equals("favorito")) {
            favoritoActual = new Favoritos();
        }

    }

    // Called when tag closing
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        currentElement = false;



        if (localName.equalsIgnoreCase("tipo")){
            int tipo = Integer.parseInt(currentValue);
            favoritoActual.setTipo(tipo);
        }
        if (localName.equalsIgnoreCase("carretera"))
            favoritoActual.setCarretera(currentValue);
        if (localName.equalsIgnoreCase("provincia")){
            favoritoActual.setProvincia(currentValue);
        }

        if (localName.equalsIgnoreCase("pkInicial")){
            int pkI = Integer.parseInt(currentValue);
            favoritoActual.setPkInicial(pkI);
        }
        if (localName.equalsIgnoreCase("pkFinal")){
            int pkF = Integer.parseInt(currentValue);
            favoritoActual.setPkFinal(pkF);
        }

        if (localName.equalsIgnoreCase("favorito"))
            Favoritos.FavoritosList.add(favoritoActual);
        Log.d("", "----------------");
    }

    // Called to get tag characters
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        if (currentElement) {
            currentValue = currentValue +  new String(ch, start, length);
        }

    }

}