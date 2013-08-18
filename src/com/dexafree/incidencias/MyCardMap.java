package com.dexafree.incidencias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dexafree.incidencias.Card;

public class MyCardMap extends Card {


    public double x;
    public double y;

    public MyCardMap(String title){
        super(title);
    }

    public MyCardMap(String title, String desc){
        super(title, desc);
    }

    public MyCardMap(String title, String desc1, String desc2){
        super(title, desc1, desc2);
    }

    public MyCardMap(String title, String desc1, String desc2, String desc3, double x, double y){
        super(title, desc1, desc2, desc3);
        this.x = x;
        this.y = y;
    }

    public MyCardMap(String title, String desc1, String desc2, String desc3){
        super(title, desc1, desc2, desc3);
    }

    public MyCardMap(String title, String desc1, String desc2, String desc3, String desc4, double x, double y){
        super(title, desc1, desc2, desc3, desc4);
        this.x = x;
        this.y = y;
    }

    public MyCardMap(String title, String desc1, String desc2, String desc3, String desc4){
        super(title, desc1, desc2, desc3, desc4);
    }


    @Override
    public View getCardContent(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_ex_map, null);

        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((TextView) view.findViewById(R.id.description1)).setText(desc1);
        ((TextView) view.findViewById(R.id.description2)).setText(desc2);
        ((TextView) view.findViewById(R.id.description3)).setText(desc3);
        ((TextView) view.findViewById(R.id.description4)).setText(desc4);

        return view;
    }






}