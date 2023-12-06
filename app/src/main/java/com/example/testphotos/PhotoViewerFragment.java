package com.example.testphotos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testphotos.classes.Photo;
import com.example.testphotos.classes.Tag;
import com.example.testphotos.databinding.PhotoviewerBinding;

import java.util.ArrayList;

public class PhotoViewer extends Fragment {
    private PhotoviewerBinding binding;

    private RecyclerView locationTagValues;
    private RecyclerView personTagValues;
    private Photo p;
    private ArrayList<Tag> locationTags;
    private ArrayList<Tag> personTags;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = PhotoviewerBinding.inflate(inflater, container, false);
        locationTagValues = binding.locationTagValues;
        locationTagValues.setLayoutManager(new LinearLayoutManager(requireContext()));
        locationTagValues.setAdapter(new TagAdapter());

        personTagValues =binding.personTagValues;
        personTagValues.setLayoutManager(new LinearLayoutManager(requireContext()));
        personTagValues.setAdapter(new TagAdapter());

        locationTags = new ArrayList<>();
        personTags= new ArrayList<>();
        return binding.getRoot();
    }
}