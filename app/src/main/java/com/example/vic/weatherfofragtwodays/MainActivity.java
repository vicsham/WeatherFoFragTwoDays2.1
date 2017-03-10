package com.example.vic.weatherfofragtwodays;


import android.os.AsyncTask;
import android.app.FragmentManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    // private ListView lvTomorrow,lvAfterTomorrow;
    public static String LOG_TAG = "JSON_result";
    DateFormat df = DateFormat.getDateTimeInstance();
    String url;
    String cityUrl0 = "Vitoria-Gasteiz";
    String cityUrl1 = "Bilbao";
    String cityUrl2 = "Pamplona";
    //String cityUrl2 = "San Sebastian";
    String landUrl = "es";
    String apiId = "e25c9e1eb33eefc821749053b8257ae8";
    String urlCity0 = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + cityUrl0 + ",%20" + landUrl + "&mode=json&appid=" + apiId + "&units=metric&lang=es&cnt=3";
    String urlCity1 = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + cityUrl1 + ",%20" + landUrl + "&mode=json&appid=" + apiId + "&units=metric&lang=es&cnt=3";
    String urlCity2 = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + cityUrl2 + ",%20" + landUrl + "&mode=json&appid=" + apiId + "&units=metric&lang=es&cnt=3";
    //San Sebastian hecho por Id
    //String urlCity2 = "http://api.openweathermap.org/data/2.5/forecast/daily?id=" + 3110044 + ",%20" + landUrl + "&mode=json&appid=" + apiId + "&units=metric&lang=es&cnt=3";
   // String urlCity2 = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + cityUrl2 + ",%20" + landUrl + "&mode=json&appid=" + apiId + "&units=metric&lang=es&cnt=3";

    String nameCity1="Bilbao";
    String currentCity="";
    String jsonString;
    int cityNumber=0;
    TextView textCity;
    private int cityCount=0;
    private int totalCity=3;
    private TextView textTomorrow, textTempTomorrow, textDescriptionTomorrow, textPressureTomorrow;
    private TextView textAfterTomorrow, textTempAfterTomorrow, textDescriptionAfterTomorrow, textPressureAfterTomorrow;
    private ImageView imageTomorrow, imageAfterTomorrow;
    private Layout fieldAfterTomorrow;
    boolean dataCargada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Quitar titulo
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

      //  new GetDatosWeather(0).execute();
      //  new GetDatosWeather(1).execute();


        Handler handler = new Handler();
        dataCargada=false;

        cityNumber=0;
        currentCity="Vitoria-Gasteiz";
        url=urlCity0;

       new GetDatosWeather().execute();


        handler.postDelayed(new Runnable() {
            public void run() {
                cityNumber=1;
                currentCity="Bilbao-Bilbo";
                url=urlCity1;
                new GetDatosWeather().execute();
            }
        }, 40);


        handler.postDelayed(new Runnable() {
            public void run() {
                cityNumber=2;
                currentCity="Pamplona";
               // currentCity="San Sebastián-Donstia";
                url=urlCity2;
                new GetDatosWeather().execute();
                dataCargada=true;
            }
        }, 80);

        handler.postDelayed(new Runnable() {
            public void run() {
                cityNumber=2;
                if (dataCargada)  weatherAnimation();
            }
        }, 900);


    }



    private class GetDatosWeather extends AsyncTask<Object, Object, String> {




        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();


        }

        @Override
        protected String doInBackground(Object... params) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            //url de ciudad elegido:


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
                    //nameCity = city.getString("name");
                    textCity = (TextView) findViewById(R.id.textCity);
                   // textCity.setText(nameCity);
                    textCity.setText(currentCity);



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
            //setWeatherData(jsonStr,0); //hoy
            setWeatherData(jsonStr,1);  //mañana
            setWeatherData(jsonStr,2);  //pasado mañana
        }


    }

    public    void setWeatherData(String result,int day){
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

        callFragments(day,cityNumber,idIcon,tempMinDay,tempMaxDay,descriptionDay, pressureDay,dateDay);

    }
