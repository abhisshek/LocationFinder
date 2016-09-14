package com.infi.abhishekjha.locationfinder;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.infi.abhishekjha.locationfinder.service.LocationService;
import com.infi.abhishekjha.locationfinder.utils.Utility;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button start_location,stop_location, view_details;
    private Context mContxet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContxet = this;
        findViews();
    }

    private boolean locationPermission = false;
    private static final int LOC_REQ_CODE = 100;
    private static final int GPS_REQ_CODE = 101;
    private void findViews(){
        start_location = (Button)findViewById(R.id.start_location);
        stop_location = (Button)findViewById(R.id.stop_location);
        view_details = (Button)findViewById(R.id.view_details);
        start_location.setOnClickListener(this);
        stop_location.setOnClickListener(this);
        view_details.setOnClickListener(this);
        checkForPermissions();
    }

    private void checkForPermissions(){
        if(Utility.checkGpsLocationPermission(mContxet)){
            if(Build.VERSION.SDK_INT>=23){
                checkLocationPermission();
            }else{
                locationPermission = true;
                //Nothing special have to do
                Utility.startLocation(mContxet);
            }
        }else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContxet);
            dialog.setMessage("Location not enabled");
            dialog.setPositiveButton("Open Location Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(myIntent, GPS_REQ_CODE);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.root_main), "Location is required.",
                            Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkForPermissions();
                        }
                    });
                    snackbar.show();

                }
            });
            dialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GPS_REQ_CODE:
                if(Utility.checkGpsLocationPermission(mContxet)){
                    if(Build.VERSION.SDK_INT>=23){
                        checkLocationPermission();
                    }else{
                        locationPermission = true;
                        //Nothing special have to do
                        Utility.startLocation(mContxet);
                    }
                }else{
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.root_main), "Location is required.",
                            Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkForPermissions();
                        }
                    });
                    snackbar.show();
                }
                break;
        }
    }

    private void checkLocationPermission(){
        int locationPermissionCheck = ActivityCompat.checkSelfPermission(mContxet, Manifest.permission.ACCESS_FINE_LOCATION);
        if(locationPermissionCheck == PackageManager.PERMISSION_GRANTED){
            locationPermission = true;
            Utility.startLocation(mContxet);
        }else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOC_REQ_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case LOC_REQ_CODE:
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    locationPermission = true;
                    //Utility.startService(mContxet);
                    Utility.startLocation(mContxet);
                    Toast.makeText(MainActivity.this, "Permission granted.", Toast.LENGTH_SHORT).show();
                }else{
                    locationPermission = false;
                    /*Utility.stopService(mContxet);*/
                    Toast.makeText(MainActivity.this, "Permission Not granted.", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.root_main), "Location is required.",
                            Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkForPermissions();
                        }
                    });
                    snackbar.show();
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_location :
                if(Utility.isMyServiceRunning(mContxet, LocationService.class)){
                    Toast.makeText(MainActivity.this, "Location Services already running.", Toast.LENGTH_SHORT).show();
                }else{
                    if(Utility.checkGpsLocationPermission(mContxet) && locationPermission){
                        /*Utility.startService(mContxet);*/
                        Utility.startLocation(mContxet);
                    }else{
                        checkForPermissions();
                    }
                    /*if(locationPermission){
                        Utility.startService(mContxet);
                    }else{
                        checkLocationPermission();
                    }*/
                }
            break;
            case R.id.stop_location :
                if(Utility.isMyServiceRunning(mContxet, LocationService.class)){
                    /*Utility.stopService(mContxet);*/
                    Utility.stopLocation(mContxet);
                }else{
                    Toast.makeText(MainActivity.this, "Location Services not running.", Toast.LENGTH_SHORT).show();
                }
            break;
            case R.id.view_details:
                startActivity(new Intent(mContxet, DetailsActivity.class));
                break;
        }
    }
}
