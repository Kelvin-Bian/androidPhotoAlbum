package com.example.testphotos.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import java.io.Serializable;
import java.util.ArrayList;

public class Photo implements Serializable {
    private transient Uri uri; // 'transient' as Uri is not Serializable
    private String uriString; // Used for serialization
    private ArrayList<Tag> tags;

    private Context context;

    public Photo(Uri uri) {
        this.uri = uri;
        this.uriString = uri.toString(); // Convert Uri to String for serialization
        this.tags = new ArrayList<>();
    }

    public Bitmap getBitmap() {
        if (uri == null && uriString != null) {
            uri = Uri.parse(uriString); // Convert String back to Uri if needed
        }
        // Replace with code to decode Bitmap from Uri
        // Example: return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        return null;
    }


    public Uri getUri() {
        if (uri == null && uriString != null) {
            uri = Uri.parse(uriString); // Convert String back to Uri if needed
        }
        return uri;
    }

    public String getFileName() {
        // Logic to extract file name from Uri
        // This is just an example and may need to be adapted depending on the Uri's format
        if (uri != null) {
            String path = uri.getPath();
            int cut = path.lastIndexOf('/');
            if (cut != -1) {
                return path.substring(cut + 1);
            }
        }
        return null;
    }

    /**
     * Adds a Tag to the photo's list of tags.
     *
     * @param t The Tag to be added.
     * @return true if the Tag was added successfully, false if a tag with the same name and value already exists.
     */
    public boolean addTag(Tag t) {
        if (tags.indexOf(t) != -1) { // Tag with the same name and value exists already
            return false;
        }
        tags.add(t);
        return true;
    }

    /**
     * Retrieves the list of tags associated with the photo.
     *
     * @return An ArrayList containing the tags.
     */
    public ArrayList<Tag> getTags() {
        return tags;
    }

    /**
     * Sets the list of tags associated with the photo.
     *
     * @param tags The ArrayList of tags to be set.
     */
    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Deletes a Tag from the photo's list of tags.
     *
     * @param t The Tag to be deleted.
     */
    public void deleteTag(Tag t) {
        tags.remove(t);
    }


    /**
     * Checks if a tag with a given name and value already exists for the photo.
     *
     * @param name  The name of the tag.
     * @param value The value of the tag.
     * @return true if the tag exists, false otherwise.
     */
    public boolean tagExists(String name, String value) {
        Tag temp = new Tag(name, value);
        return tags.indexOf(temp) != -1;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        uriString = uri == null ? null : uri.toString();
        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        uri = uriString == null ? null : Uri.parse(uriString);
    }
}