public  void callFragments(int day,int cityNumber, int idIcon,String tempMinDay,String tempMaxDay,String descriptionDay,
                           String pressureDay, String dateDay){
    FragmentManager fragmentManager = getFragmentManager();

    if (day==1) {

     /*

        FragmentTomorrow fragmentTomorrow = (FragmentTomorrow) fragmentManager.findFragmentById(R.id.fragment_tomorrow);
        // Выводим нужную информацию
        if (fragmentTomorrow != null) {

            fragmentTomorrow.setDescription(idIcon, tempMinDay, tempMaxDay, descriptionDay, pressureDay, dateDay);
        }
    */
        switch (cityNumber) {
            case 0:
                FragmentTomorrow fragmentTomorrow = (FragmentTomorrow) fragmentManager.findFragmentById(R.id.fragment_tomorrow);
                // Выводим нужную информацию
                if (fragmentTomorrow != null) {

                    fragmentTomorrow.setDescription(idIcon, tempMinDay, tempMaxDay, descriptionDay, pressureDay, dateDay);
                }

                break;

            case 1:


                FragmentTomorrow fragmentTomorrow1 = (FragmentTomorrow) fragmentManager.findFragmentById(R.id.fragment_tomorrow1);
                // Выводим нужную информацию
                if (fragmentTomorrow1 != null) {

                    fragmentTomorrow1.setDescription(idIcon, tempMinDay, tempMaxDay, descriptionDay, pressureDay, dateDay);
                }
                break;
            case 2:

                FragmentTomorrow fragmentTomorrow2 = (FragmentTomorrow) fragmentManager.findFragmentById(R.id.fragment_tomorrow2);
                // Выводим нужную информацию
                if (fragmentTomorrow2 != null) {

                    fragmentTomorrow2.setDescription(idIcon, tempMinDay, tempMaxDay, descriptionDay, pressureDay, dateDay);
                }

                break;

            default:
                break;
        }


    }

    if (day==2){

        switch (cityNumber) {
            case 0:
                FragmentAfterTomorrow fragmentAfterTomorrow = (FragmentAfterTomorrow) fragmentManager.findFragmentById(R.id.fragment_after_tomorrow);
                // Выводим нужную информацию
                if (fragmentAfterTomorrow != null) {

                    fragmentAfterTomorrow.setDescription(idIcon, tempMinDay, tempMaxDay, descriptionDay, pressureDay, dateDay);
                      Toast.makeText(MainActivity.this, "Pasado Call", Toast.LENGTH_LONG).show();
                }

                break;

            case 1:
                FragmentAfterTomorrow fragmentAfterTomorrow1 = (FragmentAfterTomorrow) fragmentManager.findFragmentById(R.id.fragment_after_tomorrow1);
                // Выводим нужную информацию
                if (fragmentAfterTomorrow1 != null) {

                    fragmentAfterTomorrow1.setDescription(idIcon, tempMinDay, tempMaxDay, descriptionDay, pressureDay, dateDay);
                }
                break;
            case 2:

                FragmentAfterTomorrow fragmentAfterTomorrow2 = (FragmentAfterTomorrow) fragmentManager.findFragmentById(R.id.fragment_after_tomorrow2);
                // Выводим нужную информацию
                if (fragmentAfterTomorrow2 != null) {

                    fragmentAfterTomorrow2.setDescription(idIcon, tempMinDay, tempMaxDay, descriptionDay, pressureDay, dateDay);
                }

                break;

            default:
                break;
        }


      /*
       FragmentManager fragmentManagerAfter = getFragmentManager();
         //fragmentManager = getSupportFragmentManager();
        // Получаем ссылку на второй фрагмент по ID
        FragmentAfterTomorrow fragmentAfterTomorrow = (FragmentAfterTomorrow) fragmentManagerAfter.findFragmentById(R.id.fragment_after_tomorrow);
        // Выводим нужную информацию
        if (fragmentAfterTomorrow != null){

            fragmentAfterTomorrow.setDescription(idIcon,tempMinDay,tempMaxDay,descriptionDay,pressureDay,dateDay);
        }
        */
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

        return icon;

    }


    private void weatherAnimation(){

         Handler handlerShow = new Handler();

        FrameLayout frameLayoutTomorrow = (FrameLayout) findViewById(R.id.containerTomorrow);
        assert frameLayoutTomorrow != null;
          FrameLayout frameLayoutAfterTomorrow = (FrameLayout) findViewById(R.id.containerTomorrow);
           assert frameLayoutAfterTomorrow != null;

        getFragmentManager()
                .beginTransaction()
                .add(R.id.containerTomorrow,new FragmentTomorrow())
                .commit();

        getFragmentManager()
                .beginTransaction()
                .add(R.id.containerAfterTomorrow,new FragmentAfterTomorrow())
                .commit();
        textCity.setText(cityUrl0);

        handlerShow.postDelayed(new Runnable() {
            public void run() {
                textCity.setText(cityUrl1);
                flipTomorrow();
                flipAfterTomorrow();

            }
        }, 4000);

        handlerShow.postDelayed(new Runnable() {
            public void run() {
                textCity.setText(cityUrl2);
                flipTomorrow();
                flipAfterTomorrow();

            }
        }, 8000);
    }
    private void flipTomorrow(){


        switch(cityCount) {
            case 0:
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.animator.card_flip_right_enter,
                                R.animator.card_flip_right_exit,
                                R.animator.card_flip_left_enter,
                                R.animator.card_flip_left_exit)
                        .replace(R.id.containerTomorrow, new
                                FragmentTomorrow1())
                        .addToBackStack(null)
                        .commit();
                cityCount++;
                if(cityCount==totalCity)cityCount=0;
                break;
            case 1:
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.animator.card_flip_right_enter,
                                R.animator.card_flip_right_exit,
                                R.animator.card_flip_left_enter,
                                R.animator.card_flip_left_exit)
                        .replace(R.id.containerTomorrow, new
                                FragmentTomorrow2())
                        .addToBackStack(null)
                        .commit();
                cityCount++;
                if(cityCount==totalCity)cityCount=0;
                break;
            case 2:


                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.animator.card_flip_right_enter,
                                R.animator.card_flip_right_exit,
                                R.animator.card_flip_left_enter,
                                R.animator.card_flip_left_exit)
                        .replace(R.id.containerTomorrow, new
                                FragmentTomorrow())
                        .addToBackStack(null)
                        .commit();
                cityCount++;
                if(cityCount==totalCity)cityCount=0;
                break;

        }


    }

    private void flipAfterTomorrow(){


        switch(cityCount) {
            case 0:
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.animator.card_flip_right_enter,
                                R.animator.card_flip_right_exit,
                                R.animator.card_flip_left_enter,
                                R.animator.card_flip_left_exit)
                        .replace(R.id.containerAfterTomorrow, new
                                FragmentAfterTomorrow1())
                        .addToBackStack(null)
                        .commit();
                cityCount++;
                if(cityCount==totalCity)cityCount=0;
                break;
            case 1:
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.animator.card_flip_right_enter,
                                R.animator.card_flip_right_exit,
                                R.animator.card_flip_left_enter,
                                R.animator.card_flip_left_exit)
                        .replace(R.id.containerAfterTomorrow, new
                                FragmentAfterTomorrow2())
                        .addToBackStack(null)
                        .commit();
                cityCount++;
                if(cityCount==totalCity)cityCount=0;
                break;
            case 2:

                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.animator.card_flip_right_enter,
                                R.animator.card_flip_right_exit,
                                R.animator.card_flip_left_enter,
                                R.animator.card_flip_left_exit)
                        .replace(R.id.containerAfterTomorrow, new
                                FragmentTomorrow())
                        .addToBackStack(null)
                        .commit();
                cityCount++;
                if(cityCount==totalCity)cityCount=0;
                break;

        }


    }

}