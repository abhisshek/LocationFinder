package com.infi.abhishekjha.locationfinder.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.infi.abhishekjha.locationfinder.database.DatabaseHandler;
import com.infi.abhishekjha.locationfinder.utils.Constants;
import com.infi.abhishekjha.locationfinder.utils.Utility;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Abhishek.jha on 9/12/2016.
 */
public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private DatabaseHandler helper;
    private Cursor cursor;
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 60;
    private static final long FASTEST_INTERVAL = 1000 * 60;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    AddressResultReceiver mResultReceiver;
    int fetchType = Constants.USE_ADDRESS_LOCATION;

    protected void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mResultReceiver = new AddressResultReceiver(null);
        createLocationRequest();
        if(mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LocationService: ","onDestroy");
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("LocationService: ","onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("LocationService: ","onStartCommand");
        mGoogleApiClient.connect();
        helper = DatabaseHandler.getInstance(mContext);
        helper.open();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.d("LocationService: ","onConnected");
        startLocationUpdates();

        /*Log.d("LocationService: ", "OnConnected Called");
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        String lat = String.valueOf(mLocation.getLatitude());
        String lon = String.valueOf(mLocation.getLongitude());
        String time = Utility.convertDate(String.valueOf(mLocation.getTime()),"dd/MM/yyyy hh:mm:ss");
        Log.d("Abhishek: ","lat: "+lat+" lon: "+lon+" time: "+time);
        Toast.makeText(LocationService.this, "lat: "+lat+" lon: "+lon+" time: "+time,
                Toast.LENGTH_LONG).show();
        long rows = helper.mWriteCategory(lat, lon, String.valueOf(mLocation.getTime()));
        if(rows>0){
            Toast.makeText(LocationService.this, "rows value: "+rows, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(LocationService.this, "rows value: 0", Toast.LENGTH_SHORT).show();
        }
        stopSelf();*/
    }

    protected void startLocationUpdates() {
        Log.d("LocationService: ","startLocationUpdates before check permission");
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.d("LocationService: ","startLocationUpdates after check permission");
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    protected void stopLocationUpdates(){
        Log.d("LocationService: ","stopLocationUpdates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    String mLastUpdateTime = "";
    @Override
    public void onLocationChanged(Location location) {
        Log.d("LocationService: ","OnLocationChanged");
        Location mLastLocation = location;
        String mLatitude = String.valueOf(mLastLocation.getLatitude());
        String mLongitude = String.valueOf(mLastLocation.getLongitude());
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        Log.d("Location update","mLatitude: "+mLatitude+" mLongitude: "+mLongitude+ " mLastUpdateTime: "+mLastUpdateTime);
        Toast.makeText(LocationService.this,
                "mLatitude: "+mLatitude+" mLongitude: "+mLongitude+ " mLastUpdateTime: "+mLastUpdateTime,
                Toast.LENGTH_LONG).show();
        if(Utility.isNetworkAvailable(mContext)){
            callAddressIntentService(mLatitude, mLongitude);
        }else{
            saveDataWithoutAddress(mLatitude, mLongitude);
        }
    }

    private void saveDataWithoutAddress(String mLatitude, String mLongitude){
        long rows = helper.mWriteCategory(mLatitude, mLongitude,"No Internet Found.", mLastUpdateTime);
        if(rows>0){
            Toast.makeText(LocationService.this, "rows value: "+rows, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(LocationService.this, "rows value: 0", Toast.LENGTH_SHORT).show();
        }
        sendLocalBroadcast();
        stopSelf();
    }

    private void callAddressIntentService(String mLatitude, String mLongitude){
        Intent intent = new Intent(this, GeocodeAddressIntentService.class);
        fetchType = Constants.USE_ADDRESS_LOCATION;
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.FETCH_TYPE_EXTRA, fetchType);
        intent.putExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA,
                Double.parseDouble(mLatitude));
        intent.putExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA,
                Double.parseDouble(mLongitude));
        startService(intent);

    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                final Address address = resultData.getParcelable(Constants.RESULT_ADDRESS);
                String addressStr = address.getAddressLine(0);
                String addressStr1 = address.getAddressLine(1);
                String addressStr2 = address.getAddressLine(2);
                String addressStr3 = address.getAddressLine(3);

                String latitude = String.valueOf(address.getLatitude());
                String longitude = String.valueOf(address.getLongitude());
                String locationAddress = resultData.getString(Constants.RESULT_DATA_KEY);
                long rows = helper.mWriteCategory(latitude, longitude,locationAddress, mLastUpdateTime);
                if(rows>0){
                    //Toast.makeText(LocationService.this, "rows value: "+rows, Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(LocationService.this, "rows value: 0", Toast.LENGTH_SHORT).show();
                }
                Log.d("Location Address: ", addressStr +" "+addressStr1 +" "+addressStr2 +" "+addressStr3);
                Log.d("resultData Address: ",resultData.getString(Constants.RESULT_DATA_KEY));
                sendLocalBroadcast();
                stopSelf();

                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        infoText.setVisibility(View.VISIBLE);
                        infoText.setText("Latitude: " + address.getLatitude() + "\n" +
                                "Longitude: " + address.getLongitude() + "\n" +
                                "Address: " + resultData.getString(Constants.RESULT_DATA_KEY));
                    }
                });*/
            }
            else {

                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        infoText.setVisibility(View.VISIBLE);
                        infoText.setText(resultData.getString(Constants.RESULT_DATA_KEY));
                    }
                });*/
            }
        }
    }

    private void sendLocalBroadcast(){
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("custom-event-name");
        // You can also include some extra data.
        intent.putExtra("message", "This is my message!");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


}
