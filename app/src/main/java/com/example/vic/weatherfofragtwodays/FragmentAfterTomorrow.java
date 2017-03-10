package com.example.vic.weatherfofragtwodays;

import android.os.Bundle;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by vic on 20/02/2017.
 */

public class FragmentAfterTomorrow extends Fragment{

    private TextView textTempMinDay,textTempMaxDay, textDescriptionDay, textPressureDay,textDateDay;
    private ImageView imageDay;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        View rootView1=inflater.inflate(R.layout.fragment_after_tomorrow,container,false);

        imageDay = (ImageView) rootView1.findViewById(R.id.imageDay);
        textTempMinDay=(TextView) rootView1.findViewById(R.id.textTempMinDay);
        textTempMaxDay=(TextView) rootView1.findViewById(R.id.textTempMaxDay);
        textDescriptionDay=(TextView)rootView1.findViewById(R.id.textDescriptionDay);
        textPressureDay=(TextView)rootView1.findViewById(R.id.textPressureDay);
        textDateDay=(TextView)rootView1.findViewById(R.id.textDate);
        return rootView1;
    }

    public void setDescription(int idIcon,String tempMinDayText,String tempMaxDayText,String descriptionDay, String pressureDay,String dateDay) {

        textTempMinDay.setText(tempMinDayText);

        textTempMaxDay.setText(tempMaxDayText);
        textDescriptionDay.setText(descriptionDay);

        imageDay.setImageResource(idIcon);
    //    textDateDay.setText(dateDay);
     //   textPressureDay.setText(pressureDay);

    }
}
