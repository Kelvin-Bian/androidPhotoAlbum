package com.example.testphotos.classes;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

public class Album implements Serializable{
    private HashMap<String, Photo> photos; // Changed to String key
    private String name;

    public Album(String name) {
        this.name = name;
        photos = new HashMap<>();
    }

    public ArrayList<Photo> getPhotos() {
        return new ArrayList<>(photos.values());
    }

    public boolean addPhoto(Photo p) {
        String uriString = p.getUri().toString(); // Convert Uri to String
        if (photos.containsKey(uriString)) return false;
        photos.put(uriString, p);
        return true;
    }
    public int index(Photo p){
        ArrayList<Photo> photosList = getPhotos();
        return photosList.indexOf(p);
    }

    public boolean deletePhoto(Photo p) {
        String uriString = p.getUri().toString(); // Convert Uri to String
        return photos.remove(uriString) != null;
    }
    public ArrayList<Tag> getTagsByName(Predicate<String> matchingName){
        ArrayList<Tag> tags = new ArrayList<>();
        for(Photo p: getPhotos()){
            for(Tag t: p.getTagsByName(matchingName)){
                tags.add(t);
            }
        }
        return tags;
    }
    public String getName() {
        return name;
    }

    public void setName(String n) {
        this.name = n;
    }

    public int numPhotos() {
        return photos.size();
    }

    public Photo findPhoto(Uri uri) {
        return photos.get(uri.toString()); // Convert Uri to String
    }

    /**
     * Checks if this Album is equal to another object.
     *
     * @param obj The object to compare.
     * @return true if the objects are equal, false otherwise.
     */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Album a = (Album) obj;
        return a.getName().equals(name);
    }

    /**
     * Converts the Album object to a string representation (the name of the album).
     *
     * @return The name of the album.
     */
    public String toString() {
        return name;
    }
}
