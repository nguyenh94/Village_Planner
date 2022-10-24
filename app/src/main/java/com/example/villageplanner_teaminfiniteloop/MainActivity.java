package com.example.villageplanner_teaminfiniteloop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase root;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Write a message to the database
        root = FirebaseDatabase.getInstance();
        reference = root.getReference("message");
        reference.setValue("Works on macbook");

        Login_Registration registration = new Login_Registration();
        try {
            registration.Register("test@gmail.com", "12345", "Huong", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}