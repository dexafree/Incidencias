package com.dexafree.incidencias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyImageCard extends Card {

    public double x;
    public double y;

	public MyImageCard(String title, int image, String desc1, String desc2, String desc3){
		super(title, image, desc1, desc2, desc3);
	}

    public MyImageCard(String title, int image, String desc1, String desc2, String desc3, double x, double y){
        super(title, image, desc1, desc2, desc3);
        this.x = x;
        this.y = y;
    }

	@Override
	public View getCardContent(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.card_picture, null);

		((TextView) view.findViewById(R.id.title)).setText(title);
		((ImageView) view.findViewById(R.id.imageView1)).setImageResource(image);
        ((TextView) view.findViewById(R.id.descriptionim1)).setText(desc1);
        ((TextView) view.findViewById(R.id.descriptionim2)).setText(desc2);
        ((TextView) view.findViewById(R.id.descriptionim3)).setText(desc3);
		
		return view;
	}





	
	
	
}
