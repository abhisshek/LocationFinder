package com.infi.abhishekjha.locationfinder.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.infi.abhishekjha.locationfinder.R;

/**
 * Created by Abhishek.jha on 9/12/2016.
 */
public class DetailsRecyclerViewHolder extends RecyclerView.ViewHolder{

    TextView lat_text_val,lon_text_val, time_text_val, location_address_val;
    public DetailsRecyclerViewHolder(View itemView) {
        super(itemView);
        lat_text_val = (TextView)itemView.findViewById(R.id.lat_text_val);
        lon_text_val = (TextView)itemView.findViewById(R.id.lon_text_val);
        time_text_val = (TextView)itemView.findViewById(R.id.time_text_val);
        location_address_val = (TextView)itemView.findViewById(R.id.location_address_val);
    }

}
