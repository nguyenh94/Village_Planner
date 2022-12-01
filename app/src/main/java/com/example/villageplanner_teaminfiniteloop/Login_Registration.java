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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login_Registration {
    // https://www.geeksforgeeks.org/how-to-save-data-to-the-firebase-realtime-database-in-android/
    static public void Register(String email, String password, String name, Image photo) throws Exception{

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

    static public Boolean checkEmailFormat(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    static public Boolean checkPasswordStrength(String pass) {
        int upChar = 0;
        int loChar = 0;
        int num = 0;

        if (pass.length() <= 8) {
            return false;
        }
        for (int i=0; i<pass.length(); i++) {
            char ch = pass.charAt(i);
            if (Character.isUpperCase(ch)) {
                upChar++;
            } else if (Character.isLowerCase(ch)) {
                loChar++;
            } else if (Character.isDigit(ch)) {
                num++;
            }
        }
        if (upChar < 1 && loChar < 1 && num < 1) {
            return false;
        }
        return true;
    }
}
