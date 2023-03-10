package com.example.masluli.Model;

public class Model {
    private static final Model _instance = new Model();
    private FirebaseModel firebaseModel = new FirebaseModel();

    public static Model instance(){
        return _instance;
    }
    private Model(){
    }

    public interface Listener<T>{
        void onComplete(T data);
    }

}
