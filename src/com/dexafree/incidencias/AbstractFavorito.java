package com.dexafree.incidencias;

/**
 * Created by Carlos on 24/05/13.
 */
public class AbstractFavorito {

    protected String carretera;

    protected int pkInicial;

    protected int pkFinal;


    public String getFavCarretera(){
        return carretera;
    }

    public int getFavPkInicial() {
        return pkInicial;
    }

    public int getFavPkFinal(){
        return pkFinal;
    }

}
