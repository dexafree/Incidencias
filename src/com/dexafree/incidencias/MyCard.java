package com.dexafree.incidencias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dexafree.incidencias.Card;

public class MyCard extends Card {

    public MyCard(String title){
        super(title);
    }

    public MyCard(String title, String desc){
        super(title, desc);
    }

    public MyCard(String title, String desc1, String desc2){
        super(title, desc1, desc2);
    }

    @Override
    public View getCardContent(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_ex, null);

        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((TextView) view.findViewById(R.id.description1)).setText(desc1);
        ((TextView) view.findViewById(R.id.description2)).setText(desc2);



        return view;
    }




}