package com.example.masluli.Model;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.masluli.R;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {

    public enum MaslulimListLoadingState{
        LOADING,
        NOT_LOADING
    }

    public static final String[] areas = new String[]{
            "Where am I form",
            "North",
            "Center",
            "South"
    };

    public static final Model instance = new Model();
    private FirebaseModel firebaseModel = new FirebaseModel();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    AppLocalDbRepository localDb = AppLocalDb.getAppDb();

    final public MutableLiveData<MaslulimListLoadingState> EventMaslulimListLoadingState = new MutableLiveData<MaslulimListLoadingState>(MaslulimListLoadingState.NOT_LOADING);

    public static Model instance(){
        return instance;
    }
    private Model(){
    }

    public interface Listener<T>{
        void onComplete(T data);
    }

    private LiveData<List<Maslul>> allMaslulimList;

    private LiveData<List<Maslul>> myMaslulimList;

    public LiveData<List<Maslul>> getAllMaslulim() {
        if(allMaslulimList == null){
            allMaslulimList = localDb.maslulDao().getAll();
            refreshAllMaslulim();
        }
        return allMaslulimList;
    }

    public LiveData<List<Maslul>> getMyMaslulim() {
        if(myMaslulimList == null){
            myMaslulimList = localDb.maslulDao().getMyMaslulim(getUserEmail());
            refreshAllMaslulim();
        }
        return myMaslulimList;
    }

    public void refreshAllMaslulim(){
        EventMaslulimListLoadingState.setValue(MaslulimListLoadingState.LOADING);

        // get local last update
        Long localLastUpdate = Maslul.getLocalLastUpdate();

        // get all updated records from firebase since local last update
        firebaseModel.getAllMaslulimSince(localLastUpdate,list->{
            executor.execute(()->{
                Log.d("TAG", " firebase return : " + list.size());
                Long time = localLastUpdate;
                for(Maslul maslul:list){
                    // insert new records into ROOM
                    if (maslul.getDeleted()) {
                        localDb.maslulDao().delete(maslul);
                    } else {
                        localDb.maslulDao().insertAll(maslul);
                    }

                    if (time < maslul.getLastUpdated()){
                        time = maslul.getLastUpdated();
                    }
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // update local last update
                Maslul.setLocalLastUpdate(time);
                EventMaslulimListLoadingState.postValue(MaslulimListLoadingState.NOT_LOADING);
            });
        });
    }

    public void addMaslul(Maslul maslul, Listener<Void> listener){
        maslul.setUserId(getUserEmail());
        firebaseModel.saveMaslul(maslul,(Void)->{
            refreshAllMaslulim();
            listener.onComplete(null);
        });
    }

    public void register(String email, String password, Model.Listener<FirebaseUser> listener) {
        firebaseModel.register(email,password, listener);
    }

    public void login(String email, String password, Model.Listener<FirebaseUser> listener) {
        firebaseModel.login(email,password, listener);
    }

    public boolean isSignedIn() {
        return firebaseModel.isSignedIn();
    }

    public void signOut() {
        EventMaslulimListLoadingState.postValue(null);
        firebaseModel.signOut();
    }

    public void addUser(User user, Model.Listener<User> listener){
        firebaseModel.addUser(user, listener);
    }

    public void getUserById(String email, Model.Listener<User> listener) {
        firebaseModel.getUserById(email,listener);
    }

    public String getUserEmail(){
        return firebaseModel.getUserEmail();
    }

    public void uploadImage(String name, Bitmap bitmap, Listener<String> listener) {
        firebaseModel.uploadImage(name,bitmap,listener);
    }

    public void getMaslulById(String maslulId, Listener<Maslul> listener) {
        List<Maslul> maslulimList = allMaslulimList.getValue();
        Maslul maslul = maslulimList.stream().filter(ms -> ms.getId().
                equals(maslulId)).findFirst().orElse(null);
        listener.onComplete(maslul);
    }
}
