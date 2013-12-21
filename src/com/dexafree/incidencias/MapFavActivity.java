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

public class MapFavActivity extends SherlockFragmentActivity {

    private GoogleMap mapa = null;
    private double latCentral = 40.97872614480813;
    private double lonCentral = -3.051414079964161;
    private double zoomCentral = 9.396;
    public float z = (float)zoomCentral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_map);

        mapa = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        SharedPreferences pm =
                PreferenceManager.getDefaultSharedPreferences(
                        MapFavActivity.this);

        if(pm.getBoolean("trafico", false)){
            mapa.setTrafficEnabled(true);
        }
        else{
            mapa.setTrafficEnabled(false);
        }

        Intent intent = getIntent();
        double xcoord = intent.getDoubleExtra(MainFavoritos.XCOORDFAV, latCentral);
        double ycoord = intent.getDoubleExtra(MainFavoritos.YCOORDFAV, lonCentral);

        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom( new LatLng(xcoord, ycoord), z);

        mapa.moveCamera(camUpd1);

       /* mapa.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng point) {
                Projection proj = mapa.getProjection();
                Point coord = proj.toScreenLocation(point);

                Toast.makeText(
                        MapFavActivity.this,
                        "Click\n" +
                                "Lat: " + point.latitude + "\n" +
                                "Lng: " + point.longitude + "\n" +
                                "X: " + coord.x + " - Y: " + coord.y,
                        Toast.LENGTH_SHORT).show();
            }
        });

        mapa.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            public void onMapLongClick(LatLng point) {
                Projection proj = mapa.getProjection();
                Point coord = proj.toScreenLocation(point);

                Toast.makeText(
                        MapFavActivity.this,
                        "Click Largo\n" +
                                "Lat: " + point.latitude + "\n" +
                                "Lng: " + point.longitude + "\n" +
                                "X: " + coord.x + " - Y: " + coord.y,
                        Toast.LENGTH_SHORT).show();
            }
        }); */

       /* mapa.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            public void onCameraChange(CameraPosition position) {
                Toast.makeText(
                        MapFavActivity.this,
                        "Cambio Camara\n" +
                                "Lat: " + position.target.latitude + "\n" +
                                "Lng: " + position.target.longitude + "\n" +
                                "Zoom: " + position.zoom + "\n" +
                                "Orientacion: " + position.bearing + "\n" +
                                "Angulo: " + position.tilt,
                        Toast.LENGTH_SHORT).show();
            }
        }); */

        mapa.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(
                        MapFavActivity.this,
                        "Incidencia:\nHacia " +
                                marker.getTitle(),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        crearMarcadores();

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId())
        {
            case R.id.menu_marcadores:
                //mostrarMarcador(40.5, -3.5);
                crearMarcadores();
                break;
            case R.id.menu_lineas:
                mostrarLineas();
                break;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private void mostrarMarcador(Incidencia incidActual){


        double lat = incidActual.getX();
        double lng = incidActual.getY();

        mapa.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title(incidActual.getCarretera() + "\n" + incidActual.getHacia())
                .snippet((incidActual.getPkInicio() + " - " + incidActual.getPkFin())));
    }

    private void crearMarcadores(){

        Log.d("Tamaño", "" + MainFavoritos.IncidenciaList2.size());

        for (int i = 0; i < MainFavoritos.IncidenciaList2.size(); i++){



            if (MainFavoritos.IncidenciaList2.get(i).getX() != 0.0){
                Log.d("", "Marcador creado en: " + MainFavoritos.IncidenciaList2.get(i).getX() + " | " + MainFavoritos.IncidenciaList2.get(i).getY());
                mostrarMarcador(MainFavoritos.IncidenciaList2.get(i));
            }
        }

    }

    private void mostrarLineas(){
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