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

public class PhotoAdapter extends ArrayAdapter<Photo> {
    public PhotoAdapter(Context context, ArrayList<Photo> photos) {
        super(context, 0, photos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.photo_list_item, parent, false);
        }
        // Get the data item for this position
        Photo photo = getItem(position);

        // Lookup view for data population
        ImageView imageView = convertView.findViewById(R.id.imageView);

        // Populate the data into the template view using the data object
        // Assuming Photo class has a method to get the image (e.g., as a Bitmap or drawable resource ID)

        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(photo.getUri()));
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        TextView textView = convertView.findViewById(R.id.photoNameTextView);

        textView.setText(photo.getFileName());

        // Return the completed view to render on screen
        return convertView;
    }
}
