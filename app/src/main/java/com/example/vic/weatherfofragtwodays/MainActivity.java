package com.example.vic.weatherfofragtwodays;


import android.os.AsyncTask;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

//import static com.example.vic.weatherforecasttwodays.R.drawable.w03n;


public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    // private ListView lvTomorrow,lvAfterTomorrow;
    public static String LOG_TAG = "JSON_result";
    DateFormat df = DateFormat.getDateTimeInstance();
    String cityUrl = "Vitoria-Gasteiz";
    String landUrl = "es";
    String apiId = "e25c9e1eb33eefc821749053b8257ae8";

    String jsonString;
    TextView textCity;
    private TextView textTomorrow, textTempTomorrow, textDescriptionTomorrow, textPressureTomorrow;
    private TextView textAfterTomorrow, textTempAfterTomorrow, textDescriptionAfterTomorrow, textPressureAfterTomorrow;
    private ImageView imageTomorrow, imageAfterTomorrow;
    private Layout fieldAfterTomorrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Quitar titulo
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        new GetDatosWeather().execute();
    }

    private class GetDatosWeather extends AsyncTask<Object, Object, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

         //   Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();


        }

        @Override
        protected String doInBackground(Object... params) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            //url de ciudad elegido:
            String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + cityUrl + ",%20" + landUrl + "&mode=json&appid=" + apiId + "&units=metric&lang=es&cnt=3";

            //datos recibidos:
            jsonString = sh.makeServiceCall(url);
            // выводим целиком полученную json-строку
            // Log.d(LOG_TAG, jsonStr);

            Log.e(TAG, "Response from url: " + jsonString);


            return jsonString;

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String jsonStr = result;
            String nameCity = "";


            if (jsonStr != null) {
                try {
                    JSONObject jsonDataGeneral = new JSONObject(jsonStr);
                    //  cod=jsonDataGeneral.getString("cod");
                    if (jsonDataGeneral.getInt("cod") != 200) {
                        Log.e(TAG, "Json parsing error: WEB no contesta");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: WEB no contesta",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    // Getting JSON Array node
                    JSONArray list = jsonDataGeneral.getJSONArray("list");
                    JSONObject city = jsonDataGeneral.getJSONObject("city");
                    nameCity = city.getString("name");
                    textCity = (TextView) findViewById(R.id.textCity);
                    textCity.setText(nameCity);


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            setWeatherData(jsonStr,1);
            setWeatherData(jsonStr,2);
        }


    }

    public    void setWeatherData(String result,int day ){
        String jsonStr=result;

        String tempDayText = "";
        String tempMinDay = "";
        String tempMaxDay = "";
        String descriptionDay = "";
        int weatherIdDay = 0;
        String pressureDay = "";
        String dateDay="";
        String iconString;
        int idIcon;

        if (jsonStr != null) {
            try {
                JSONObject jsonDataGeneral = new JSONObject(jsonStr);

                // Getting JSON Array node
                JSONArray list = jsonDataGeneral.getJSONArray("list");

                JSONObject listDay = list.getJSONObject(day);


                JSONObject tempDay = listDay.getJSONObject("temp");
                tempMinDay = String.format("%.0f", tempDay.getDouble("min")) + "°";
                tempMaxDay = String.format("%.0f", tempDay.getDouble("max")) + "°";
                //tempDayText = "min. " + tempMinDay + " - max. " + tempMaxDay;

                JSONArray weatherDay = listDay.getJSONArray("weather");

                JSONObject weatherOfDay = weatherDay.getJSONObject(0);

                descriptionDay = weatherOfDay.getString("description");
                pressureDay = String.format("Presión: %.1f", listDay.getDouble("pressure")) + " hPa";
                dateDay = df.format(new Date(listDay.getLong("dt")*1000));

                weatherIdDay = weatherOfDay.getInt("id");

            } catch (final JSONException e) {
          //      Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        } else {
           Log.e(TAG, "Couldn't get json from server.");
           runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG).show();
                }
            });
        }

        iconString = setWeatherIcon(weatherIdDay);

        idIcon = getResources().getIdentifier(iconString, "drawable", getPackageName());

        callFragments(day,idIcon,tempMinDay,tempMaxDay,descriptionDay, pressureDay,dateDay);

    }
public  void callFragments(int day, int idIcon,String tempMinDay,String tempMaxDay,String descriptionDay,
                           String pressureDay, String dateDay){


    if (day==1){
        FragmentManager fragmentManager = getFragmentManager();

    // Получаем ссылку на второй фрагмент по ID
    FragmentTomorrow fragmentTomorrow = (FragmentTomorrow) fragmentManager.findFragmentById(R.id.fragment_tomorrow);
    // Выводим нужную информацию
    if (fragmentTomorrow != null){

           fragmentTomorrow.setDescription(idIcon,tempMinDay,tempMaxDay,descriptionDay,pressureDay,dateDay);
                }
    }

    if (day==2){
       FragmentManager fragmentManagerAfter = getFragmentManager();
         //fragmentManager = getSupportFragmentManager();
        // Получаем ссылку на второй фрагмент по ID
        FragmentAfterTomorrow fragmentAfterTomorrow = (FragmentAfterTomorrow) fragmentManagerAfter.findFragmentById(R.id.fragment_after_tomorrow);
        // Выводим нужную информацию
        if (fragmentAfterTomorrow != null){

            fragmentAfterTomorrow.setDescription(idIcon,tempMinDay,tempMaxDay,descriptionDay,pressureDay,dateDay);
        }
    }

}
    public static String setWeatherIcon(int weatherId) {
  //      int id = (weatherId / 100);
        String icon = "";
        if ((199<weatherId)&(weatherId<233) ) icon = "w11d";
        else if ((299<weatherId)&(weatherId<311) ) icon = "w05d";
        else if ((310<weatherId)&(weatherId<322) ) icon = "w06d";
        else if ((499<weatherId)&(weatherId<522) ) icon = "w08d";
        else if ((522<weatherId)&(weatherId<532) ) icon = "w09d";
        else if ((599<weatherId)&(weatherId<650) ) icon = "w13d";
        else if ((700<weatherId)&(weatherId<799) ) icon = "w50d";
        else if (800==weatherId) icon = "w01d";
        else if (801==weatherId) icon = "w02d";
        else if (802==weatherId) icon = "w03d";
        else if ((803==weatherId)&(804==weatherId)) icon = "w04d";

        // int icon;
/*
        switch(id){
            case 2:
                icon = "w11d";
                break;
            case 3:
                icon = "w09d"; //"&#xf01c;";
                break;
            case 7:
                icon = "w50d"; //"&#xf014;";
                break;
            case 8:
                icon = "w02d"; //"&#xf013;";
                break;
            case 6:
                icon = "w13d"; //"&#xf01b;";
                break;
            case 5:
                icon = "w10d"; //"&#xf019;";
                break;

        } */
        return icon;

    }

}