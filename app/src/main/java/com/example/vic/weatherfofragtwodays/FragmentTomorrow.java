package com.example.vic.weatherfofragtwodays;

import android.os.Bundle;
//import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;
import static com.example.vic.weatherfofragtwodays.R.id.containerTomorrow;
import static com.example.vic.weatherfofragtwodays.R.id.textCity;
import static com.example.vic.weatherfofragtwodays.R.id.textTempMinDay;

/**
 * Created by vic on 20/02/2017.
 */

public class FragmentTomorrow extends Fragment{


    private TextView textTempMinDay,textTempMaxDay, textDescriptionDay, textPressureDay,textDateDay;
    private ImageView imageDay;
    static final String MIN_TEMP="textTempMinDay";
    private String memTempMinDay;
    private final String tempMinDay="12º";

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View rootView1=inflater.inflate(R.layout.fragment_tomorrow,container,false);
        setRetainInstance(true);
        imageDay = (ImageView) rootView1.findViewById(R.id.imageDay);
        textTempMinDay=(TextView) rootView1.findViewById(R.id.textTempMinDay);
        textTempMaxDay=(TextView) rootView1.findViewById(R.id.textTempMaxDay);
        textDescriptionDay=(TextView)rootView1.findViewById(R.id.textDescriptionDay);
        textPressureDay=(TextView)rootView1.findViewById(R.id.textPressureDay);
        textDateDay=(TextView)rootView1.findViewById(R.id.textDate);
        return rootView1;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("message", "This is my message to be reloaded");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getActivity(), "Pasado Fragment", Toast.LENGTH_LONG).show();
        if (savedInstanceState != null) {
            String message = savedInstanceState.getString("message");
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
    }
/*
  @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MIN_TEMP, memTempMinDay);
      //  Toast.makeText(getActivity(), "Save instance", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Save instance");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            textTempMinDay.setText("");

        } else {

            textTempMinDay.setText(savedInstanceState.getString(memTempMinDay));
        }
    }
 */



/*
@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("counter", mCounter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            mCounter = 0;
        } else {
            mCounter = savedInstanceState.getInt("counter", 0);
        }
    }
 */

    public void setDescription(int idIcon, String tempMinDayText, String tempMaxDayText, String descriptionDay, String pressureDay, String dateDay ){

        textTempMinDay.setText(tempMinDayText);

        textTempMaxDay.setText(tempMaxDayText);
        textDescriptionDay.setText(descriptionDay);

        imageDay.setImageResource(idIcon);
        //    textDateDay.setText(dateDay);
        //   textPressureDay.setText(pressureDay);

    }
}
