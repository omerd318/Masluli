package com.example.masluli;

import androidx.room.TypeConverter;

import com.google.firebase.firestore.GeoPoint;

public class Converters {

    @TypeConverter
    public static String geoPointToString(GeoPoint latlng) {

        if(latlng == null)
            return null;

        return latlng.getLatitude() + ":" + latlng.getLongitude();
    }


    @TypeConverter
    public static GeoPoint stringToGeoPoint(String latlng) {

        if(latlng == null)
            return null;

        GeoPoint gp = new GeoPoint(Double.parseDouble(latlng.split(":")[0]), Double.parseDouble(latlng.split(":")[1]));

        return gp;
    }
}
