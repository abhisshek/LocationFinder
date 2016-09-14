package com.infi.abhishekjha.locationfinder.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Abhishek on 14/09/16.
 */
public class GeofenceTransitionsIntentService extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GeofenceTransitionsIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
