package com.dexafree.incidencias;

import android.content.Context;
import android.util.DisplayMetrics;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;

public class Utils {

    public static String ID;

    public Utils(String ID){
        this.setID(ID);
    }

	public float convertPixelsToDp(Context ctx, float px) {
		DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;

	}

	public static int convertDpToPixelInt(Context context, float dp) {

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int px = (int) (dp * (metrics.densityDpi / 160f));
		return px;
	}
	
	public static float convertDpToPixel(Context context, float dp) {

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float px = (float) (dp * (metrics.densityDpi / 160f));
		return px;
	}

    public static void setID(String ID){
        Utils.ID = ID;
    }

    public static String getID(){
        return ID;
    }


}
