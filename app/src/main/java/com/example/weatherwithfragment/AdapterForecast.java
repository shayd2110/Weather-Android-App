package com.example.weatherwithfragment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.newweatherapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;

public class AdapterForecast extends ArrayAdapter<ForecastObject> implements View.OnClickListener {
    private ArrayList<ForecastObject> lForecast ;
    private int lastPosition = -1;
    Context mContext;


    public  class ViewHolder {
        public TextView tvIcon;
        public TextView tvDate;

    }

    public AdapterForecast(ArrayList<ForecastObject> data, Context context) {
        super(context, R.layout.forecast_listview_item, data);
        this.lForecast = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object obj =getItem(position);
        ForecastObject forecastObj = (ForecastObject) obj;
        

        switch (v.getId())
        {
            case R.id.tvIconItem:
                Snackbar.make(v, "Release date " +forecastObj.getIcon(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }

    }



    public View getView(int position, View convertView, ViewGroup parent){
        ForecastObject forecastObj = getItem(position);
        View res = null ;
        ViewHolder holder = null;

        try {
            if (convertView == null){
                holder =  new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.forecast_listview_item,parent,false);
                holder.tvIcon =  (TextView) convertView.findViewById(R.id.tvIconItem);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tvDateItem);
                res = convertView;
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
                res = convertView;
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        res.startAnimation(animation);
        lastPosition = position;
        Date date =  forecastObj.getDate().toDate();
        String dateStr = date.toString();
        holder.tvIcon.setText(forecastObj.getIcon());
        holder.tvDate.setText(dateStr);

        // Return the completed view to render on screen
        return convertView;


    }


}
