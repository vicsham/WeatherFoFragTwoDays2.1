package com.example.vic.weatherfofragtwodays;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by vic on 20/02/2017.
 */

public class FragmentAfterTomorrow extends Fragment{

    private TextView textTempDay, textDescriptionDay, textPressureDay,textDateDay;
    private ImageView imageDay;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View rootView1=inflater.inflate(R.layout.fragment_after_tomorrow,container,false);

        imageDay = (ImageView) rootView1.findViewById(R.id.imageDay);
        textTempDay=(TextView) rootView1.findViewById(R.id.textTempDay);
        textDescriptionDay=(TextView)rootView1.findViewById(R.id.textDescriptionDay);
        textPressureDay=(TextView)rootView1.findViewById(R.id.textPressureDay);
        textDateDay=(TextView)rootView1.findViewById(R.id.textDate);
        return rootView1;
    }

    public void setDescription(int idIcon,String tempDayText,String descriptionDay, String pressureDay,String dateDay) {

        textTempDay.setText(tempDayText);
        textDescriptionDay.setText(descriptionDay);
        textPressureDay.setText(pressureDay);
        imageDay.setImageResource(idIcon);
        textDateDay.setText(dateDay);


    }
}
