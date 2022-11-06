package com.example.villageplanner_teaminfiniteloop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void registerCallBack(View view, boolean validReg, String name, String email, String password, Image photo) {
        // if email has not been registered before
        if(validReg) {
            Login_Registration reg_helper = new Login_Registration();

            // generate new ID for user
            String uniqueId = UUID.randomUUID().toString();

            // hash password
            String hashedPass = reg_helper.hashPassword(password);
            ArrayList<String> reminders = new ArrayList<String>();
            User user = new User(uniqueId, email, name, photo, password, null, reminders);
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            CollectionReference users = db.collection("users");
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("uniqueId", uniqueId);
            userInfo.put("name", name);
            userInfo.put("email", email);
            userInfo.put("password", hashedPass);
            userInfo.put("photo", photo);
            userInfo.put("location", LoginActivity.coordinate);

            users.document(email).set(userInfo);

            Intent intent = new Intent(this, TabBarActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(view.getContext(), "Email already registered.", Toast.LENGTH_LONG).show();
        }
    }

    public void register(View view) {
        TextView nameEntered = (TextView) findViewById(R.id.name);
        TextView passwordEntered = (TextView) findViewById(R.id.password);
        TextView emailEntered = (TextView) findViewById(R.id.email);

        String userName = nameEntered.getText().toString();
        String password = passwordEntered.getText().toString();
        String userEmail = emailEntered.getText().toString();

        // check if email is valid format
        boolean validEmailFormat = Login_Registration.checkEmailFormat(userEmail);
        if (!validEmailFormat) {
            Toast.makeText(view.getContext(), "Please enter a valid email.", Toast.LENGTH_LONG).show();
            return;
        }

        // check if email is already registered
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(userEmail);
        final Object[] userData = new Object[1];
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("getting user", "DocumentSnapshot data: " + document.getData());
                        userData[0] = document.getData();
                        registerCallBack(view, false, userName, userEmail, password, null);
                    } else {
                        Log.d("getting user", "No such document");
                        registerCallBack(view,true, userName, userEmail, password, null);
                    }
                } else {
                    Log.d("getting user", "get failed with ", task.getException());
                }
            }
        });
    }
}