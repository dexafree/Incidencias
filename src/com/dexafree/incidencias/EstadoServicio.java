package com.dexafree.incidencias;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class EstadoServicio {

    private Activity mActivity;
    private String estado;

    // Constructor memorize the calling Activity ("context")
    public EstadoServicio(Activity context, String estado) {
        mActivity = context;
        this.estado = estado;
    }

    public void show() {

                final String title = "Estado del servicio";



                // Show the News since last version
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                        .setTitle(title)
                        .setMessage(estado)
                        .setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {

                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();
                            }
                        });
                builder.create().show();

    }

}