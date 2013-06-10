package com.dexafree.incidencias;

/**
 * Created by Carlos on 21/05/13.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.view.MenuItem;
import android.widget.Toast;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ImageView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import com.dexafree.incidencias.CardStack;
import com.dexafree.incidencias.CardUI;
import com.dexafree.incidencias.MyImageCard;

public class MainActivity extends Activity {

    ProgressDialog ShowProgress;
    public static ArrayList<Incidencia> IncidenciaList = new ArrayList<Incidencia>();
    private CardUI mCardView;

    String texto;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cards);



        // init CardView
        mCardView = (CardUI) findViewById(R.id.cardsview);
        mCardView.setSwipeable(true);




        //TAREA DE CARGA DE XML Y PARSEO
        SharedPreferences pm =
                PreferenceManager.getDefaultSharedPreferences(
                        MainActivity.this);

        if ( pm.getBoolean("actuinicio", false)) {
            // Log.i("", "Es true");
            ShowProgress = ProgressDialog.show(MainActivity.this, "",
                    "Cargando. Espere por favor...", true);
            new loadingTask().execute("http://www.dgt.es/incidenciasXY.xml");
        }

        firstTime();
        borrardatos();

        Favoritos.FavoritosList.clear();

        try
        {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput("Favoritos.xml")));

            //ins = openFileInput("Favoritos.xml");

            int lines = load();

            for(int i=0; i<lines; i++){

                texto = fin.readLine();
                Log.d("", "XML: " + texto);
                AndroidParseXMLActivity axa = new AndroidParseXMLActivity();
                Log.d("","Vamos a parsear");
                axa.parseXML(texto);
            }
            fin.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
            Log.e("Ficheros", "Creando uno");

            try{
                OutputStreamWriter fcrear =
                        new OutputStreamWriter(
                                openFileOutput("Favoritos.xml", Context.MODE_PRIVATE));
                fcrear.close();
                Log.e("", "Creado XML");
            }
            catch (Exception exc)
            {
                Log.e("","Ni siquiera se puede crear");
            }
        }

    }



    //MENU ACTIONBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public int load() throws IOException
    {

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("Favoritos.xml")));
            String line;


            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');

                lineCount++;
            }
            Log.d("", "Lines: " + lineCount);

            return lineCount;

        }
        catch (IOException e) {
            Log.d("", "Ha saltado la excepcion de load");
            return 0;
        }

    }





    //GESTIONAR CLICK ACTIONBAR
    //GESTIONANDO EL CLICK DE LA ACTIONBAR

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_provincias:
                startActivity(new Intent(this, Provincias.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, Ajustes.class));
                return true;
            case R.id.actualizar:
                actualizar();

                return true;
            case R.id.prueba:
                startActivity(new Intent(this, MainFavoritos.class));

                return true;
            case R.id.action_acerca:
                startActivity(new Intent(this, About.class));
                return true;
       /*     case R.id.vista_cartas:
                startActivity(new Intent(this, CardsMainActivity.class));
                return true; */

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void borrardatos() {
        //final String PREFS_NAME = "MyPrefsFile";
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        if (settings.getBoolean("borradatos", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");

            try{
            OutputStreamWriter fout =
                    new OutputStreamWriter(
                            openFileOutput("Favoritos.xml", Context.MODE_PRIVATE));

            StringBuilder sb = new StringBuilder();

            Log.d("","Consigue crear el sb");

            sb.append("");
            Log.d("", "sb antes de append: " + sb.toString());
            //Escribimos el resultado a un fichero
            fout.append(sb.toString());

            fout.close();
            }
            catch (Exception ex)
            {
                Log.e("XmlTips", "Error al escribir fichero XML.");
            }

            settings.edit().putBoolean("borradatos", false).commit();
        }

    }


    public void firstTime() {
        //final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);


        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");

            mCardView.addCard(new MyCard("No tienes ninguna provincia seleccionada", "Entra al menú de Provincias y selecciona las que te interesen","Luego pulsa Actualizar", "En el menú de Ajustes también podrás configurar opciones tales como el filtrado por horas, configurar si quieres que al iniciar la app se actualice automáticamente, o la recepción de notificaciones", "En el apartado de Acerca de... podrás ver información adicional de la aplicación, como el desarrollador, la fuente de los datos, o la versión de la aplicación que tienes instalada"));
            mCardView.addCard(new MyCard("Para acceder a más detalles...", "Y entonces pasarás a ver todos los detalles de la incidencia", "En este apartado verás una información más detallada de la incidencia", "Contarás con información detallada, como el sentido, hacia donde circulan los coches que se encontrarán con la incidencia, la causa de la incidencia...", "Cuando pulses el botón de Actualizar, se volverán a mostrar las tarjetas que hayas descartado, por si las has descartado accidentalmente"));
            mCardView.addCardToLastStack(new MyCard( "Deslízame!", "Puedes deslizar las tarjetas hacia los laterales para descartarlas", "En la tarjeta frontal verás información resumida acerca de la incidencia", "El icono que verás te indicará, con su forma, el tipo de incidencia, y con el color, la gravedad de la situación","De menos a más, el orden es VERDE < AMARILLO < ROJO < NEGRO"));
            mCardView.addCard(new MyCard("Favoritos", "Pulsando el botón de Favoritos accederás a un nuevo menú donde podrás encontrar las incidencias en rutas que hayas marcado como favoritas", "Estos favoritos incluyen información sobre tus carreteras y tus provincias favoritas, mostrándote exactamente la información que tú quieres", "También sirven de base para, si así lo deseas, mostrarte notificaciones automáticas en caso de que la aplicación detecte que se ha producido una incidencia en tus rutas favoritas"));
            mCardView.refresh();



            // first time task

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }

    }


    //LOADINGTASK Y PARSEO
    class loadingTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {

            SAXHelper sh = null;
            try {
                sh = new SAXHelper(urls[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            sh.parseContent("");
            return "";

        }

        protected void onPostExecute(String s) {
            //   lv1.setAdapter(new EfficientAdapter(MainActivity.this, IncidenciaList));
            ShowProgress.dismiss();
            mCardView.refresh();
            Toast.makeText(getApplicationContext(), "Actualizado", Toast.LENGTH_LONG).show();

        }
    }



    class SAXHelper {
        public HashMap<String, String> userList = new HashMap<String, String>();
        private URL url2;

        public SAXHelper(String url1) throws MalformedURLException {
            this.url2 = new URL(url1);
        }

        public RSSHandler parseContent(String parseContent) {
            RSSHandler df = new RSSHandler();
            try {

                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();
                XMLReader xr = sp.getXMLReader();
                xr.setContentHandler(df);
                xr.parse(new InputSource(url2.openStream()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return df;
        }
    }

    public void actualizar() {
        //ELIMINAMOS LAS INCIDENCIAS EXISTENTES
        IncidenciaList.clear();
        mCardView.clearCards();

        //CARGAMOS NUEVAS INCIDENCIAS
        ShowProgress = ProgressDialog.show(MainActivity.this, "",
                "Cargando. Espere por favor...", true);
        new loadingTask().execute("http://www.dgt.es/incidenciasXY.xml");

        //REFRESCAR LA VISTA DE LAS CARDS
        mCardView.refresh();

    }


    public boolean checkTesteo() {
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(
                        MainActivity.this);

        if ( pref.getBoolean("testeo", false)) {
            // Log.i("", "Es true");
            return true;
        }
        else {
            //Log.i("", "Es false");
            return false;
        }


    }

    public boolean comparador(String fechahora){

        Log.d("","Iniciando comparador");

        //Obtenemos la fecha actual
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

        //Obtenemos la hora actual
        Calendar c = Calendar.getInstance();
        int minutes = c.get(Calendar.MINUTE);
        int hours = c.get(Calendar.HOUR_OF_DAY);

        //CREAMOS EL STRING YEAR, MONTH... PARA SACAR SOLO LOS PRIMEROS DIGITOS PARA INICIAR LA COMPARACION
        String year = new SimpleDateFormat("yyyy").format(cDate);
        String month = new SimpleDateFormat("MM").format(cDate);
        String day = new SimpleDateFormat("dd").format(cDate);

        //Obtener datos del argumento fechahora
        String yearpas = fechahora.substring(0,4); //Año
        String monthpas = fechahora.substring(5,7); //Mes
        String daypas = fechahora.substring(8,10); //Dia
        String fhs = fechahora.trim(); //Variable temporal para eliminar espacios al inicio y al final
        String horapas = fhs.substring(11,13); //Hora
        String minutospas = fhs.substring(14,16); //Minutos


        //Convertimos cada uno de los Strings a Ints

        int yearInt = Integer.parseInt(year);
        int monthInt = Integer.parseInt(month);
        int dayInt = Integer.parseInt(day);

        int horaInt = Integer.parseInt(horapas);
        int minutosInt = Integer.parseInt(minutospas);

        int yearpasInt = Integer.parseInt(yearpas);
        int monthpasInt = Integer.parseInt(monthpas);
        int daypasInt = Integer.parseInt(daypas);


        //Si la hora actual es entre las 00 y las 06, restaremos un dia y sumaremos 24 a las horas
        if (hours < 6){
                hours = hours+ 24;
                dayInt = dayInt -1;

            if(horaInt < 6){
                horaInt = horaInt + 24;
                daypasInt = daypasInt -1;
            }

        }

        //Si la hora de la notificación también es menor a las 6AM, restamos un dia y añadimos 24 a las horas

        //Empezamos comparacion de fecha

        //Comprobamos si coincide el año
        if (yearInt == yearpasInt){

            //Comprobamos si coincide el mes
            if (monthInt == monthpasInt){

                //Comprobamos si coincide el dia
                if (dayInt == daypasInt){

                    //Comprobamos si el filtrado horario esta habilitado

                    SharedPreferences cFilt = PreferenceManager.getDefaultSharedPreferences(this);

                    //En caso de que lo este
                    if (cFilt.getBoolean("filtrado_horario", false)) {

                        Log.d("Filtrado horario", "Activado");
                        Log.d("", "Hours:" + hours + "  horaInt: " + horaInt);

                        if (hours >= horaInt){

                            //Obtenemos la preferencia de hora_selec, que es el intervalo maximo deseado.
                            SharedPreferences sphora = PreferenceManager.getDefaultSharedPreferences(this);
                            String interv = sphora.getString("hora_selecc", "-1");
                            int intervInt = Integer.parseInt(interv);

                            Log.d("intervInt", ""+intervInt);

                            //Obtenemos la diferencia entre la hora atual y la de la incidencia
                            int dif = hours-horaInt;
                            Log.d("dif", ""+dif);

                            //Comparamos la diferencia con el intervalo maximo deseado
                            if (dif <= intervInt ) {

                                //Si la diferencia coincide con el intervalo, significa que la hora es la misma
                                //con lo que comprobaremos los minutos

                                if ((dif == intervInt ) && ((minutosInt - minutes) >= 0)) {
                                    // Log.i("", "Es true");
                                    return true;
                                }

                                //Si la diferencia no coincide, significa que han pasado menos horas que las que
                                //marca el intervalo, con lo que los minutos son indiferentes
                                else if (dif != intervInt ) {
                                    return true;
                                }

                                else {
                                    return false;
                                }
                            }
                            else {
                                return false;
                            }
                        }
                        else {
                            return false;
                        }
                    }
                    else {
                    Log.d("Filtrado horario", "Desactivado");
                    return true;
                    }
                }
            }
        }
        return false;
    }


    public boolean comparaFecha(String fechahora){

        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

        //CREAMOS EL STRING YEAR, MONTH... PARA SACAR SOLO LOS PRIMEROS DIGITOS PARA INICIAR LA COMPARACION
        String year = new SimpleDateFormat("yyyy").format(cDate);
        String month = new SimpleDateFormat("MM").format(cDate);
        String day = new SimpleDateFormat("dd").format(cDate);

        //   Log.i("", "year month day: " + year + " " + month + " " + day);

        String yearpas = fechahora.substring(0,4);
        String monthpas = fechahora.substring(5,7);
        String daypas = fechahora.substring(8,10);

        int dayInt = Integer.parseInt(day);
        int daypasInt = Integer.parseInt(daypas);

        //  Log.i("", "yearpas monthpas daypas: " + yearpas + " " + monthpas + " " + daypas);

        if (year.equals(yearpas)) {

            //  Log.i("", "COINCIDE YEAR");

            if (month.equals(monthpas)) {
               // Log.i("", "COINCIDE MONTH");
               // Log.d("", "dayInt: " + dayInt + "  daypasInt: " + daypasInt);
                if (dayInt == daypasInt) {
                    //    Log.i("", "COINCIDE DAY");
                    return true;
                }

                else if (dayInt == (daypasInt-1)){
                  //  Log.d("", "COINCIDE");
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else {
            //   Log.i("", "No coincide year");
            return false;
        }


    }

    public boolean checkHora(String hora){

        //Log.i("", "Hora: " + hora);

        Calendar c = Calendar.getInstance();
        int minutes = c.get(Calendar.MINUTE);
        int hours = c.get(Calendar.HOUR_OF_DAY);

        if (hours < 6){
            hours = hours+24;
        }

        Log.i("", "hora minuto: " + hours + "  " + minutes);

        String horatr = hora.trim();
        String hourpas = horatr.substring(11,13);
        String minutepas = horatr.substring(14,16);
        Log.i("", "hp: " + hourpas);
        Log.i("", "mp: " + minutepas);

        int hourInc = Integer.parseInt(hourpas);
        int minuteInc= Integer.parseInt(minutepas);

        if (hourInc < 6){
            hourInc = hourInc + 24 ;
        }

        if (hours >= hourInc) {
            SharedPreferences sphora = PreferenceManager.getDefaultSharedPreferences(this);
            String interv = sphora.getString("hora_selecc", "-1");

            Log.i("", "interv: " + interv);

            int intervInt = Integer.parseInt(interv);
            int dif = hours-hourInc;
            Log.i("", "dif: " + dif);

            if (dif <= intervInt ) {

                if ((dif == intervInt ) && ((minutes - minuteInc) >= 0)) {
                    // Log.i("", "Es true");
                    return true;
                }

                else if (dif != intervInt ) {
                    return true;
                }

                else {
                    return false;
                }

            }

            else {
                return false;
            }



        }

        else {
            return false;
        }
    }

    public boolean checkFiltrado(){
        SharedPreferences cFilt = PreferenceManager.getDefaultSharedPreferences(this);
        if (cFilt.getBoolean("filtrado_horario", false)) {
            return true;
        }
        return false;
    }





    public String getHora(String fechaHora) {

      //  Log.i("", "FechaHora: " + fechaHora);
        String fhs = fechaHora.trim();
        //Log.i("", "fhs: " + fhs);
        String hora = fhs.substring(11,13);
        //Log.i("", "hora: " + hora);
        String minutos = fhs.substring(14,16);
        //Log.i("", "minutos: " + minutos);
        Log.d("getHora","ok");
        return hora + ":" + minutos + "  ";

    }

    public int incIcono(String tipo, String nivel){

        if (tipo.equalsIgnoreCase("METEOROLOGICA")){

            if (nivel.equals("VERDE")){

                return R.drawable.meteo_verde;

            }

            if (nivel.equalsIgnoreCase("ROJO")){

                return R.drawable.meteo_rojo;

            }

            if (nivel.equalsIgnoreCase("AMARILLO")){

                return R.drawable.meteo_amarillo;

            }

            if (nivel.equalsIgnoreCase("NEGRO")){

                return R.drawable.meteo_negro;

            }



        }

        else if (tipo.equalsIgnoreCase("CONOS")){

            if (nivel.equals("VERDE")){

                return R.drawable.conos_verde;

            }

            if (nivel.equalsIgnoreCase("ROJO")){

                return R.drawable.conos_rojo;

            }

            if (nivel.equalsIgnoreCase("AMARILLO")){

                return R.drawable.conos_amarillo;

            }

            if (nivel.equalsIgnoreCase("NEGRO")){

                return R.drawable.conos_negro;

            }



        }

        else if (tipo.equalsIgnoreCase("RETENCION")){

            if (nivel.equals("VERDE")){

                return R.drawable.retencion_verde;

            }

            if (nivel.equalsIgnoreCase("ROJO")){

                return R.drawable.retencion_rojo;

            }

            if (nivel.equalsIgnoreCase("AMARILLO")){

                return R.drawable.retencion_amarillo;

            }

            if (nivel.equalsIgnoreCase("NEGRO")){

                return R.drawable.retencion_negro;

            }



        }

        else if (tipo.equalsIgnoreCase("CONOS")){

            if (nivel.equals("VERDE")){

                return R.drawable.conos_verde;

            }

            if (nivel.equalsIgnoreCase("ROJO")){

                return R.drawable.conos_rojo;

            }

            if (nivel.equalsIgnoreCase("AMARILLO")){

                return R.drawable.conos_amarillo;

            }

            if (nivel.equalsIgnoreCase("NEGRO")){

                return R.drawable.conos_negro;

            }



        }

        else if (tipo.equalsIgnoreCase("OBRAS")){

            if (nivel.equals("VERDE")){

                return R.drawable.obras_verde;

            }

            if (nivel.equalsIgnoreCase("ROJO")){

                return R.drawable.obras_rojo;

            }

            if (nivel.equalsIgnoreCase("AMARILLO")){

                return R.drawable.obras_amarillo;

            }

            if (nivel.equalsIgnoreCase("NEGRO")){

                return R.drawable.obras_negro;

            }



        }

        return R.drawable.conos_verde;

    }

    public boolean checkProvincia(String provincia) {

        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(
                        MainActivity.this);

        //   Log.i("", provincia + ": " + pref.getBoolean(provincia, false));

        if ( pref.getBoolean(provincia, false) == true) {
            //   Log.i("", "Es true");
            return true;
        }
        else {
            // Log.i("", "Es false");
            return false;
        }
    }


    class RSSHandler extends DefaultHandler {

        private Incidencia currentIncidencia = new Incidencia();
        StringBuffer chars = new StringBuffer();

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes atts) {

            chars = new StringBuffer();
            if (localName.equalsIgnoreCase("incidencia")) {

            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {

            if (localName.equalsIgnoreCase("tipo")
                    && currentIncidencia.getTipo() == null) {
                currentIncidencia.setTipo(chars.toString().trim());

            }
            if (localName.equalsIgnoreCase("autonomia")
                    && currentIncidencia.getAutonomia() == null) {
                currentIncidencia.setAutonomia(chars.toString().trim());

            }
            if (localName.equalsIgnoreCase("provincia")
                    && currentIncidencia.getProvincia() == null) {
                currentIncidencia.setProvincia(chars.toString().trim());

            }
            if (localName.equalsIgnoreCase("causa")
                    && currentIncidencia.getCausa() == null) {
                currentIncidencia.setCausa(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("poblacion")
                    && currentIncidencia.getPoblacion() == null) {
                currentIncidencia.setPoblacion(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("fechahora_ini")
                    && currentIncidencia.getFechahora() == null) {
                currentIncidencia.setFechahora(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("nivel")
                    && currentIncidencia.getNivel() == null) {
                currentIncidencia.setNivel(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("carretera")
                    && currentIncidencia.getCarretera() == null) {
                currentIncidencia.setCarretera(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("pk_inicial")
                    && currentIncidencia.getPkInicio() == null) {
                currentIncidencia.setPkInicio(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("pk_final")
                    && currentIncidencia.getPkFin() == null) {
                currentIncidencia.setPkFin(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("sentido")
                    && currentIncidencia.getSentido() == null) {
                currentIncidencia.setSentido(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("hacia")
                    && currentIncidencia.getHacia() == null) {
                currentIncidencia.setHacia(chars.toString().trim());
            }

            if (localName.equalsIgnoreCase("incidencia")) {




                // Log.i("", "Funciona: " + currentIncidencia.getProvincia());
                if (checkProvincia(currentIncidencia.getProvincia()) == true) {

                    if (checkTesteo() == false) {
                        //   Log.i("", "Pasado el primer if");
                        //if (comparaFecha(currentIncidencia.getFechahora().trim()) == true) {

                            //if (checkFiltrado()) {

                                if (comparador(currentIncidencia.getFechahora())) {
                                    Log.d("1 INFO", "Al menos no ha petado \n --------------------------------");
                                    //     Log.i("", "Añadida la provincia: " + currentIncidencia.getProvincia());
                                    IncidenciaList.add(currentIncidencia);

                                    mCardView.addCard(new MyCard(getHora(currentIncidencia.getFechahora()) + currentIncidencia.getCarretera() + "  -  " + currentIncidencia.getPoblacion(), "CAUSA: " + currentIncidencia.getCausa(), "KM INICIAL: " + currentIncidencia.getPkInicio() + "        KM FINAL: " + currentIncidencia.getPkFin(), "SENTIDO: " + currentIncidencia.getSentido(), "HACIA: " + currentIncidencia.getHacia()));
                                    mCardView.addCardToLastStack(new MyImageCard(currentIncidencia.getTipo() , incIcono(currentIncidencia.getTipo(), currentIncidencia.getNivel()), "KM INI: " + currentIncidencia.getPkInicio(),"KM FIN: " +  currentIncidencia.getPkFin(),"SENTIDO: " +  currentIncidencia.getSentido()));
                                }

                            //}

                            /*else {
                                Log.d("2 INFO", "Al menos no ha petado");
                                IncidenciaList.add(currentIncidencia);
                                mCardView.addCard(new MyCard(getHora(currentIncidencia.getFechahora()) + currentIncidencia.getCarretera() + "  -  " + currentIncidencia.getPoblacion(), "CAUSA: " + currentIncidencia.getCausa(), "KM INICIAL: " + currentIncidencia.getPkInicio() + "        KM FINAL: " + currentIncidencia.getPkFin(), "SENTIDO: " + currentIncidencia.getSentido(), "HACIA: " + currentIncidencia.getHacia()));
                                mCardView.addCardToLastStack(new MyImageCard(currentIncidencia.getTipo() , incIcono(currentIncidencia.getTipo(), currentIncidencia.getNivel()), "KM INI: " + currentIncidencia.getPkInicio(),"KM FIN: " +  currentIncidencia.getPkFin(),"SENTIDO: " +  currentIncidencia.getSentido()));
                            }*/

                       // }

                    }
                    else {
                        Log.d("3 INFO", "Al menos no ha petado");
                        IncidenciaList.add(currentIncidencia);
                        mCardView.addCard(new MyCard(getHora(currentIncidencia.getFechahora()) + currentIncidencia.getCarretera() + "  -  " + currentIncidencia.getPoblacion(), "CAUSA: " + currentIncidencia.getCausa(), "KM INICIAL: " + currentIncidencia.getPkInicio() + "        KM FINAL: " + currentIncidencia.getPkFin(), "SENTIDO: " + currentIncidencia.getSentido(), "HACIA: " + currentIncidencia.getHacia()));
                        mCardView.addCardToLastStack(new MyImageCard(currentIncidencia.getTipo() , incIcono(currentIncidencia.getTipo(), currentIncidencia.getNivel()), "KM INI: " + currentIncidencia.getPkInicio(),"KM FIN: " +  currentIncidencia.getPkFin(),"SENTIDO: " +  currentIncidencia.getSentido()));

                    }

                }
            currentIncidencia = new Incidencia();
        }
    }


        @Override
        public void characters(char ch[], int start, int length) {
            chars.append(new String(ch, start, length));
        }

    }




    public class AndroidParseXMLActivity {

        private void parseXML(String contenido) {

            String carretera;
            int pkI;
            int pkF;



            try {

                Log.w("AndroidParseXMLActivity", "Start");
                /** Handling XML */
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();
                XMLReader xr = sp.getXMLReader();

                ItemXMLHandler myXMLHandler = new ItemXMLHandler();
                xr.setContentHandler(myXMLHandler);
                InputSource inStream = new InputSource();
                Log.w("AndroidParseXMLActivity", "Parse1");


                inStream.setCharacterStream(new StringReader(contenido.toString()));
                Log.w("AndroidParseXMLActivity", "Parse2");

                xr.parse(inStream);
                Log.w("AndroidParseXMLActivity", "Parse3");


                Log.w("AndroidParseXMLActivity", "Done");
            }
            catch (Exception e) {
                Log.w("AndroidParseXMLActivity",e );
            }


        }

    }


}