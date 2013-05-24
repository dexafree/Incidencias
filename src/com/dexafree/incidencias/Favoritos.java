package com.dexafree.incidencias;

import java.util.ArrayList;

/**
 * Created by Carlos on 24/05/13.
 */

public class Favoritos {

    public ArrayList<String[]> FavoritosList;

    protected String carretera;

    protected int pkInicial;

    protected int pkFinal;

        public Favoritos(String carretera, int pkInicial, int pkFinal){
            this.carretera = carretera;
            this.pkInicial = pkInicial;
            this.pkFinal = pkFinal;
        }



}
