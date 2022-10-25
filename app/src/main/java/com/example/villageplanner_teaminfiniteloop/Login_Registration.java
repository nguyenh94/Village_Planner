package com.example.villageplanner_teaminfiniteloop;
import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Login_Registration {
    // https://www.geeksforgeeks.org/how-to-save-data-to-the-firebase-realtime-database-in-android/
    static public void Register(String email, String password, String name, Image photo) throws Exception{
        // implement random id generator or want to use email as user id?
        String uniqueId = UUID.randomUUID().toString();
        //TODO SETUP FIRESTORE AUTH and incorporate user object
        boolean validReg = true;
        if (validReg) {
            User user = new User(uniqueId, email, name, photo, password, null);
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            CollectionReference users = db.collection("users");

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("uniqueId", uniqueId);
            userInfo.put("name", name);
            userInfo.put("email", email);
            userInfo.put("password", hashPassword(password));
            userInfo.put("photo", photo);

            users.document(email).set(userInfo);
//            FirebaseDatabase root = FirebaseDatabase.getInstance();
//            DatabaseReference reference = root.getReference("users");
//            password = hashPassword(password);
//
//            reference.child(user.getId()).child("name").setValue(name);
//            reference.child(user.getId()).child("email").setValue(email);
//            reference.child(user.getId()).child("photo").setValue(photo);
//            reference.child(user.getId()).child("password").setValue(password);
        }
    }

    static public void Login(String email, String password) throws Exception{

    }

    static private String hashPassword(String rawValue){
        try
        {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Add password bytes to digest
            md.update(rawValue.getBytes());
            // Get the hash's bytes
            byte[] bytes = md.digest();

            // This bytes[] has bytes in decimal format. Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            // Get complete hashed password in hex format
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";  //Error
    }

    // Get the email and talk to the firebase to see email has been already registered
//    static private boolean checkValidRegistration(String email) {
//        final boolean[] emailValid = {true};
//        FirebaseDatabase root = FirebaseDatabase.getInstance();
//        DatabaseReference reference = root.getReference("users");
//        reference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                if (snapshot.child("users").child("email").exists()) {
//                    System.out.println("Email already exists.");
//                    emailValid[0] = false;
//                }
//            }
//           }
//                    return false;
//        }
//            root.child("users").child(email).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>()
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("firebase", "Error getting data", task.getException());
//                }
//                else {
//                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                }
//             }
//            });
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        return emailValid[0];

    // Get the email and password and talk to the firebase to see email and password are matched
    static private boolean checkValidLogin(String email, String password){
        //use hash function here to hash the password raw value
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(email);
        final Object[] userData = new Object[1];
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("getting user", "DocumentSnapshot data: " + document.getData());
                        userData[0] = document.getData();
                    } else {
                        Log.d("getting user", "No such document");
                    }
                } else {
                    Log.d("getting user", "get failed with ", task.getException());
                }
            }
        });
        //TODO UNHASH PASSOWRD TO COMPARE
        if(userData[0] == password)
        {
            return true;
        }
        return false;
    }
}
