package com.dexafree.incidencias;

/**
 * Created by Carlos on 18/05/13.
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class EfficientAdapter extends BaseAdapter {



    private Activity activity;
    private ArrayList<Incidencia> data;
    private static LayoutInflater inflater = null;
    //public ImageLoader imageLoader;
    ViewHolder holder;

    EfficientAdapter(Activity a, ArrayList<Incidencia> d) {

        activity = a;
        data = d;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //	imageLoader = new ImageLoader(activity.getApplicationContext());

    }

    @Override
    public int getCount() {
        return data.toArray().length;

    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public static class ViewHolder {
        public TextView tipo;
        public TextView autonomia;
        public TextView provincia;
        public TextView causa;
        public TextView poblacion;
        public TextView fechahora;
        public TextView nivel;
        public TextView carretera;
        public TextView pkinicio;
        public TextView pkfin;
        public TextView sentido;
        public TextView hacia;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.row, null);
            holder = new ViewHolder();
            holder.tipo = (TextView) vi.findViewById(R.id.tipo);
            holder.autonomia = (TextView) vi.findViewById(R.id.autonomia);
            holder.provincia = (TextView) vi.findViewById(R.id.provincia);
            holder.causa = (TextView) vi.findViewById(R.id.causa);
            holder.poblacion = (TextView) vi.findViewById(R.id.poblacion);
            holder.fechahora = (TextView) vi.findViewById(R.id.fechahora);
            holder.nivel = (TextView) vi.findViewById(R.id.nivel);
            holder.carretera = (TextView) vi.findViewById(R.id.carretera);
            holder.pkinicio = (TextView) vi.findViewById(R.id.pkinicio);
            holder.pkfin = (TextView) vi.findViewById(R.id.pkfin);
            holder.sentido = (TextView) vi.findViewById(R.id.sentido);
            holder.hacia= (TextView) vi.findViewById(R.id.hacia);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        holder.tipo.setText("TIPO: " + data.get(position).getTipo());
        holder.autonomia.setText("AUTONOMÍA: " + data.get(position).getAutonomia());
        holder.provincia.setText("PROVINCIA: " + data.get(position).getProvincia());
        holder.causa.setText("CAUSA: " + data.get(position).getCausa());
        holder.poblacion.setText("POBLACIÓN: " + data.get(position).getPoblacion());
        holder.fechahora.setText("FECHA/HORA: " + data.get(position).getFechahora());
        holder.nivel.setText("NIVEL: " + data.get(position).getNivel());
        holder.carretera.setText("CARRETERA: " + data.get(position).getCarretera());
        holder.pkinicio.setText("PK INICIAL: " + data.get(position).getPkInicio());
        holder.pkfin.setText("PK FINAL: " + data.get(position).getPkFin());
        holder.sentido.setText("SENTIDO: " + data.get(position).getSentido());
        holder.hacia.setText("HACIA: " + data.get(position).getHacia());

//		imageLoader.DisplayImage((data.get(position).getThumbnail()), activity,
//				holder.image, 72, 72);
         /**URL url = null;
        try {
            url = new URL((data.get(position).getThumbnail()));
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        InputStream content = null;
        try {
            content = (InputStream)url.getContent();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Drawable d = Drawable.createFromStream(content , "src");
        Bitmap mIcon1 = null;
        try {
            mIcon1 =
                    BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.image.setImageBitmap(Bitmap.createScaledBitmap(mIcon1, 72, 72, false));
**/

        return vi;
    }






}
