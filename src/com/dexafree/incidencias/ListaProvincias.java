package com.dexafree.incidencias;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class ListaProvincias extends ListActivity
{
    protected SharedPreferences preferences;
    protected String[] provincias;

    protected void checkProvinces()
    {
        ListView localListView = getListView();
        String str = this.preferences.getString("provinces", "");
        TreeSet localTreeSet = new TreeSet();
        StringTokenizer localStringTokenizer = new StringTokenizer(str, ",");
        if (!localStringTokenizer.hasMoreTokens());
        for (int i = 0; ; i++)
        {
           /* if (i >= this.provincias.length)
            {
                return;
                localTreeSet.add(localStringTokenizer.nextToken());
                break;
            } */
            if (!localTreeSet.contains(this.provincias[i]))
                continue;
            localListView.setItemChecked(i, true);
        }
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        this.provincias = getResources().getStringArray(2131034115);
        setListAdapter(new ArrayAdapter(this, 17367056, this.provincias));
        ListView localListView = getListView();
        localListView.setItemsCanFocus(false);
        localListView.setChoiceMode(2);
    }

    protected void onPause()
    {
        StringBuffer localStringBuffer = new StringBuffer();
        SparseBooleanArray localSparseBooleanArray = getListView().getCheckedItemPositions();
        for (int i = 0; ; i++)
        {
            if (i >= this.provincias.length)
            {
                SharedPreferences.Editor localEditor = this.preferences.edit();
                localEditor.putString("provincias", localStringBuffer.toString());
                localEditor.commit();
                super.onPause();
                return;
            }
            if (!localSparseBooleanArray.get(i))
                continue;
            localStringBuffer.append(this.provincias[i] + ",");
        }
    }

    protected void onResume()
    {
        checkProvinces();
        super.onResume();
    }
}