package com.infi.abhishekjha.locationfinder.utils;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.text.format.DateFormat;

import com.infi.abhishekjha.locationfinder.service.LocationService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Abhishek.jha on 9/12/2016.
 */
public class Utility {

    public static void startService(Context context){
        Intent intent = new Intent(context, LocationService.class);
        PendingIntent pintent = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60*1000, pintent);
    }

    public static void stopService(Context context){
        Intent intent = new Intent(context, LocationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context,0,intent,0);//getBroadcast(context, 1253, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static final int defaultLocationUpdateTime = 1*60*1000; //1 min ch

    public static void startLocation(Context context){
        //Log.v(TAG,"Sending Location start");
        AlarmManager almMan = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        // update location
        PendingIntent pendingIntentUpdateLocation = PendingIntent.getService(context,
                1, new Intent(context, LocationService.class),PendingIntent.FLAG_UPDATE_CURRENT);
        almMan.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
                Utility.defaultLocationUpdateTime, pendingIntentUpdateLocation);


        /*AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, LocationService.class);
        PendingIntent pintent = PendingIntent.getService(context, 1, intent, 0);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(),
                Utility.defaultLocationUpdateTime, pintent);*/

    }
    public static void stopLocation(Context context){
        //Log.v(TAG,"Seding Location stop");
        AlarmManager almMan = (AlarmManager)context.getSystemService( Context.ALARM_SERVICE);
        PendingIntent pendingIntentUpdateLocation = PendingIntent.getService(context, 1,
                new Intent(context, LocationService.class),PendingIntent.FLAG_UPDATE_CURRENT);
        almMan.cancel(pendingIntentUpdateLocation); // cancel
    }

    public static String getDate(long milliSeconds, String dateFormat){
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    public static boolean checkGpsLocationPermission(final Context context){
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}
        return gps_enabled;
    }

    public static boolean checkNetworkEnabled(final Context context){
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean network_enabled = false;
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}
        return network_enabled;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
