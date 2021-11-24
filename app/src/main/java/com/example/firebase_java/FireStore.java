package com.example.firebase_java;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FireStore {

    private static FirebaseFirestore mFirebaseFirestoreDB;
    public FirebaseFirestore getFirebaseFirestoreDB() { return mFirebaseFirestoreDB; }

    public void initDB(){
        // Access a Cloud Firestore instance from your Activity
        mFirebaseFirestoreDB = FirebaseFirestore.getInstance();
    }

    public void InsertData (){
        // [DB document sample
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("A-Test", "Hello");
        user.put("B-Test", "Is");
        user.put("D-Test", "Sam");
        user.put("C-Test","You");  // 網頁 DB 顯示順序由 A -> D 上至下

        // Add a new document with a generated ID
        mFirebaseFirestoreDB.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
        // DB]
    }

    public void SearchData(){
        mFirebaseFirestoreDB.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("TAG", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.e("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
