package com.example.masluli.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.masluli.MyApplication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FirebaseModel {
    FirebaseFirestore db;
    FirebaseStorage storage;
    FirebaseAuth auth;

    FirebaseModel(){
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    void uploadImage(String name, Bitmap bitmap, Model.Listener<String> listener){
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images/" + name + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        listener.onComplete(uri.toString());
                    }
                });
            }
        });
    }


    public boolean isSignedIn(){
        FirebaseUser currentUser = auth.getCurrentUser();
        return (currentUser != null);
    }

    public String getUserEmail(){
        FirebaseUser currentUser = auth.getCurrentUser();
        return currentUser.getEmail();
    }

    public void login(String email, String password, Model.Listener<FirebaseUser> listener) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onComplete(auth.getCurrentUser());
                    } else {
                        Toast.makeText(MyApplication.getContext(), task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        listener.onComplete(null);                        }
                });
    }

    public void signOut() {
        auth.signOut();
    }

    public void register(String email, String password, Model.Listener<FirebaseUser> listener) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onComplete(auth.getCurrentUser());
                    } else {

                        Toast.makeText(MyApplication.getContext(), task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        listener.onComplete(null);                        }
                });
    }

    public void addUser(User user, Model.Listener<User> listener) {
        Map<String, Object> userJson = user.toJson();
        db.collection(User.COLLECTION_NAME)
                .document(user.getEmail())
                .set(userJson)
                .addOnSuccessListener(unused -> listener.onComplete(user))
                .addOnFailureListener(e -> listener.onComplete(user));
    }

    public void getUserById(String email, Model.Listener<User> listener) {
        db.collection(User.COLLECTION_NAME)
                .document(email)
                .get()
                .addOnCompleteListener(task -> {
                    User user = null;
                    if (task.isSuccessful() & task.getResult()!= null){
                        user = user.createUser(task.getResult().getData());
                    }
                    listener.onComplete(user);
                });
    }

    public void getAllMaslulimSince(Long since, Model.Listener<List<Maslul>> callback){
        db.collection(Maslul.COLLECTION_NAME)
                .whereGreaterThanOrEqualTo(Maslul.LAST_UPDATED, new Timestamp(since,0))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Maslul> list = new LinkedList<>();
                        if (task.isSuccessful()){
                            QuerySnapshot jsonsList = task.getResult();
                            for (DocumentSnapshot json: jsonsList){
                                Maslul maslul = Maslul.createMaslul(json.getData(), json.getId());
                                list.add(maslul);
                            }
                        }
                        callback.onComplete(list);
                    }
                });
    }

    public void saveMaslul(Maslul maslul, Model.Listener<Void> listener) {
        // Add maslul case - get new free id
        String maslulId = maslul.getId();
        if (maslulId.equals("")) {
            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
            CollectionReference maslulsRef = rootRef.collection(Maslul.COLLECTION_NAME);
            maslulId = maslulsRef.document().getId();
        }

        db.collection(Maslul.COLLECTION_NAME)
                .document(maslulId)
                .set(maslul.toJson())
                .addOnSuccessListener(unused -> listener.onComplete(null))
                .addOnFailureListener(e -> listener.onComplete(null));
    }
}
