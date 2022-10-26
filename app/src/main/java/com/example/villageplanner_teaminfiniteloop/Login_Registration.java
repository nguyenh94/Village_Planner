package com.example.villageplanner_teaminfiniteloop;
import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class Login_Registration {
    // https://www.geeksforgeeks.org/how-to-save-data-to-the-firebase-realtime-database-in-android/
    static public void Register(String email, String password, String name, Image photo) throws Exception{
        // implement random id generator or want to use email as user id?
        String uniqueId = UUID.randomUUID().toString();
        checkValidRegistration(email);
//        TODO SETUP FIRESTORE AUTH and incorporate user object
//        if (validReg) {
//            User user = new User(uniqueId, email, name, photo, password, null);
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//            CollectionReference users = db.collection("users");
//
//            Map<String, Object> userInfo = new HashMap<>();
//            userInfo.put("uniqueId", uniqueId);
//            userInfo.put("name", name);
//            userInfo.put("email", email);
//            userInfo.put("password", hashPassword(password));
//            userInfo.put("photo", photo);
//
//            users.document(email).set(userInfo);
//        }
    }

    static public void Login(String email, String password) throws Exception{

    }

    static public String hashPassword(String rawValue){
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

    public static boolean afterLogin(boolean result) {
        // deal with false
        System.out.println(result);
        return result;
    }
    // Get the email and talk to the firebase to see email has been already registered
    static private void checkValidRegistration(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(email);
        final Object[] userData = new Object[1];
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                System.out.println("line 80");
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
    }

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
