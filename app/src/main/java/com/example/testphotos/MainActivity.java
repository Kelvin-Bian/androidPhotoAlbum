package com.example.testphotos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.testphotos.classes.*;
import com.example.testphotos.databinding.PhotoviewerBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.testphotos.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private PhotoviewerBinding testbinding;
    private User user;


    // Helper method to serialize the user object
    private void saveUser(User user) {
        try {
            FileOutputStream fileOut = openFileOutput("user.ser", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(user);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to deserialize the user object
    private User loadUser() {
        User loadedUser = null;
        try {
            FileInputStream fileIn = openFileInput("user.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            loadedUser = (User) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loadedUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.photoviewer);
//        testbinding = PhotoviewerBinding.inflate(getLayoutInflater());
//        setContentView(testbinding.getRoot());
//
//        if (user == null) {
//            user = new User();
//        }
//        // Create a bundle to hold the variable
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("user", user); // Replace "key" with a unique identifier

//        //testing photoviewer
//        PhotoViewer photoViewer = new PhotoViewer();
//        photoViewer.setArguments(bundle);

//      actual main class
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        user = loadUser();
        if (user == null) {
            user = new User();
        }
        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        if (!(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED)) {

            // Permission is not granted, request it
            requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
            Toast.makeText(this, "Requested Permission", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        // Conditionally navigate to FirstFragment with the User object
        if (navController.getCurrentDestination() != null && navController.getCurrentDestination().getId() == R.id.FirstFragment) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", user);
            navController.navigate(R.id.FirstFragment, bundle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up addPhotoButton, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}