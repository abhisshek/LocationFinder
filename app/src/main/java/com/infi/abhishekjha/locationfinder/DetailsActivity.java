package com.infi.abhishekjha.locationfinder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.infi.abhishekjha.locationfinder.adapter.DetailsRecyclerViewAdapter;
import com.infi.abhishekjha.locationfinder.database.DatabaseHandler;
import com.infi.abhishekjha.locationfinder.pojo.Details;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek.jha on 9/12/2016.
 */
public class DetailsActivity extends AppCompatActivity {

    private RecyclerView details_recycler_view;
    private Context mContxet;
    private DatabaseHandler helper;
    private Cursor cursor;
    private DetailsRecyclerViewAdapter detailsRecyclerViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mContxet = this;
        helper = DatabaseHandler.getInstance(mContxet);
        helper.open();
        details_recycler_view = (RecyclerView)findViewById(R.id.details_recycler_view);
        details_recycler_view.setLayoutManager(new LinearLayoutManager(mContxet));
        details_recycler_view.setHasFixedSize(true);
        getLocationData();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            detailsRecyclerViewAdapter.swap(getUpdatedLocationData());
            //getLocationData();
            Log.d("receiver", "Got message: " + message);
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private List<Details> getUpdatedLocationData(){
        cursor = helper.getAllLocationData();
        cursor.moveToFirst();
        List<Details> list = new ArrayList<>();
        Toast.makeText(DetailsActivity.this, "Count: "+cursor.getCount(), Toast.LENGTH_SHORT).show();
        for (int i = 0; i < cursor.getCount(); i++) {
            String location_lat = cursor.getString(1);
            String location_lon = cursor.getString(2);
            String location_address = cursor.getString(3);
            String location_time = cursor.getString(4);
            list.add(new Details(location_lat,location_lon,location_address,location_time));
            cursor.moveToNext();
        }
        return list;
        /*detailsRecyclerViewAdapter = new DetailsRecyclerViewAdapter(mContxet,list);
        details_recycler_view.setAdapter(detailsRecyclerViewAdapter);
        if(cursor !=null){
            cursor.close();
        }*/
    }

    private void getLocationData(){
        cursor = helper.getAllLocationData();
        cursor.moveToFirst();
        List<Details> list = new ArrayList<>();
        Toast.makeText(DetailsActivity.this, "Count: "+cursor.getCount(), Toast.LENGTH_SHORT).show();
        for (int i = 0; i < cursor.getCount(); i++) {
            String location_lat = cursor.getString(1);
            String location_lon = cursor.getString(2);
            String location_address = cursor.getString(3);
            String location_time = cursor.getString(4);
            list.add(new Details(location_lat,location_lon,location_address,location_time));
            cursor.moveToNext();
        }
        detailsRecyclerViewAdapter = new DetailsRecyclerViewAdapter(mContxet,list);
        details_recycler_view.setAdapter(detailsRecyclerViewAdapter);
        if(cursor !=null){
            cursor.close();
        }
    }

}
