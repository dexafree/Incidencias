package com.dexafree.incidencias;

import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created by Carlos on 24/05/13.
 */

public class Favoritos {

    public static ArrayList<Favoritos> FavoritosList = new ArrayList<Favoritos>();


    public String carretera;

    public String provincia;

    public String fechahora;


    public void setProvincia(String provincia){
        this.provincia = provincia;
    }

    public String getProvincia(){
        return provincia;
    }





    public void setCarretera(String carretera) {
        this.carretera = carretera;
    }

    public String getCarretera() {
        return carretera;
    }


}
