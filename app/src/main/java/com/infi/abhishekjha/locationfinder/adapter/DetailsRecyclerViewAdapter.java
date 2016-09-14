package com.infi.abhishekjha.locationfinder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infi.abhishekjha.locationfinder.R;
import com.infi.abhishekjha.locationfinder.pojo.Details;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek.jha on 9/12/2016.
 */
public class DetailsRecyclerViewAdapter extends RecyclerView.Adapter<DetailsRecyclerViewHolder> {
    private Context mContext;
    private List<Details> list;
    List<Details> data = new ArrayList<>();
    public DetailsRecyclerViewAdapter(Context context, List<Details> list){
        this.mContext = context;
        this.list = list;
     }

    public void swap(List<Details> datas){
        list.clear();
        list.addAll(datas);
        /*data.clear();
        data.addAll(datas);*/
        notifyDataSetChanged();
    }

    @Override
    public DetailsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_row,parent,false);
        DetailsRecyclerViewHolder detailsRecyclerViewHolder = new DetailsRecyclerViewHolder(view);

        return detailsRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsRecyclerViewHolder holder, int position) {
        holder.lat_text_val.setText(list.get(position).getLocation_lat());
        holder.lon_text_val.setText(list.get(position).getLocation_lon());
        //holder.time_text_val.setText(Utility.convertDate(String.valueOf(list.get(position).getLocation_time()),"dd/MM/yyyy hh:mm:ss"));
        holder.time_text_val.setText(list.get(position).getLocation_time());
        holder.location_address_val.setText(list.get(position).getLocation_address());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
