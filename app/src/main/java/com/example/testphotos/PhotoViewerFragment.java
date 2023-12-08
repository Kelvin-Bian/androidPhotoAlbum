package com.example.testphotos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testphotos.classes.Album;
import com.example.testphotos.classes.Photo;
import com.example.testphotos.classes.Tag;
import com.example.testphotos.classes.User;
import com.example.testphotos.databinding.PhotoviewerBinding;

import java.util.ArrayList;

public class PhotoViewerFragment extends Fragment {
    private PhotoviewerBinding binding;
    private RecyclerView locationTagValues;
    private RecyclerView personTagValues;
    private Photo p;
    private ArrayList<Tag> locationTags;
    private ArrayList<Tag> personTags;
    private Button addLocationTagButton;
    private Button addPersonTagButton;
    private TagAdapter locationTagAdapter;
    private TagAdapter personTagAdapter;

    private Photo photo;
    private User user;
    private Album album;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = PhotoviewerBinding.inflate(inflater, container, false);
//        this line doesn't work anymore as we use URIs for android phone photos instead of filepath
//        p = new Photo("/Users/jessicaluo/Desktop/android/app/src/main/res/drawable/river.png");
        locationTags = p.getTagsByName(s-> s.equals("location"));
        personTags= p.getTagsByName(s->s.equals("person"));

        locationTagValues = binding.locationTagValues;
        locationTagValues.setLayoutManager(new LinearLayoutManager(getContext()));
        locationTagAdapter = new TagAdapter(locationTags);
        locationTagValues.setAdapter(locationTagAdapter);
        setClickListenerEditTag(locationTagAdapter, true);
        setClickListenerDeleteTag(locationTagAdapter, true);

        personTagValues =binding.personTagValues;
        personTagValues.setLayoutManager(new LinearLayoutManager(getContext()));
        personTagAdapter = new TagAdapter(personTags);
        personTagValues.setAdapter(personTagAdapter);
        setClickListenerEditTag(personTagAdapter, false);
        setClickListenerDeleteTag(personTagAdapter, false);

        addLocationTagButton = binding.addLocationTagButton;
        setClickListenerAddTag(addLocationTagButton, true);

        addPersonTagButton = binding.addPersonTagButton;
        setClickListenerAddTag(addPersonTagButton, false);
        return binding.getRoot();
    }

    public void setClickListenerDeleteTag(TagAdapter adapter, boolean location){
            adapter.setOnItemClickListenerDelete(new TagAdapter.OnItemClickListener() {
                public void onItemClick(int position, String oldValue) {
                    // Handle the button click, you have access to the position and associated text
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Are you sure you want to delete \""+oldValue+"\"");

                    // Set up the buttons
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Tag old = (location)? new Tag("location", oldValue): new Tag("person", oldValue);
                            p.deleteTag(old);
                            locationTags.remove(old);
                            locationTagAdapter.setTags(locationTags);
                            locationTagAdapter.notifyDataSetChanged();
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
    }
    public void setClickListenerEditTag(TagAdapter adapter, boolean location){
        if(location)
            adapter.setOnItemClickListenerEdit(new TagAdapter.OnItemClickListener() {
                public void onItemClick(int position, String oldValue) {
                    // Handle the button click, you have access to the position and associated text
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Edit value for location tag. Selected value is \""+oldValue+"\"");

                    // Set up the input
                    final EditText input = new EditText(getActivity());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String tagValue = input.getText().toString();
                            Tag newTag = new Tag("location", tagValue);
                            if (!tagValue.isEmpty()) {
                                boolean success = p.addTag(newTag);
                                if (!success) {
                                    Toast.makeText(getActivity(), "This tag value already exists", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Tag old = new Tag("location", oldValue);
                                    p.deleteTag(old);
                                    locationTags.remove(old);
                                    locationTags.add(newTag);
                                    locationTagAdapter.setTags(locationTags);
                                    locationTagAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Tag value cannot be empty", Toast.LENGTH_SHORT).show();
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
        else
        if(location)
            adapter.setOnItemClickListenerEdit(new TagAdapter.OnItemClickListener() {
                public void onItemClick(int position, String oldValue) {
                    // Handle the button click, you have access to the position and associated text
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Edit value for location tag. Selected value is \""+oldValue+"\"");

                    // Set up the input
                    final EditText input = new EditText(getActivity());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String tagValue = input.getText().toString();
                            Tag newTag = new Tag("person", tagValue);
                            if (!tagValue.isEmpty()) {
                                boolean success = p.addTag(newTag);
                                if (!success) {
                                    Toast.makeText(getActivity(), "This tag value already exists", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Tag old = new Tag("person", oldValue);
                                    p.deleteTag(old);
                                    locationTags.remove(old);
                                    locationTags.add(newTag);
                                    locationTagAdapter.setTags(locationTags);
                                    locationTagAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Tag value cannot be empty", Toast.LENGTH_SHORT).show();
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
    }
    public void setClickListenerAddTag(Button b, boolean location){
        if(location)
            b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Add Location Tag");

                    // Set up the input
                    final EditText input = new EditText(getActivity());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String tagValue = input.getText().toString();
                            if (!tagValue.isEmpty()) {
                                boolean success = p.addTag(new Tag("location", tagValue));
                                if (!success) {
                                    Toast.makeText(getActivity(), "This tag value already exists", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    locationTags.add(new Tag("location", tagValue));
                                    locationTagAdapter.setTags(locationTags);
                                    locationTagAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Tag value cannot be empty", Toast.LENGTH_SHORT).show();
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
        else
            b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Add Person Tag");

                    // Set up the input
                    final EditText input = new EditText(getActivity());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String tagValue = input.getText().toString();
                            if (!tagValue.isEmpty()) {
                                boolean success = p.addTag(new Tag("person", tagValue));
                                if (!success) {
                                    Toast.makeText(getActivity(), "This tag value already exists", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    personTags.add(new Tag("person", tagValue));
                                    personTagAdapter.setTags(personTags);
                                    personTagAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Tag value cannot be empty", Toast.LENGTH_SHORT).show();
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
    }
}