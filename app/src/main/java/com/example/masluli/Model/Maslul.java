package com.example.masluli.Model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.masluli.MyApplication;

@Entity
public class Maslul {
    @PrimaryKey
    @NonNull
    String id;
    String title;
    String location;
    String description;
    String userId;
    String imageUrl;
    Long localLastUpdate;
    Boolean isDeleted;

    static final String ID = "id";
    static final String TITLE = "title";
    static final String LOCATION = "location";
    static final String DESCRIPTION = "description";
    static final String COLLECTION = "maslul";
    static final String LAST_UPDATED = "lastUpdated";
    static final String LOCAL_LAST_UPDATED = "maslulim_local_last_update";

    public Maslul() {
        this.id = "";
        this.title = "";
        this.location = "";
        this.description = "";
        this.userId = "";
        this.localLastUpdate = new Long(0) ;
        this.isDeleted = false;
    }

    public Maslul(@NonNull String id, String title, String location, String description, String userId) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.description = description;
        this.userId = userId;
        this.isDeleted = false;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static Long getLocalLastUpdate() {
        SharedPreferences sharedPref = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sharedPref.getLong(LOCAL_LAST_UPDATED, 0);
    }

    public static void setLocalLastUpdate(Long time) {
        SharedPreferences sharedPref = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(LOCAL_LAST_UPDATED,time);
        editor.commit();
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
}
