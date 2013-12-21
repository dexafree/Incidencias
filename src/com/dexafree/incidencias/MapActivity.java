package com.dexafree.incidencias;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends SherlockFragmentActivity {

    private GoogleMap mapa = null;
    private double latCentral = 40.97872614480813;
    private double lonCentral = -3.051414079964161;
    //private double zoomCentral = 5.396;
    private double zoomCentral = 9.396;
    public float z = (float)zoomCentral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_map);

        /*SupportMapFragment fragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add((R.id.map), fragment).commit();*/

        mapa = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        //mapa = fragment.getMap();


        SharedPreferences pm =
                PreferenceManager.getDefaultSharedPreferences(
                        MapActivity.this);

        if(pm.getBoolean("trafico", false)){
            mapa.setTrafficEnabled(true);
        }
        else{
            mapa.setTrafficEnabled(false);
        }

        Intent intent = getIntent();
        double xcoord = intent.getDoubleExtra(MainActivity.XCOORD, latCentral);
        double ycoord = intent.getDoubleExtra(MainActivity.YCOORD, lonCentral);



        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom( new LatLng(xcoord, ycoord), z);

        mapa.moveCamera(camUpd1);

        mapa.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(
                        MapActivity.this,
                        "Incidencia:\nHacia " +
                                marker.getTitle(),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        crearMarcadores();

    }


    private void mostrarMarcador(Incidencia incidActual)
    //private void mostrarMarcador(double lat, double lng)
    {
          double lat = incidActual.getX();
          double lng = incidActual.getY();

          mapa.addMarker(new MarkerOptions()
              .position(new LatLng(lat, lng))
              .title(incidActual.getCarretera() + "\nSENTIDO " + incidActual.getHacia())
              .snippet((incidActual.getPkInicio() + " - " + incidActual.getPkFin())));

    }

    private void crearMarcadores(){

        Log.d("Tamaño", ""+ MainActivity.IncidenciaList.size());

        for (int i = 0; i < MainActivity.IncidenciaList.size(); i++){



            if (MainActivity.IncidenciaList.get(i).getX() != 0.0){
                Log.d("", "Marcador creado en: " + MainActivity.IncidenciaList.get(i).getX() + " | " + MainActivity.IncidenciaList.get(i).getY());
                mostrarMarcador(MainActivity.IncidenciaList.get(i));
            }
        }

    }

    private void mostrarLineas()
    {
        //Dibujo con Lineas

        PolylineOptions lineas = new PolylineOptions()
                .add(new LatLng(45.0, -12.0))
                .add(new LatLng(45.0, 5.0))
                .add(new LatLng(34.5, 5.0))
                .add(new LatLng(34.5, -12.0))
                .add(new LatLng(45.0, -12.0));

        lineas.width(8);
        lineas.color(Color.RED);

        mapa.addPolyline(lineas);

        //Dibujo con poligonos

        //PolygonOptions rectangulo = new PolygonOptions()
        //              .add(new LatLng(45.0, -12.0),
        //            	   new LatLng(45.0, 5.0),
        //            	   new LatLng(34.5, 5.0),
        //            	   new LatLng(34.5, -12.0),
        //            	   new LatLng(45.0, -12.0));
        //
        //rectangulo.strokeWidth(8);
        //rectangulo.strokeColor(Color.RED);
        //
        //mapa.addPolygon(rectangulo);
    }


}