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

    public int pkInicial;

    public int pkFinal;

    public int tipo;



    public void setProvincia(String provincia){
        this.provincia = provincia;
    }

    public String getProvincia(){
        return provincia;
    }

    public void setPkInicial(int pkInicial){
        this.pkInicial = pkInicial;
    }

    public int getPkInicial(){
        return pkInicial;
    }

    public void setPkFinal(int pkFinal){
        this.pkFinal= pkFinal;
    }

    public int getPkFinal(){
        return pkFinal;
    }

    public void setTipo(int tipo){
        this.tipo= tipo;
    }

    public int getTipo(){
        return tipo;
    }





    public void setCarretera(String carretera) {
        this.carretera = carretera;
    }

    public String getCarretera() {
        return carretera;
    }




}
