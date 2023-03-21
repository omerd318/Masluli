package com.example.masluli.Model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.masluli.MyApplication;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Maslul {
    public static final String COLLECTION_NAME = "maslulim";
    public static final String LAST_UPDATED = "lastUpdated";
    public static final String LOCAL_LAST_UPDATED = "maslulim_local_last_update";
    private static final int MAX_RATING = 5;

    @PrimaryKey
    @NonNull
    String id;
    String title;
    String location;
    int length;
    Difficulty difficulty;
    Boolean isAccessible;
    Boolean isWater;
    Boolean isRounded;
    String description;
    String userId;
    String imageUrl;
    int rating;
    Long lastUpdated;  // on firebase
    GeoPoint latlng;
    Boolean isDeleted;

    public Maslul(@NonNull String id, String title, String location, int length, Difficulty dif, Boolean isAccessible, Boolean isWater, Boolean isRounded, String description, String userId, int rating, GeoPoint latlng) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.length = length;
        this.difficulty = dif;
        this.isAccessible = isAccessible;
        this.isWater = isWater;
        this.isRounded = isRounded;
        this.description = description;
        this.userId = userId;
        this.rating = rating;
        this.latlng = null;
        if(latlng != null) {
            this.latlng = new GeoPoint(latlng.getLatitude(), latlng.getLongitude());
        }
        this.isDeleted = false;
    }

    public Maslul() {
        this.id = "";
        this.title = "";
        this.location = "";
        this.length = 0;
        this.difficulty = Difficulty.Easy;
        this.isAccessible = false;
        this.isWater = false;
        this.isRounded = false;
        this.description = "";
        this.userId = "";
        this.lastUpdated = new Long(0);
        this.rating = 0;
        this.latlng = new GeoPoint(0,0);
        this.isDeleted = false;
    }

    public enum Difficulty {
        Easy,
        Medium,
        Hard
    }

    public static Long getLocalLastUpdate() {
        SharedPreferences sharedPref = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sharedPref.getLong(LOCAL_LAST_UPDATED, 0);
    }

    public static void setLocalLastUpdate(Long time) {
        SharedPreferences sharedPref = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(LOCAL_LAST_UPDATED, time);
        editor.commit();
    }

    public static Maslul createMaslul(Map<String, Object> maslulJson, String docId) {
        String title = (String) maslulJson.get("title");
        String location = (String) maslulJson.get("location");
        int length = ((Long) maslulJson.get("length")).intValue();
        Difficulty difficulty = Difficulty.valueOf((String) maslulJson.get("difficulty"));
        Boolean isAccessible = (Boolean) maslulJson.get("isAccessible");
        Boolean isWater = (Boolean) maslulJson.get("isWater");
        Boolean isRounded = (Boolean) maslulJson.get("isRounded");
        String description = (String) maslulJson.get("description");
        String userId = (String) maslulJson.get("userId");
        Long rating = ((Long)maslulJson.get("rating"));
        int ratingInt = (rating != null) ? rating.intValue() : 0;
        String imageUrl = (String) maslulJson.get("imageUrl");
        GeoPoint latLng = (GeoPoint) maslulJson.get("latlng");
        Boolean isDeleted = (Boolean) maslulJson.get("isDeleted");

        Timestamp timestamp = (Timestamp) maslulJson.get(LAST_UPDATED);
        Long lastUpdated = timestamp.getSeconds();

        Maslul maslulItem = new Maslul(docId, title, location, length, difficulty, isAccessible, isWater, isRounded, description, userId, ratingInt, latLng);

        maslulItem.setLastUpdated(lastUpdated);
        maslulItem.setImageUrl(imageUrl);
        maslulItem.setId(docId);
        maslulItem.setDeleted(isDeleted);
        return maslulItem;
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put("id", id);
        json.put("title", title);
        json.put("location", location);
        json.put("length", length);
        json.put("difficulty", difficulty);
        json.put("isAccessible", isAccessible);
        json.put("isWater", isWater);
        json.put("isRounded", isRounded);
        json.put("description", description);
        json.put("userId", userId);
        json.put("rating", rating);
        json.put("imageUrl", imageUrl);
        json.put("latlng", latlng);
        json.put("isDeleted", isDeleted);

        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        return json;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAccessible() {
        return isAccessible;
    }

    public void setAccessible(Boolean accessible) {
        isAccessible = accessible;
    }

    public Boolean getWater() {
        return isWater;
    }

    public void setWater(Boolean water) {
        isWater = water;
    }

    public Boolean getRounded() {
        return isRounded;
    }

    public void setRounded(Boolean rounded) {
        isRounded = rounded;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if(rating > MAX_RATING) {
            this.rating = MAX_RATING;
        } else {
            this.rating = rating;
        }
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getLatitude() {
        return latlng != null ? latlng.getLatitude() : 0;
    }

    public double getLongitude() {
        return latlng != null ? latlng.getLongitude() : 0;
    }

    public void setLatlng(double lat, double lng) {
        this.latlng = new GeoPoint(lat, lng);
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
