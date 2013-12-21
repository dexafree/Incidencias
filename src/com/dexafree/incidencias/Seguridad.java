package com.dexafree.incidencias;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockActivity;

/**
 * Created by Carlos on 21/08/13.
 */
public class Seguridad extends SherlockActivity {

    private Button secButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seguridad);

        secButton = (Button)findViewById(R.id.secButton);


        new Evento("Dev_Menu");


        secButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0){

                startActivity(new Intent(Seguridad.this, DevMenu.class));

            }

        });

    }

}
