package com.example.testphotos;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.testphotos.classes.*;

public class SearchFragment extends Fragment {

    private User user;
    private Spinner tag1TypeSpinner, tag2TypeSpinner;
    private AutoCompleteTextView tag1ValueEditText, tag2ValueEditText;
    private Button andButton, orButton;
    private boolean andSearch;
    private ListView searchOutputList;
    private Album album;
    private PhotoAdapter photoAdapter;

    private Button searchButton;

    @Override
    public void onResume() {
        super.onResume();
        andSearch = false;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        if (getArguments() != null) {
            SearchFragmentArgs args = SearchFragmentArgs.fromBundle(getArguments());
            user = args.getUser();

            // Initialize components
            tag1TypeSpinner = view.findViewById(R.id.tag1type);
            tag2TypeSpinner = view.findViewById(R.id.tag2type);
            tag1ValueEditText = view.findViewById(R.id.tag1value);
            tag2ValueEditText = view.findViewById(R.id.tag2value);
            andButton = view.findViewById(R.id.andButton);
            orButton = view.findViewById(R.id.orButton);
            searchOutputList = view.findViewById(R.id.searchOutputList);
            searchButton = view.findViewById(R.id.searchButton);

            // set up spinners
            String[] spinnerValues = new String[]{"person", "location"};

            // Create ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinnerValues);

            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Apply the adapter to the spinner
            tag1TypeSpinner.setAdapter(adapter);
            tag2TypeSpinner.setAdapter(adapter);
            album = new Album("testnamecrazynew");
            photoAdapter = new PhotoAdapter(getContext(), album.getPhotos());
            searchOutputList.setAdapter(photoAdapter);

            // Set up listeners for buttons
            andButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    andSearch = true;
                    // Implement AND logic
                    Toast.makeText(getActivity(), "AND Clicked", Toast.LENGTH_SHORT).show();
                }
            });

            orButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    andSearch = false;
                    // Implement OR logic
                    Toast.makeText(getActivity(), "OR Clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }
        // Setting onItemClickListener for the ListView
        searchOutputList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Extracting the selected Photo and its index
                Photo selectedPhoto = (Photo) parent.getItemAtPosition(position);
                int selectedIndex = album.index(selectedPhoto);

                NavDirections action = SearchFragmentDirections.actionSearchFragmentToPhotoViewerFragment(user, album, selectedPhoto, selectedIndex);
                Navigation.findNavController(view).navigate(action);
            }
        });

//        TextWatcher textWatcher = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                search();
//            }
//        };
//        tag1ValueEditText.addTextChangedListener(textWatcher);
//        tag2ValueEditText.addTextChangedListener(textWatcher);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(tag1TypeSpinner.getSelectedItem()!= null) {
                    String tag1Name = tag1TypeSpinner.getSelectedItem().toString().trim();
                    ArrayAdapter<Tag> tag1Adapter = new ArrayAdapter<Tag>
                            (getContext(), android.R.layout.select_dialog_item, user.getAllTagsByName(name -> name.equals(tag1Name)));
                    tag1ValueEditText.setThreshold(1);
                    tag1ValueEditText.setAdapter(tag1Adapter);
                }
                if(tag2TypeSpinner.getSelectedItem()!= null){
                    String tag2Name = tag2TypeSpinner.getSelectedItem().toString().trim();
                    ArrayAdapter<Tag> tag2Adapter = new ArrayAdapter<Tag>
                            (getContext(), android.R.layout.select_dialog_item, user.getAllTagsByName(name -> name.equals(tag2Name)));
                    tag2ValueEditText.setThreshold(1);
                    tag2ValueEditText.setAdapter(tag2Adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        };
        tag1TypeSpinner.setOnItemSelectedListener(listener);
        tag2TypeSpinner.setOnItemSelectedListener(listener);
        searchButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                search();
            }
        });
        return view;
    }
    private void search() {
        // Retrieve the tag values
        String tag1Name = tag1TypeSpinner.getSelectedItem().toString().trim();
        String tag1Value = tag1ValueEditText.getText().toString().trim().toLowerCase();
        String tag2Name = tag2TypeSpinner.getSelectedItem().toString().trim();
        String tag2Value = tag2ValueEditText.getText().toString().trim().toLowerCase();

        // Check if both tags are provided
        boolean twoTags = !tag1Value.isEmpty() && !tag2Value.isEmpty();

        // Create a new album for search results
        Album newAlbum = new Album("searchAlbum");

        for (Album a : user.getAlbums()) {
            for (Photo p : a.getPhotos()) {
                boolean matchesTag1 = false;
                boolean matchesTag2 = false;

                for (Tag t : p.getTags()) {
                    String tagName = t.getName().trim().toLowerCase();
                    String tagValue = t.getValue().trim().toLowerCase();

                    // Debugging logs
                    Log.d("searchh", "Comparing: [" + tagName + "] with [" + tag1Name + "] and [" + tagValue + "] with [" + tag1Value + "]");
                    Log.d("searchh", "Comparing: [" + tagName + "] with [" + tag2Name + "] and [" + tagValue + "] with [" + tag2Value + "]");
                    if (!tag1Value.isEmpty() && tagName.equals(tag1Name) && tagValue.startsWith(tag1Value)) {
                        matchesTag1 = true;
                    }
                    if (!tag2Value.isEmpty() && tagName.equals(tag2Name) && tagValue.startsWith(tag2Value)) {
                        matchesTag2 = true;
                    }
                }

                // Add photo to album if it matches the search criteria
                if ((twoTags && matchesTag1 && matchesTag2) || (twoTags && !andSearch && (matchesTag1 || matchesTag2)) ||  (!twoTags && (matchesTag1 || matchesTag2))) {
                    newAlbum.addPhoto(p);
                }
            }
        }
        album = newAlbum;
        updateListView();
    }

    private void updateListView() {
        if (album != null && photoAdapter != null) {
            photoAdapter.clear();
            photoAdapter.addAll(album.getPhotos());
            photoAdapter.notifyDataSetChanged();
        }
    }
}