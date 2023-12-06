package com.example.testphotos;

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


        import java.util.ArrayList;
        import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private ListView albumList;
    private ArrayAdapter<String> adapter;
//    private List<String> albumNames;
    private User user;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        albumList = binding.albumList; // Replace with your ListView ID

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
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, user.getAlbumNames());
        albumList.setAdapter(adapter);



        return binding.getRoot();
    }

    private void createAlbum(String albumName) {
        if (user.addAlbum(albumName)) {
            user.addAlbum(albumName);
            adapter.clear();
            adapter.addAll(user.getAlbumNames());
            adapter.notifyDataSetChanged();
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
            if (user != null) {
                String selectedAlbum = user.getAlbumNames().get(position);

                // Navigate to SecondFragment
                FirstFragmentDirections.ActionFirstFragmentToSecondFragment action =
                        FirstFragmentDirections.actionFirstFragmentToSecondFragment(user, selectedAlbum);
                Navigation.findNavController(view).navigate(action);
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
