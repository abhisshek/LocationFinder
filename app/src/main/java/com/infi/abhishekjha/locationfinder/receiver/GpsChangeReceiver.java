package com.infi.abhishekjha.locationfinder.receiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.infi.abhishekjha.locationfinder.utils.Utility;

/**
 * Created by Abhishek.jha on 9/12/2016.
 */
public class GpsChangeReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        final LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
        if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Toast.makeText(context, "Gps: enabled", Toast.LENGTH_SHORT).show();
            if(Utility.checkGpsLocationPermission(context)){
                int locationPermissionCheck = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
                if(locationPermissionCheck == PackageManager.PERMISSION_GRANTED){
                    /*Utility.startService(context);*/
                    Utility.startLocation(context);
                }else{
                    Intent i = new Intent();
                    i.setClassName("com.infi.abhishekjha.locationfinder", "com.infi.abhishekjha.locationfinder.MainActivity");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            }else{
                Intent i = new Intent();
                i.setClassName("com.infi.abhishekjha.locationfinder", "com.infi.abhishekjha.locationfinder.MainActivity");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }

        }else{
            Toast.makeText(context, "Gps: disabled", Toast.LENGTH_SHORT).show();
            /*Utility.stopService(context);*/
            Utility.stopLocation(context);
        }
    }
}
