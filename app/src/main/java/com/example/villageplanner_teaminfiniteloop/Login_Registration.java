package com.example.villageplanner_teaminfiniteloop;
import android.media.Image;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login_Registration {
    // https://www.geeksforgeeks.org/how-to-save-data-to-the-firebase-realtime-database-in-android/
    static public void Register(String email, String password, String name, Image photo) throws Exception{

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
    static private boolean checkValidRegistration(String email) {

        return true;
    }

    // Get the email and password and talk to the firebase to see email and password are matched
    static private boolean checkValidLogin(String email, String password){
        //use hash function here to hash the password raw value
        return true;
    }
}
