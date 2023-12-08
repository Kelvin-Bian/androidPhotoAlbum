package com.example.testphotos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.testphotos.classes.Photo;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AlbumListAdapter extends ArrayAdapter<String> {
    public AlbumListAdapter(Context context, ArrayList<String> albums) {
        super(context, 0, albums);
    }

    private int selectedPosition = -1;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.album_list_item, parent, false);
        }
        // Get the data item for this position
        String albumName = getItem(position);


        //diff bg color if selected by user
        if (position == selectedPosition) {
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.selected_item_bg));
        } else {
            convertView.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
        }

        TextView textView = convertView.findViewById(R.id.albumName);

        textView.setText(albumName);

        // Return the completed view to render on screen
        return convertView;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

}