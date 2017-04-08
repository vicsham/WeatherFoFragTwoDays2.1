package com.example.vic.weatherfofragtwodays;


import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.app.FragmentManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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

    String nameCity0 = "Vitoria-Gasteiz";
    String nameCity1 = "Bilbao-Bilbo";
    String nameCity2 = "Pamplona-Iruña";
    String currentCity = "";
    String jsonString;
    String iconString;
    LinearLayout layoutPrincipal;

    int cityNumber = 0;
    TextView textCity;

    /*
    private int cityCount = 0;
    private int totalCity = 3;
    private TextView textTomorrow, textTempTomorrow, textDescriptionTomorrow, textPressureTomorrow;
    private TextView textAfterTomorrow, textTempAfterTomorrow, textDescriptionAfterTomorrow, textPressureAfterTomorrow;
    private ImageView imageTomorrow, imageAfterTomorrow;
    private Layout fieldAfterTomorrow;
    */
    boolean dataCargada;
    public static final int TODAY = 0;
    public static final int TOMORROW = 1;
    public static final int AFTER_TOMORROW = 2;
    String jsonStr;
    public String jsonCity0;
    public String jsonCity1;
    public String jsonCity2;

    ArrayMap<String, String> arrayMapJSon;

    TextView textTempMinDay, textTempMaxDay, textDescriptionDay, textPressureDay, textDateDay;
    ImageView imageDay;
    int idIcon;
    public static  int countConn=0;
    public static  int countEntr=0;

    private boolean mIsShowSetting = false; //Show no instalado

    private AnimatorSet mSetRightOut, mSetRightOut1, mSetDownOut;
    private AnimatorSet mSetLeftIn, mSetLeftIn1, mSetUpIn;
    private AnimatorSet mSetFadeIn, mSetFadeOut;
    private boolean mIsBackVisible = false; //lado Back invisible
    private View mCardFrontLayout, mCardBackLayout;
    private View mCardFrontLayout1, mCardBackLayout1;
    private View mTextCityLayout0, mTextCityLayout1;
    private ImageView frontImage, backImage;
    private ImageView imageDayFrontTomorrow, imageDayFrontAfterTomorrow,imageDayBackAfterTomorrow, imageDayBackTomorrow;
    private TextView textCity0, textCity1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Quitar titulo
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //  new GetDatosWeather(0).execute();
        //  new GetDatosWeather(1).execute();

        findViews();
        loadAnimations();
        changeCameraDistance();


        Handler handler = new Handler();
        dataCargada = false;

        cityNumber = 0;
        currentCity = nameCity0;
        url = urlCity0;

        new GetDatosWeather().execute();
        //jsonCity0 = jsonStr;
       // Log.e(TAG, "Response from String: " + jsonCity0);
       Log.d(LOG_TAG,"jsonCity00: "+ jsonCity0);
        handler.postDelayed(new Runnable() {
            public void run() {
                cityNumber = 1;
                currentCity = nameCity1;
                url = urlCity1;
                new GetDatosWeather().execute();
            }
        }, 1200);

       // jsonCity1 = jsonStr;
        //  Log.d(LOG_TAG,"jsonCity1: "+ jsonCity1);

        handler.postDelayed(new Runnable() {
            public void run() {
             //   Log.d(LOG_TAG,"jsonCity1: " +jsonCity1);
                cityNumber = 2;
                currentCity = nameCity2;
                // currentCity="San Sebastián-Donstia";
                url = urlCity2;
                new GetDatosWeather().execute();
                dataCargada = true;  //ultima ciudad cargada
            }
        }, 2400);
       // jsonCity2 = jsonStr;

        handler.postDelayed(new Runnable() {
            public void run() {
                /*
                Log.d(LOG_TAG,"jsonCity0: " +jsonCity0);
                Log.d(LOG_TAG,"jsonCity1: "+ jsonCity1);
                Log.d(LOG_TAG,"jsonCity2: " +jsonCity2);
                */
                if (dataCargada) weatherAnimation();

            }
        },3200);
      //  jsonCity2 = jsonStr;


    }


    private class GetDatosWeather extends AsyncTask<Object, Object, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

          //  Toast.makeText(MainActivity.this, "Json Data is downloading " + cityNumber, Toast.LENGTH_LONG).show();


        }

        @Override
        protected String doInBackground(Object... params) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            //url de ciudad elegido:


            //datos recibidos:
            jsonString = sh.makeServiceCall(url);
            // выводим целиком полученную json-строку
             Log.d(LOG_TAG,"datos recibidos: "+ jsonString);

            //Log.e(TAG, "Response from url: " + jsonString);
            if (cityNumber==0) jsonCity0 = jsonString;
            else if (cityNumber==1) jsonCity1 = jsonString;
            else if (cityNumber==2) jsonCity2 = jsonString;

            return jsonString;

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            jsonStr = result;

            // Log.e(TAG, "Response from String: " + jsonStr);
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
                                cityNumber + " Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            //setWeatherData(jsonStr,0); //hoy
            // setWeatherData(jsonStr,1);  //mañana
            //  setWeatherData(jsonStr,2);  //pasado mañana
        }


    }

    public void setWeatherData(String result, int day) {
        String jsonStr = result;

        String tempDayText = "";
        String tempMinDay = "";
        String tempMaxDay = "";
        String descriptionDay = "";
        int weatherIdDay = 0;
        String pressureDay = "";
        String dateDay = "";
        //String iconString;
        //int idIcon;
          //  Log.d(LOG_TAG, jsonCity0);
     // Log.d(TAG, "Response from String city: " + currentCity);// jsonStr

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

                JSONObject weatherOfDay = weatherDay.getJSONObject(0);//квадратная скобка weather

                descriptionDay = weatherOfDay.getString("description");
                pressureDay = String.format("Presión: %.1f", listDay.getDouble("pressure")) + " hPa";
                dateDay = df.format(new Date(listDay.getLong("dt") * 1000));

                weatherIdDay = weatherOfDay.getInt("id");
                Log.d(TAG, "weather day: id " + weatherIdDay);
            } catch (final JSONException e) {
                //      Log.e(TAG, "Json String parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json String parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        } else {
            Log.e(TAG, "Couldn't get json from JSON String.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get json from JSON String. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG).show();
                }
            });
        }

        iconString = setWeatherIcon(weatherIdDay);
        Log.d(TAG, "weather day: iconString " + iconString);
        idIcon = getResources().getIdentifier(iconString, "drawable", getPackageName());

        arrayMapJSon = new ArrayMap<>();

        arrayMapJSon.put("iconString", iconString);
        arrayMapJSon.put("tempMinDay", tempMinDay);
        arrayMapJSon.put("tempMaxDay", tempMaxDay);
        arrayMapJSon.put("descriptionDay", descriptionDay);
        arrayMapJSon.put("pressureDay", pressureDay);
        arrayMapJSon.put("dateDay", dateDay);

        //    callFragments(day,cityNumber,idIcon,tempMinDay,tempMaxDay,descriptionDay, pressureDay,dateDay);

    }


    public static String setWeatherIcon(int weatherId) {
        //      int id = (weatherId / 100);
        String icon = "";
        if ((199 < weatherId) & (weatherId < 233)) icon = "w11d";
        else if ((299 < weatherId) & (weatherId < 311)) icon = "w05d";
        else if ((310 < weatherId) & (weatherId < 322)) icon = "w06d";
        else if ((499 < weatherId) & (weatherId < 522)) icon = "w08d";
        else if ((522 < weatherId) & (weatherId < 532)) icon = "w09d";
        else if ((599 < weatherId) & (weatherId < 650)) icon = "w13d";
        else if ((700 < weatherId) & (weatherId < 799)) icon = "w50d";
        else if (800 == weatherId) icon = "w01d";
        else if (801 == weatherId) icon = "w02d";
        else if (802 == weatherId) icon = "w03d";
        else if (803 == weatherId) icon = "w04d";
        else if (804 == weatherId) icon = "w04d";

        return icon;

    }


    private void weatherAnimation() {

        Handler handlerShow = new Handler();
        //layoutPrincipal=(LinearLayout)findViewById(R.id.layoutPrincipal);
      //  layoutPrincipal.setBackgroundResource(R.drawable.vitoria_gasteiz);
        handlerShow.postDelayed(new Runnable() {

            public void run() {
        cityNumber = 0;
        currentCity = "Vitoria-Gasteiz";

        setShow(cityNumber, currentCity, jsonCity0);
        mIsBackVisible = false; //lado Back invisible
            }
        }, 4000);
        handlerShow.postDelayed(new Runnable() {
            public void run() {
                //     textCity.setText(cityUrl1);
                cityNumber = 1;
                currentCity = "Bilbao-Bilbo";
                setShow(cityNumber, currentCity, jsonCity1);
                flipCard();
            }
        },9000);

        handlerShow.postDelayed(new Runnable() {
            public void run() {
                cityNumber = 2;
                currentCity = "Pamplona-Iruña";
                setShow(cityNumber, currentCity, jsonCity1);
                flipCard();
            }
        }, 14000);


    }



    private void setShow(int cityNumber, String currentCity, String jsonCity) {

        String jsonStr;
        //  String currentCity;
        //  int cityNumber;

        this.cityNumber = cityNumber;
        this.currentCity = currentCity; //"Vitoria-Gasteiz";
        // textCity.setText(currentCity);
        jsonStr = jsonCity;
        if (mIsShowSetting) {  //instalado
            if (!mIsBackVisible) {//Back setting
                setWeatherData(jsonStr, TOMORROW);
                setBackTomorrow();
                setWeatherData(jsonStr, AFTER_TOMORROW);
                setBackAfterTomorrow();


            } else {                // Front setting
                setWeatherData(jsonStr, TOMORROW);
                setFrontTomorrow();
                setWeatherData(jsonStr, AFTER_TOMORROW);
                setFrontAfterTomorrow();


            }
        } else {   //No instalado

            setWeatherData(jsonStr, TOMORROW);
            setFrontTomorrow();
            setWeatherData(jsonStr, AFTER_TOMORROW);
            setFrontAfterTomorrow();
            mIsShowSetting=true;

        }
    }

    private void setBackTomorrow() {
        textCity = (TextView) findViewById(R.id.textCityBack);
        imageDay = (ImageView) findViewById(R.id.imageDayBackTomorrow);
        textTempMinDay = (TextView) findViewById(R.id.textTempMinDayBackTomorrow);
        textTempMaxDay = (TextView) findViewById(R.id.textTempMaxDayBackTomorrow);
        textDescriptionDay = (TextView) findViewById(R.id.textDescriptionDayBackTomorrow);
        textPressureDay = (TextView) findViewById(R.id.textPressureDayBackTomorrow);
        textDateDay = (TextView) findViewById(R.id.textDateBackTomorrow);
        setWeatherData();
    }

    private void setBackAfterTomorrow() {
        imageDay = (ImageView) findViewById(R.id.imageDayBackAfterTomorrow);
        textTempMinDay = (TextView) findViewById(R.id.textTempMinDayBackAfterTomorrow);
        textTempMaxDay = (TextView) findViewById(R.id.textTempMaxDayBackAfterTomorrow);
        textDescriptionDay = (TextView) findViewById(R.id.textDescriptionDayBackAfterTomorrow);
        textPressureDay = (TextView) findViewById(R.id.textPressureDayBackAfterTomorrow);
        textDateDay = (TextView) findViewById(R.id.textDateBackAfterTomorrow);
        setWeatherData();
    }

    private void setFrontTomorrow() {
        textCity = (TextView) findViewById(R.id.textCityFront);
        imageDay = (ImageView) findViewById(R.id.imageDayFrontTomorrow);
        textTempMinDay = (TextView) findViewById(R.id.textTempMinDayFrontTomorrow);
        textTempMaxDay = (TextView) findViewById(R.id.textTempMaxDayFrontTomorrow);
        textDescriptionDay = (TextView) findViewById(R.id.textDescriptionDayFrontTomorrow);
        textPressureDay = (TextView) findViewById(R.id.textPressureDayFrontTomorrow);
        textDateDay = (TextView) findViewById(R.id.textDateFrontTomorrow);
        setWeatherData();
    }

    private void setFrontAfterTomorrow() {

        imageDay = (ImageView) findViewById(R.id.imageDayFrontAfterTomorrow);
        textTempMinDay = (TextView) findViewById(R.id.textTempMinDayFrontAfterTomorrow);
        textTempMaxDay = (TextView) findViewById(R.id.textTempMaxDayFrontAfterTomorrow);
        textDescriptionDay = (TextView) findViewById(R.id.textDescriptionDayFrontAfterTomorrow);
        textPressureDay = (TextView) findViewById(R.id.textPressureDayFrontAfterTomorrow);
        textDateDay = (TextView) findViewById(R.id.textDateFrontAfterTomorrow);
        setWeatherData();
    }

    private void setWeatherData() {
        idIcon = getResources().getIdentifier(arrayMapJSon.get("iconString"), "drawable", getPackageName());

        textTempMinDay.setText(arrayMapJSon.get("tempMinDay"));
        textTempMaxDay.setText(arrayMapJSon.get("tempMaxDay"));
        textDescriptionDay.setText(arrayMapJSon.get("descriptionDay"));
        imageDay.setImageResource(idIcon);
        //imageDay.setBackgroundResource(R.color.transparent);// imageDay.setAlpha(0);

        /* Picasso.with(this)
                .load("http://gasteiz-tronic.com/gt_publi/weather/"+iconString+".png")
                .resize(256, 256)
                .into(imageDay);
*/
        textCity.setText(currentCity);
        Log.d(TAG, "Response  city: " + currentCity+" image weather "+idIcon);// jsonStr
        //    textDateDay.setText(arrayMapJSon.get("dateDay"));
        //   textPressureDay.setText(arrayMapJSon.get("pressureDay"));
    }



    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
        mCardFrontLayout1.setCameraDistance(scale);
        mCardBackLayout1.setCameraDistance(scale);
        mTextCityLayout0.setCameraDistance(scale);
        mTextCityLayout1.setCameraDistance(scale);
    }

    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);
        mSetRightOut1 = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        mSetLeftIn1 = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);
        mSetUpIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.up_animation);
        mSetDownOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.down_animation);
        mSetFadeIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.image_fade_in);
        mSetFadeOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.image_fade_out);
    }

    private void findViews() {
        mCardBackLayout = findViewById(R.id.card_back);
        mCardFrontLayout = findViewById(R.id.card_front);
        mCardBackLayout1 = findViewById(R.id.card_back1);
        mCardFrontLayout1 = findViewById(R.id.card_front1);
        mTextCityLayout0 = findViewById(R.id.tvCityCero);
        mTextCityLayout1 = findViewById(R.id.tvCityOne);


    }

    public void flipCard() {
        if (!mIsBackVisible) {
            mSetRightOut.setTarget(mCardFrontLayout);
            mSetLeftIn.setTarget(mCardBackLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mSetRightOut1.setTarget(mCardFrontLayout1);
            mSetLeftIn1.setTarget(mCardBackLayout1);
            mSetRightOut1.start();
            mSetLeftIn1.start();
            mSetDownOut.setTarget(mTextCityLayout0);
            mSetUpIn.setTarget(mTextCityLayout1);
            mSetDownOut.start();
            mSetUpIn.start();

           // fadingImageDay();
            mIsBackVisible = true;
        } else {
            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFrontLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mSetRightOut1.setTarget(mCardBackLayout1);
            mSetLeftIn1.setTarget(mCardFrontLayout1);
            mSetRightOut1.start();
            mSetLeftIn1.start();
            mSetDownOut.setTarget(mTextCityLayout1);
            mSetUpIn.setTarget(mTextCityLayout0);
            mSetDownOut.start();
            mSetUpIn.start();
           // fadingImageDay();

            mIsBackVisible = false;
        }


    }

    private void fadingImageDay(){
        mSetFadeIn.setTarget(imageDayFrontTomorrow);
        mSetFadeIn.start();
        mSetFadeOut.setTarget(imageDayFrontAfterTomorrow);
        mSetFadeOut.start();
        mSetFadeIn.setTarget(imageDayBackTomorrow);
        mSetFadeIn.start();
        mSetFadeOut.setTarget(imageDayBackAfterTomorrow);
        mSetFadeOut.start();


    }

}

