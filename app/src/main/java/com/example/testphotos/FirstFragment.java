package com.example.testphotos;

        import android.content.Context;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.Toast;
        import androidx.annotation.NonNull;
        import androidx.fragment.app.Fragment;

        import com.example.testphotos.classes.*;
        import com.example.testphotos.databinding.FragmentFirstBinding;

        import android.app.AlertDialog;
        import android.widget.EditText;
        import android.content.DialogInterface;
        import android.text.InputType;
        import androidx.navigation.Navigation;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;


        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.ObjectOutputStream;
        import java.util.ArrayList;
        import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private ListView albumList;
    private AlbumListAdapter adapter;
    private User user;
    private Album selectedAlbum;
    private int selectedPosition = -1;

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
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        albumList = binding.albumList;


        // Retrieve the object from the arguments bundle
        Bundle args = getArguments();
        if (args != null && args.containsKey("user")) {
            user = (User) args.getSerializable("user");
            Log.d("FirstFragment", "User object received from arguments.");
        } else {
            user = new User();
            Log.d("FirstFragment", "User object not found in arguments, created new User object.");
        }

//        albumNames = new ArrayList<>();
        adapter = new AlbumListAdapter(getContext(), user.getAlbumNames());
        albumList.setAdapter(adapter);

        return binding.getRoot();
    }

    private void createAlbum(String albumName) {
        if (user.addAlbum(albumName)) {
            user.addAlbum(albumName);
            adapter.clear();
            adapter.addAll(user.getAlbumNames());
            adapter.notifyDataSetChanged();
            saveUser(user);
        } else {
            Toast.makeText(getActivity(), "Album name already exists", Toast.LENGTH_SHORT).show();
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up the listener for the createAlbumButton
        binding.createAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Create New Album");

                // Set up the input
                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String albumName = input.getText().toString();
                        if (!albumName.isEmpty()) {
                            createAlbum(albumName);
                        } else {
                            Toast.makeText(getActivity(), "Album name cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }

        });

        // Set up the listener for the openAlbumButton
        binding.openAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to AlbumFragment
                if(selectedAlbum != null) {
                    FirstFragmentDirections.ActionFirstFragmentToSecondFragment action =
                            FirstFragmentDirections.actionFirstFragmentToSecondFragment(user, selectedAlbum.getName());
                    Navigation.findNavController(view).navigate(action);
                }
                else {
                    Toast.makeText(getActivity(), "No Album Selected!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up the listener for the deleteAlbumButton
        binding.deleteAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedAlbum != null && user != null) {
                    // Delete the album
                    boolean isDeleted = user.deleteAlbum(selectedAlbum.getName());
                    if (isDeleted) {
                        // Update the adapter and ListView
                        adapter.clear();
                        adapter.addAll(user.getAlbumNames());
                        adapter.notifyDataSetChanged();

                        // Save the updated user object
                        saveUser(user);

                        // Clear the selected album as it has been deleted
                        selectedAlbum = null;

                        Toast.makeText(getActivity(), "Album deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed to delete album", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No album selected or user data not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up the listener for the renameAlbumButton
        binding.renameAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedAlbum == null) {
                    Toast.makeText(getActivity(), "No album selected", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Rename Album");

                // Set up the input
                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = input.getText().toString();
                        if (!newName.isEmpty()) {
                            boolean isRenamed = user.renameAlbum(selectedAlbum, newName);
                            if (isRenamed) {
                                // Update the adapter and ListView
                                adapter.clear();
                                adapter.addAll(user.getAlbumNames());
                                adapter.notifyDataSetChanged();

                                // Save the updated user object
                                saveUser(user);

                                Toast.makeText(getActivity(), "Album renamed successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Failed to rename album", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Album name cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        albumList.setOnItemClickListener((parent, view1, position, id) -> {
            if (user != null) {
                selectedAlbum = user.findAlbum(user.getAlbumNames().get(position));
                adapter.setSelectedPosition(position);
                Toast.makeText(getActivity(), "Selected Album!", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getActivity(), "User data is not available", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Set the binding to null when the view is destroyed
        binding = null;
    }

}
