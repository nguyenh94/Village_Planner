package com.example.villageplanner_teaminfiniteloop.ui.me;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.villageplanner_teaminfiniteloop.LoginActivity;
import com.example.villageplanner_teaminfiniteloop.R;
import com.example.villageplanner_teaminfiniteloop.RestaurantDetail;
import com.example.villageplanner_teaminfiniteloop.TabBarActivity;
import com.example.villageplanner_teaminfiniteloop.User;
import com.example.villageplanner_teaminfiniteloop.databinding.FragmentMeBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

public class MeFragment extends Fragment {
    private ImageView profilePic;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference imageRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        View view = inflater.inflate(R.layout.fragment_me, null);
        TextView userNameTextView = (TextView) view.findViewById(R.id.userName);
        TextView userEmailTextView = (TextView) view.findViewById(R.id.userEmail);
        Button logoutButton = (Button) view.findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutButtonPressed(view);
            }
        });
        profilePic = view.findViewById(R.id.profilePic);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        imageRef = storageReference.child("images/" + User.currentUserEmail);

        userNameTextView.setText(User.currentUserName);
        userEmailTextView.setText(User.currentUserEmail);

        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    Glide.with(MeFragment.this).load(uri).into(profilePic);
                } catch (Exception e){
                    System.out.println(e);
                }
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                launcher.launch(intent);
            }
        });

        return view;
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK
                        && result.getData() != null) {
                    Uri photoUri = result.getData().getData();
                    //use photoUri here
                    profilePic.setImageURI(photoUri);
                    uploadPicture(photoUri);
                }
            }
    );

    private void uploadPicture(Uri photoUri) {
        // While the file names are the same, the references point to different files
        imageRef.getName().equals(imageRef.getName());    // true
        imageRef.getPath().equals(imageRef.getPath());    // false
        imageRef.putFile(photoUri);

        // store the user's photo uri in firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference users = db.collection("users");
        users.document(User.currentUserEmail).update("photo", photoUri);
    }

    public void logoutButtonPressed(View view) {
        // Log user out and move to login page
        Intent myIntent = new Intent(getActivity(), LoginActivity.class);
        MeFragment.this.startActivity(myIntent);
    }
}