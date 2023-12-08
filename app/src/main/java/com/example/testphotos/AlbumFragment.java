package com.example.testphotos;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testphotos.classes.Album;
import com.example.testphotos.classes.Photo;
import com.example.testphotos.classes.User;
import com.example.testphotos.databinding.FragmentAlbumBinding;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class AlbumFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private FragmentAlbumBinding binding;
    private User user;
    private Album album;
    private String albumName;
    private PhotoAdapter adapter;
    private Photo selectedPhoto;

    // Helper method to serialize the user object
    private void saveUser(User user) {
        try {
            FileOutputStream fileOut = getContext().openFileOutput("user.ser", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(user);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        binding = FragmentAlbumBinding.inflate(inflater, container, false);



        // Retrieve arguments passed to this fragment
        if (getArguments() != null) {
            AlbumFragmentArgs args = AlbumFragmentArgs.fromBundle(getArguments());
            user = args.getUser();  // Assuming User class is Parcelable or Serializable
            albumName = args.getAlbumName();

            // Here, you can also initialize your Album object based on the albumName and user
            // For example, you could retrieve the album from the user object
            album = user.findAlbum(albumName);
        }

        // Set up the ListView with PhotoAdapter
        if (album != null) {
            adapter = new PhotoAdapter(getContext(), album.getPhotos());
            binding.photoList.setAdapter(adapter);
        }

        return binding.getRoot();
    }
    private void updateListView() {
        if (album != null && adapter != null) {
            adapter.clear();
            adapter.addAll(album.getPhotos());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            // Take persistable URI permission so you can use this URI after app restarts
            try {
                getActivity().getContentResolver().takePersistableUriPermission(
                        imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                );
            } catch (SecurityException e) {
                e.printStackTrace();
            }

            // Store the URI in SharedPreferences or a database for later use
            SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("imageUri", imageUri.toString());
            editor.apply();

            // Rest of your code for handling the new photo
            Photo newPhoto = new Photo(imageUri);
            if (album != null) {
                album.addPhoto(newPhoto);
                updateListView();
                saveUser(user);
            }
        } else {
            Toast.makeText(getContext(), "You haven't picked an image",Toast.LENGTH_LONG).show();
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.addPhotoButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, you can proceed with your logic to pick an image
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);

                Toast.makeText(getContext(), "Add Photo", Toast.LENGTH_SHORT).show();
            } else {
                // Permission is not granted, request it
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_CODE);
                Toast.makeText(getContext(), "Requested Permission", Toast.LENGTH_SHORT).show();
            }
        });

        binding.movePhotoButton.setOnClickListener(v -> {
            if (user != null && selectedPhoto != null) {
                // Create an array of album names
                String[] albumNames = user.getAlbums().stream().map(Album::getName).toArray(String[]::new);

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Select an Album");

                builder.setItems(albumNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Album selectedAlbum = user.getAlbums().get(which);

                        // Move the photo to the selected album and remove it from the current album
                        if (selectedAlbum != null) {
                            album.deletePhoto(selectedPhoto);
                            selectedAlbum.addPhoto(selectedPhoto);
                            updateListView();
                            saveUser(user);
                            Toast.makeText(getContext(), "Photo moved to " + selectedAlbum.getName(), Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        binding.deletePhotoButton.setOnClickListener(v -> {
            if (album != null && selectedPhoto != null) {
                album.deletePhoto(selectedPhoto); // Remove the selected photo from the album
                updateListView(); // Update the ListView
                saveUser(user); // Save the updated user object
                selectedPhoto = null; // Reset the selected photo
            }
            Toast.makeText(getContext(), "Delete Photo", Toast.LENGTH_SHORT).show();

        });

        binding.openPhotoButton.setOnClickListener(v -> {
            if (user != null && album != null && selectedPhoto != null) {
                // Create a bundle with the necessary arguments
                Bundle args = new Bundle();
                args.putSerializable("user", user);
                args.putSerializable("album", album);
                args.putSerializable("selectedPhoto", selectedPhoto);

                // Navigate to PhotoViewerFragment with the arguments
                NavDirections action = AlbumFragmentDirections.actionAlbumFragmentToPhotoViewerFragment(user, album, selectedPhoto, adapter.getPosition(selectedPhoto));
                NavHostFragment.findNavController(this).navigate(action);
            }
            Toast.makeText(getContext(), "Open Photo", Toast.LENGTH_SHORT).show();
        });


        binding.photoList.setOnItemClickListener((parent, v, position, id) -> {
            selectedPhoto = adapter.getItem(position);
            adapter.setSelectedPosition(position);
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}