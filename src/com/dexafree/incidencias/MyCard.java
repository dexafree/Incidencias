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

    @Override
    public View getCardContent(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_ex, null);

        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((TextView) view.findViewById(R.id.description)).setText(desc);



        return view;
    }




}