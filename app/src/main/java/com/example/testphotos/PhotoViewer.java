package com.example.testphotos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testphotos.classes.Photo;
import com.example.testphotos.classes.Tag;
import com.example.testphotos.databinding.FragmentFirstBinding;

import java.util.ArrayList;

public class PhotoViewer extends Fragment {
    private FragmentFirstBinding binding;

    private RecyclerView locationTagValues;
    private RecyclerView personTagValues;
    private Photo p;
    private ArrayList<Tag> locationTags;
    private ArrayList<Tag> personTags;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        locationTagValues = container.findViewById(R.id.locationTagValues);
        locationTagValues.setAdapter(new TagAdapter());

        personTagValues = container.findViewById(R.id.personTagValues);
        personTagValues.setAdapter(new TagAdapter());

        locationTags = new ArrayList<>();
        personTags= new ArrayList<>();
        return binding.getRoot();
    }
}