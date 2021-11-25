package com.example.firebase_java;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FireStore {

    private static FirebaseFirestore mFirebaseFirestoreDB;

    public FirebaseFirestore getFirebaseFirestoreDB() {
        return mFirebaseFirestoreDB;
    }

    public void initDB() {
        // Access a Cloud Firestore instance from your Activity
        mFirebaseFirestoreDB = FirebaseFirestore.getInstance();
    }

    public void InsertData(String collection, String document) {
        // [DB document sample
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("A-Test", "Hello");
        user.put("B-Test", "Is");
        user.put("D-Test", "Sam");
        user.put("C-Test", "You");  // 網頁 DB 顯示順序由 A -> D 上至下

        // Add a new document with a generated ID
        mFirebaseFirestoreDB.collection(collection).document(document)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
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

    public void SearchData(String collection) {
        mFirebaseFirestoreDB.collection(collection)
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

    public void SearchDataFromDoc(String collection, String document) {
        mFirebaseFirestoreDB.collection(collection).document(document)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.e("TAG", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    public void UpdateData(String collection, String document) {

        Map<String, Object> user = new HashMap<>();
        user.put("A-Test", "World");
        user.put("B-Test", "One");
        user.put("D-Test", "Two");
        user.put("C-Test", "Three");  // 網頁 DB 顯示順序由 A -> D 上至下
        
        mFirebaseFirestoreDB
                .collection(collection)
                .document(document)
                .update(user);

        mFirebaseFirestoreDB
                .collection(collection)
                .document(document)
                .update("C-Test", 6666);
    }

    public void deleteData(String collection, String document) {
        mFirebaseFirestoreDB.collection(collection).document(document)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error deleting document", e);
                    }
                });
    }
}