package com.example.villageplanner_teaminfiniteloop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void loginCallBack(View view, boolean emailValid, String email, String password) {
        TextView passwordEntered = (TextView) findViewById(R.id.password);
        String unhashedPass = passwordEntered.getText().toString();

        if (emailValid) {
            Login_Registration login_helper = new Login_Registration();

            // check password by hashing current password and compare to user's stored password
            String hashedPass = login_helper.hashPassword(unhashedPass);
            if (password.equals(hashedPass)) {
                // TODO START ACTIVITY THAT MOVES TO HOME PAGE (MAP UI)
                Toast.makeText(view.getContext(), "Login Successful.", Toast.LENGTH_LONG).show();
            } else {  // if password is incorrect
                Toast.makeText(view.getContext(), "Incorrect password.", Toast.LENGTH_LONG).show();
            }
        } else {  // if email is not registered
            Toast.makeText(view.getContext(), "Email is not registered.", Toast.LENGTH_LONG).show();
        }
    }

    public void login(View view) {
        TextView passwordEntered = (TextView) findViewById(R.id.password);
        TextView emailEntered = (TextView) findViewById(R.id.email);

        String password = passwordEntered.getText().toString();
        String userEmail = emailEntered.getText().toString();

        // check if query to the database to get email and password
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(userEmail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {  // if email exists
                        User user = document.toObject(User.class);
                        loginCallBack(view, true, user.getEmail(), user.getPassword());
                    } else {  // if email doesn't exists
                        loginCallBack(view, false, userEmail, password);
                    }
                } else {
                    Log.d("getting user", "get failed with ", task.getException());
                }
            }
        });

    }

}