package com.example.vic.weatherfofragtwodays;

import android.os.Bundle;
//import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by vic on 20/02/2017.
 */

public class FragmentTomorrow extends Fragment{

    private TextView  textTempDay, textDescriptionDay, textPressureDay,textDateDay;
    private ImageView imageDay;
       @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
           View rootView=inflater.inflate(R.layout.fragment_tomorrow,container,false);

           imageDay = (ImageView) rootView.findViewById(R.id.imageDay);
           textTempDay=(TextView) rootView.findViewById(R.id.textTempDay);
           textDescriptionDay=(TextView)rootView.findViewById(R.id.textDescriptionDay);
           textPressureDay=(TextView)rootView.findViewById(R.id.textPressureDay);
           textDateDay=(TextView)rootView.findViewById(R.id.textDate);
           return rootView;
    }

    public void setDescription(int idIcon,String tempDayText,String descriptionDay, String pressureDay,String dateDay) {

        textTempDay.setText(tempDayText);
        textDescriptionDay.setText(descriptionDay);
        textPressureDay.setText(pressureDay);
        imageDay.setImageResource(idIcon);
        textDateDay.setText(dateDay);


    }
}
