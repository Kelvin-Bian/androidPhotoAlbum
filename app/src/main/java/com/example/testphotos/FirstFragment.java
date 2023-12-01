package com.example.testphotos;

        import android.os.Bundle;
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


        import java.util.ArrayList;
        import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private ListView albumList;
    private ArrayAdapter<String> adapter;
    private List<String> albumNames;
    private User user;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        albumList = binding.albumList; // Replace with your ListView ID
        albumNames = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, albumNames);
        albumList.setAdapter(adapter);

        // Retrieve the object from the arguments bundle
        Bundle args = getArguments();
        if (args != null) {
            User user = (User) args.getSerializable("user"); // Replace "object_key" with the same key used in MainActivity
            // Now you can use the yourObject in your Fragment
        }

        return binding.getRoot();
    }

    private void createAlbum(String albumName) {
        albumNames.add(albumName);
        adapter.notifyDataSetChanged();
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
                // Handle the click for openAlbumButton
                Toast.makeText(getActivity(), "Open Album button clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up the listener for the deleteAlbumButton
        binding.deleteAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click for deleteAlbumButton
                Toast.makeText(getActivity(), "Delete Album button clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up the listener for the renameAlbumButton
        binding.renameAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click for renameAlbumButton
                Toast.makeText(getActivity(), "Rename Album button clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        albumList.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedAlbum = albumNames.get(position);
            // Implement what happens when an album is clicked
            Toast.makeText(getActivity(), "Selected Album: " + selectedAlbum, Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Set the binding to null when the view is destroyed
        binding = null;
    }

}
