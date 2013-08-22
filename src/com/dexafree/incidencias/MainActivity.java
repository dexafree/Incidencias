package com.dexafree.incidencias;

import android.content.Context;
import android.content.SharedPreferences;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.app.Activity;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

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

    public final static String XCOORD = "com.dexafree.incidencias.XCOORD";
    public final static String YCOORD = "com.dexafree.incidencias.YCOORD";

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
            actualizar();
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

            int lines = load();

            for(int i=0; i<lines; i++){
                texto = fin.readLine();
                AndroidParseXMLActivity axa = new AndroidParseXMLActivity();
                axa.parseXML(texto);
            }
            fin.close();
        }
        catch (Exception ex)
        {
            try{
                OutputStreamWriter fcrear =
                        new OutputStreamWriter(
                                openFileOutput("Favoritos.xml", Context.MODE_PRIVATE));
                fcrear.close();
            }
            catch (Exception exc){
            }
        }

        new Utils(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        new Evento("Iniciado");

        new WhatsNewScreen(this).show();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("DEXA", "Esto al menos si");

        SharedPreferences shapre =
                PreferenceManager.getDefaultSharedPreferences(
                        MainActivity.this);

        Log.d("KEY", ""+shapre.getBoolean("sendstats", false));

        if ( shapre.getBoolean("sendstats", true)) {
            DevMenu.enviar();
            Log.d("estadisticas", "enviadas");
        }



    }

    //MENU ACTIONBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

            return lineCount;
        }
        catch (IOException e) {
            return 0;
        }
    }

    //GESTIONAR CLICK ACTIONBAR
    //GESTIONANDO EL CLICK DE LA ACTIONBAR

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_provincias:
                new Evento("Provincias");
                startActivity(new Intent(this, Provincias.class));
                return true;
            case R.id.action_settings:
                new Evento("Ajustes");
                startActivity(new Intent(this, Ajustes.class));
                return true;
            case R.id.actualizar:
                new Evento("Actualizar");
                actualizar();
                return true;
            case R.id.prueba:
                new Evento("Favoritos");
                startActivity(new Intent(this, MainFavoritos.class));
                return true;
            case R.id.action_acerca:
                new Evento("Acerca de");
                startActivity(new Intent(this, About.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void borrardatos() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        if (settings.getBoolean("borradatos", true)) {
            try{
                OutputStreamWriter fout =
                        new OutputStreamWriter(
                                openFileOutput("Favoritos.xml", Context.MODE_PRIVATE));

                StringBuilder sb = new StringBuilder();
                sb.append("");
                //Escribimos el resultado a un fichero
                fout.append(sb.toString());
                fout.close();
            }
            catch (Exception ex){
            }
            settings.edit().putBoolean("borradatos", false).commit();
        }
    }


    public void firstTime() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        if (settings.getBoolean("my_first_time", true)) {
            mCardView.addCard(new MyCard("No tienes ninguna provincia seleccionada", "Entra al menú de Provincias y selecciona las que te interesen","Luego pulsa Actualizar", "En el menú de Ajustes también podrás configurar opciones tales como el filtrado por horas, configurar si quieres que al iniciar la app se actualice automáticamente, o la recepción de notificaciones", "En el apartado de Acerca de... podrás ver información adicional de la aplicación, como el desarrollador, la fuente de los datos, o la versión de la aplicación que tienes instalada"));
            mCardView.addCard(new MyCard("Para acceder a más detalles...", "Y entonces pasarás a ver todos los detalles de la incidencia", "En este apartado verás una información más detallada de la incidencia", "Contarás con información detallada, como el sentido, hacia donde circulan los coches que se encontrarán con la incidencia, la causa de la incidencia...", "Cuando pulses el botón de Actualizar, se volverán a mostrar las tarjetas que hayas descartado, por si las has descartado accidentalmente"));
            mCardView.addCardToLastStack(new MyCard( "Deslízame!", "Puedes deslizar las tarjetas hacia los laterales para descartarlas", "En la tarjeta frontal verás información resumida acerca de la incidencia", "El icono que verás te indicará, con su forma, el tipo de incidencia, y con el color, la gravedad de la situación","De menos a más, el orden es VERDE < AMARILLO < ROJO < NEGRO"));
            mCardView.addCard(new MyCard("Favoritos", "Pulsando el botón de Favoritos accederás a un nuevo menú donde podrás encontrar las incidencias en rutas que hayas marcado como favoritas", "Estos favoritos incluyen información sobre tus carreteras y tus provincias favoritas, mostrándote exactamente la información que tú quieres", "También sirven de base para, si así lo deseas, mostrarte notificaciones automáticas en caso de que la aplicación detecte que se ha producido una incidencia en tus rutas favoritas"));
            mCardView.refresh();
            settings.edit().putBoolean("my_first_time", false).commit();
        }
    }



    public boolean provsEsp(){

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        if (settings.getBoolean("A CORUÑA", true)){
            return true;
        }
        if (settings.getBoolean("ALBACETE", true)){
            return true;
        }
        if (settings.getBoolean("ALICANTE/ALACANT", true)){
            return true;
        }
        if (settings.getBoolean("ALMERIA", true)){
            return true;
        }
        if (settings.getBoolean("ASTURIAS", true)){
            return true;
        }
        if (settings.getBoolean("AVILA", true)){
            return true;
        }
        if (settings.getBoolean("BADAJOZ", true)){
            return true;
        }
        if (settings.getBoolean("BARCELONA", true)){
            return true;
        }
        if (settings.getBoolean("BURGOS", true)){
            return true;
        }
        if (settings.getBoolean("CACERES", true)){
            return true;
        }
        if (settings.getBoolean("CADIZ", true)){
            return true;
        }
        if (settings.getBoolean("CANTABRIA", true)){
            return true;
        }
        if (settings.getBoolean("CASTELLON/CASTELLO", true)){
            return true;
        }
        if (settings.getBoolean("CIUDAD REAL", true)){
            return true;
        }
        if (settings.getBoolean("CORDOBA", true)){
            return true;
        }
        if (settings.getBoolean("CUENCA", true)){
            return true;
        }
        if (settings.getBoolean("GERONA", true)){
            return true;
        }
        if (settings.getBoolean("GRANADA", true)){
            return true;
        }
        if (settings.getBoolean("GUADALAJARA", true)){
            return true;
        }
        if (settings.getBoolean("HUELVA", true)){
            return true;
        }
        if (settings.getBoolean("HUESCA", true)){
            return true;
        }
        if (settings.getBoolean("ILLES BALEARS", true)){
            return true;
        }
        if (settings.getBoolean("JAEN", true)){
            return true;
        }
        if (settings.getBoolean("LA RIOJA", true)){
            return true;
        }
        if (settings.getBoolean("LAS PALMAS", true)){
            return true;
        }
        if (settings.getBoolean("LEON", true)){
            return true;
        }
        if (settings.getBoolean("LLEIDA", true)){
            return true;
        }
        if (settings.getBoolean("LUGO", true)){
            return true;
        }
        if (settings.getBoolean("MADRID", true)){
            return true;
        }
        if (settings.getBoolean("MALAGA", true)){
            return true;
        }
        if (settings.getBoolean("MURCIA", true)){
            return true;
        }
        if (settings.getBoolean("NAVARRA", true)){
            return true;
        }
        if (settings.getBoolean("OURENSE", true)){
            return true;
        }
        if (settings.getBoolean("PALENCIA", true)){
            return true;
        }
        if (settings.getBoolean("PONTEVEDRA", true)){
            return true;
        }
        if (settings.getBoolean("SALAMANCA", true)){
            return true;
        }
        if (settings.getBoolean("SANTA CRUZ DE TENERIFE", true)){
            return true;
        }
        if (settings.getBoolean("SEGOVIA", true)){
            return true;
        }
        if (settings.getBoolean("SEVILLA", true)){
            return true;
        }
        if (settings.getBoolean("SORIA", true)){
            return true;
        }
        if (settings.getBoolean("TARRAGONA", true)){
            return true;
        }
        if (settings.getBoolean("TERUEL", true)){
            return true;
        }
        if (settings.getBoolean("TOLEDO", true)){
            return true;
        }
        if (settings.getBoolean("VALENCIA", true)){
            return true;
        }
        if (settings.getBoolean("VALLADOLID", true)){
            return true;
        }
        if (settings.getBoolean("ZAMORA", true)){
            return true;
        }
        if (settings.getBoolean("ZARAGOZA", true)){
            return true;
        }
        return false;
    }



    //LOADINGTASK Y PARSEO
    class loadingTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            SAXHelper sh = null;
            int i = urls.length;

            for (int j = 0;j<i; j++){
                try {
                    sh = new SAXHelper(urls[j]);
                    Log.d("URLS", urls[j]);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                sh.parseContent("");
            }
            return "";
        }

        protected void onPostExecute(String s) {

            if(IncidenciaList.size() == 0){
                mCardView.addCard(new MyCard("No hay incidencias a mostrar!", "Circule con cuidado","No olvide abrocharse el cinturón", "No utilice la aplicación mientras conduce", "Pare a descansar cada 2 horas de conducción"));
            }
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
                InputSource is = new InputSource(url2.openStream());
                if(url2.toString().equalsIgnoreCase("http://www.dexa-dev.es/incidencias/InciVascP.xml")){
                    is.setEncoding("ISO-8859-1");
                }
                xr.parse(is);
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




        SharedPreferences pm =
                PreferenceManager.getDefaultSharedPreferences(
                        MainActivity.this);

        if((pm.getBoolean("VI", true) == false && pm.getBoolean("BI", true) == false && pm.getBoolean("SS", true) == false ) && provsEsp() == false){
            MyCard aviso = new MyCard("No tienes ninguna provincia seleccionada!", "En el Menú, accede al apartado provincias y selecciona de qué provincias quieres conocer el estado del tráfico");
            mCardView.addCard(aviso);
        }
        else if((pm.getBoolean("VI", true) || pm.getBoolean("BI", true) || pm.getBoolean("SS", true)) && provsEsp() ){
            //CARGAMOS NUEVAS INCIDENCIAS
            ShowProgress = ProgressDialog.show(MainActivity.this, "",
                    "Cargando. Espere por favor...", true);
            new loadingTask().execute("http://www.dexa-dev.es/incidencias/InciDGT.xml", "http://www.dexa-dev.es/incidencias/InciVascP.xml");
        }
        else if((pm.getBoolean("VI", true) || pm.getBoolean("BI", true) || pm.getBoolean("SS", true) ) && provsEsp() == false){
            //CARGAMOS NUEVAS INCIDENCIAS
            ShowProgress = ProgressDialog.show(MainActivity.this, "",
                    "Cargando. Espere por favor...", true);
            new loadingTask().execute("http://www.dexa-dev.es/incidencias/InciVascP.xml");
        }
        else if((pm.getBoolean("VI", true) == false && pm.getBoolean("BI", true) == false && pm.getBoolean("SS", true) == false ) && provsEsp()){
            ShowProgress = ProgressDialog.show(MainActivity.this, "",
                    "Cargando. Espere por favor...", true);
            new loadingTask().execute("http://www.dexa-dev.es/incidencias/InciDGT.xml");
        }

        //REFRESCAR LA VISTA DE LAS CARDS
        mCardView.refresh();

    }


    public boolean checkTesteo() {
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(
                        MainActivity.this);

        if ( pref.getBoolean("testeo", false)) {
            return true;
        }
        else {
            return false;
        }


    }

    public boolean comparador(String fechahora){

        String checker = fechahora.substring(11,12);

        if (checker.equals("-")){
            return false;
        }

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

                        if (hours >= horaInt){

                            //Obtenemos la preferencia de hora_selec, que es el intervalo maximo deseado.
                            SharedPreferences sphora = PreferenceManager.getDefaultSharedPreferences(this);
                            String interv = sphora.getString("hora_selecc", "-1");
                            int intervInt = Integer.parseInt(interv);

                            //Obtenemos la diferencia entre la hora atual y la de la incidencia
                            int dif = hours-horaInt;
                            //Log.d("dif", ""+dif);

                            //Comparamos la diferencia con el intervalo maximo deseado
                            if (dif <= intervInt ) {

                                //Si la diferencia coincide con el intervalo, significa que la hora es la misma
                                //con lo que comprobaremos los minutos

                                if ((dif == intervInt ) && ((minutosInt - minutes) >= 0)) {
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
                        //Log.d("Filtrado horario", "Desactivado");
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

        String yearpas = fechahora.substring(0,4);
        String monthpas = fechahora.substring(5,7);
        String daypas = fechahora.substring(8,10);

        int dayInt = Integer.parseInt(day);
        int daypasInt = Integer.parseInt(daypas);

        if (year.equals(yearpas)) {

            if (month.equals(monthpas)) {
                if (dayInt == daypasInt) {
                    return true;
                }

                else if (dayInt == (daypasInt-1)){
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

    public boolean checkHora(String hora){

        Calendar c = Calendar.getInstance();
        int minutes = c.get(Calendar.MINUTE);
        int hours = c.get(Calendar.HOUR_OF_DAY);

        if (hours < 6){
            hours = hours+24;
        }

        String horatr = hora.trim();
        String hourpas = horatr.substring(11,13);
        String minutepas = horatr.substring(14,16);

        int hourInc = Integer.parseInt(hourpas);
        int minuteInc= Integer.parseInt(minutepas);

        if (hourInc < 6){
            hourInc = hourInc + 24 ;
        }

        if (hours >= hourInc) {
            SharedPreferences sphora = PreferenceManager.getDefaultSharedPreferences(this);
            String interv = sphora.getString("hora_selecc", "-1");

            int intervInt = Integer.parseInt(interv);
            int dif = hours-hourInc;
            if (dif <= intervInt ) {

                if ((dif == intervInt ) && ((minutes - minuteInc) >= 0)) {
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
        String fhs = fechaHora.trim();
        String hora = fhs.substring(11,13);
        String minutos = fhs.substring(14,16);

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

        if ( pref.getBoolean(provincia, false) == true) {
            return true;
        }
        else {
            return false;
        }
    }

    public void creaCard(Incidencia currentIncidencia){

        final double x = currentIncidencia.getX();
        final double y = currentIncidencia.getY();


        if(x!= 0.0){
            MyCardMap card = new MyCardMap(getHora(currentIncidencia.getFechahora()) + currentIncidencia.getCarretera() + "  -  " + currentIncidencia.getPoblacion(), "CAUSA: " + currentIncidencia.getCausa(), "KM INICIAL: " + currentIncidencia.getPkInicio() + "        KM FINAL: " + currentIncidencia.getPkFin(), "SENTIDO: " + currentIncidencia.getSentido(), "HACIA: " + currentIncidencia.getHacia(), currentIncidencia.getX(), currentIncidencia.getY());
            card.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    new Evento("Mapa");
                    Context context = getApplicationContext();
                    Intent intent = new Intent(context, MapActivity.class);
                    intent.putExtra(XCOORD,x);
                    intent.putExtra(YCOORD,y);
                    startActivity(intent);

                }
            });

            mCardView.addCard(card);
        }

        else{

            MyCard card = new MyCard(getHora(currentIncidencia.getFechahora()) + currentIncidencia.getCarretera() + "  -  " + currentIncidencia.getPoblacion(), "CAUSA: " + currentIncidencia.getCausa(), "KM INICIAL: " + currentIncidencia.getPkInicio() + "        KM FINAL: " + currentIncidencia.getPkFin(), "SENTIDO: " + currentIncidencia.getSentido(), "HACIA: " + currentIncidencia.getHacia(), currentIncidencia.getX(), currentIncidencia.getY());

            mCardView.addCard(card);

        }




        MyImageCard imCard = new MyImageCard(currentIncidencia.getTipo() , incIcono(currentIncidencia.getTipo(), currentIncidencia.getNivel()), "KM INI: " + currentIncidencia.getPkInicio(),"KM FIN: " +  currentIncidencia.getPkFin(),"SENTIDO: " +  currentIncidencia.getSentido());

        if(x!= 0.0){
            imCard.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    new Evento("Mapa");
                    Context context = getApplicationContext();
                    Intent intent = new Intent(context, MapActivity.class);
                    intent.putExtra(XCOORD,x);
                    intent.putExtra(YCOORD,y);
                    startActivity(intent);

                }
            });
        }

        mCardView.addCardToLastStack(imCard);
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
            if (localName.equalsIgnoreCase("matricula")
                    && currentIncidencia.getMatricula() == null) {
                currentIncidencia.setMatricula(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("carretera")
                    && currentIncidencia.getCarretera() == null) {
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
            if (localName.equalsIgnoreCase("x")
                    && currentIncidencia.getX() == 0.0f) {
                String xTemp = chars.toString().trim();
                if (xTemp != ""){
                    double x = Double.parseDouble(xTemp);
                    currentIncidencia.setX(x);
                }
            }
            if (localName.equalsIgnoreCase("y")
                    && currentIncidencia.getY() == 0.0f) {
                String yTemp = chars.toString().trim();
                if (yTemp != ""){
                    double y = Double.parseDouble(yTemp);
                    currentIncidencia.setY(y);
                }
            }

            if (localName.equalsIgnoreCase("incidencia")) {

                if (checkProvincia(currentIncidencia.getProvincia()) == true) {

                    if (checkTesteo() == false) {

                        if (comparador(currentIncidencia.getFechahora())) {

                            IncidenciaList.add(currentIncidencia);

                            creaCard(currentIncidencia);
                        }
                    }
                    else {
                        IncidenciaList.add(currentIncidencia);
                        creaCard(currentIncidencia);
                    }
                }

                else if (checkProvincia(currentIncidencia.getMatricula()) == true){
                    if (checkTesteo() == false) {

                        if (comparador(currentIncidencia.getFechahora())) {
                            IncidenciaList.add(currentIncidencia);
                            creaCard(currentIncidencia);
                        }
                    }

                    else {
                        //Log.d("3 INFO", "Al menos no ha petado");
                        IncidenciaList.add(currentIncidencia);
                        creaCard(currentIncidencia);
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