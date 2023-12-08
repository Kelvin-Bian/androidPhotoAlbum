package com.example.testphotos;

import android.app.Activity;
import android.content.Intent;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testphotos.classes.Album;
import com.example.testphotos.classes.Photo;
import com.example.testphotos.classes.User;
import com.example.testphotos.databinding.FragmentAlbumBinding;

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

            // Assuming you have a constructor in your Photo class that accepts a URI
            Photo newPhoto = new Photo(imageUri);

            // Add the new photo to the album
            if (album != null) {
                album.addPhoto(newPhoto); // Make sure your Album class has a method to add photos
                updateListView(); // Update the list view to reflect the new photo addition
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
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
            // Add your logic here
            Toast.makeText(getContext(), "Move Photo", Toast.LENGTH_SHORT).show();
        });

        binding.deletePhotoButton.setOnClickListener(v -> {
            // Add your logic here
            Toast.makeText(getContext(), "Delete Photo", Toast.LENGTH_SHORT).show();
        });

        binding.homeButton.setOnClickListener(v -> {
            if (user != null) {
                AlbumFragmentDirections.ActionAlbumFragmentToFirstFragment action =
                        AlbumFragmentDirections.actionAlbumFragmentToFirstFragment(user);
                NavHostFragment.findNavController(AlbumFragment.this).navigate((NavDirections) action);
            }
        });

        binding.slideshowButton.setOnClickListener(v -> {
            // Add your logic here
            Toast.makeText(getContext(), "Slideshow", Toast.LENGTH_SHORT).show();
        });

        binding.openButton.setOnClickListener(v -> {
            // Add your logic here
            Toast.makeText(getContext(), "Open Photo", Toast.LENGTH_SHORT).show();
        });


        binding.photoList.setOnItemClickListener((parent, v, position, id) -> {
            // Add your logic here
            Photo p = album.getPhotos().get(position);
            AlbumFragmentDirections.ActionAlbumFragmentToPhotoViewerFragment action =
                    AlbumFragmentDirections.actionAlbumFragmentToPhotoViewerFragment(album, p, position);
            NavHostFragment.findNavController(AlbumFragment.this).navigate((NavDirections) action);
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}