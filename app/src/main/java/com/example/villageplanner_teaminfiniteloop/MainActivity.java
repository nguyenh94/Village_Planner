package com.example.villageplanner_teaminfiniteloop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase root;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Write a message to the database
//        root = FirebaseDatabase.getInstance();
//        reference = root.getReference("message");
//        reference.setValue("Works on macbook");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference users = db.collection("users");

        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);
        users.document("Ada").set(user);

        DocumentReference docRef = db.collection("users").document("Ada");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("getting user", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("getting user", "No such document");
                    }
                } else {
                    Log.d("getting user", "get failed with ", task.getException());
                }
            }
        });



//        Login_Registration registration = new Login_Registration();
//        try {
//            registration.Register("danialatest@gmail.com", "12345", "Huong", null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}