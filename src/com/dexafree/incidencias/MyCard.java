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

    public MyCard(String title, String desc1, String desc2, String desc3){
        super(title, desc1, desc2, desc3);
    }

    public MyCard(String title, String desc1, String desc2, String desc3, String desc4, String desc5){
        super(title, desc1, desc2, desc3, desc4, desc5);
    }

    @Override
    public View getCardContent(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_ex, null);

        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((TextView) view.findViewById(R.id.description1)).setText(desc1);
        ((TextView) view.findViewById(R.id.description2)).setText(desc2);
        ((TextView) view.findViewById(R.id.description3)).setText(desc3);
        ((TextView) view.findViewById(R.id.description4)).setText(desc4);
        ((TextView) view.findViewById(R.id.description5)).setText(desc5);



        return view;
    }




}