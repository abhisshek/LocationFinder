package com.infi.abhishekjha.locationfinder.pojo;

/**
 * Created by Abhishek.jha on 9/12/2016.
 */
public class Details {
    String location_lat;
    String location_lon;
    String location_time;

    public String getLocation_address() {
        return location_address;
    }

    public void setLocation_address(String location_address) {
        this.location_address = location_address;
    }

    String location_address;

    public String getLocation_time() {
        return location_time;
    }

    public void setLocation_time(String location_time) {
        this.location_time = location_time;
    }

    public String getLocation_lon() {
        return location_lon;
    }

    public void setLocation_lon(String location_lon) {
        this.location_lon = location_lon;
    }

    public String getLocation_lat() {
        return location_lat;
    }

    public void setLocation_lat(String location_lat) {
        this.location_lat = location_lat;
    }
    public Details(String location_lat, String location_lon, String location_time, String location_address){
        this.location_lat = location_lat;
        this.location_lon = location_lon;
        this.location_time = location_time;
        this.location_address = location_address;
    }

}
