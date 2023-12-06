package com.example.testphotos.classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
public class Album implements Serializable{
    /**
     * HashMap to store photos, where the key is the photo path and the value is the corresponding Photo object.
     */
    private HashMap<String, Photo> photos;

    /**
     * The name of the album.
     */
    private String name;

    /**
     * The start date of the album (earliest date among photos).
     */
    private LocalDate startDate;

    /**
     * The end date of the album (latest date among photos).
     */
    private LocalDate endDate;

    /**
     * Constructs a new Album with the given name.
     *
     * @param name The name of the album.
     */
    public Album(String name) {
        this.name = name;
        photos = new HashMap<>();
        startDate = null;
        endDate = null;
    }

    /**
     * Retrieves a list of all Photo objects in the album.
     *
     * @return An ArrayList containing all Photo objects.
     */
    public ArrayList<Photo> getPhotos() {
        return new ArrayList<>(photos.values());
    }

    /**
     * Adds a Photo object to the album.
     *
     * @param p The Photo object to be added.
     * @return true if the Photo was added successfully, false if the photo already exists in the album.
     */
    public boolean addPhoto(Photo p) {
        String path = p.getPath();
        if (photos.get(path) != null) return false;
        photos.put(path, p);
        LocalDate date = p.getDate();
        if (startDate == null && endDate == null) {
            startDate = date;
            endDate = date;
        }
        if (startDate == null || date.isBefore(startDate))
            startDate = date;
        if (endDate == null || date.isAfter(endDate))
            endDate = date;
        return true;
    }

    /**
     * Deletes a Photo object from the album.
     *
     * @param p The Photo object to be deleted.
     * @return true if the Photo was deleted successfully, false if the photo was not found.
     */
    public boolean deletePhoto(Photo p) {
        boolean success = photos.remove(p.getPath()) != null;
        if (!success) return false;
        if (photos.isEmpty()) {
            startDate = null;
            endDate = null;
            return true;
        }
        startDate = getPhotos().get(0).getDate();
        endDate = getPhotos().get(0).getDate();
        for (Photo photo : getPhotos()) {
            if (photo.getDate().isBefore(startDate))
                startDate = photo.getDate();
            if (photo.getDate().isAfter(endDate))
                endDate = photo.getDate();
        }
        return true;
    }

    /**
     * Retrieves the name of the album.
     *
     * @return The name of the album.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the album.
     *
     * @param n The new name for the album.
     */
    public void setName(String n) {
        this.name = n;
    }

    /**
     * Retrieves the number of photos in the album.
     *
     * @return The number of photos in the album.
     */
    public int numPhotos() {
        return photos.size();
    }

    /**
     * Finds a Photo object in the album based on the photo path.
     *
     * @param path The path of the photo to find.
     * @return The Photo object corresponding to the path, or null if not found.
     */
    public Photo findPhoto(String path) {
        return photos.get(path);
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
     * Retrieves the date range of the album (start date - end date).
     *
     * @return A formatted string representing the date range, or null if no photos exist in the album.
     */
    public String getDateRange() {
        return startDate == null ? null : startDate.toString() + " - " + endDate.toString();
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
