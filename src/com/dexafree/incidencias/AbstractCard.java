package com.dexafree.incidencias;

import android.content.Context;
import android.view.View;

public abstract class AbstractCard {

	protected String title;
	
	protected int image;
	
	protected String desc1;

    protected String desc2;
	
	public abstract View getView(Context context);
	
	public abstract View getView(Context context, boolean swipable);
	
	
	public String getTitle() {
		return title;
	}
	
	public String getDesc1() {
		return desc1;
	}

    public String getDesc2() {
        return desc2;
    }
	
	public int getImage() {
		return image;
	}
	
}
