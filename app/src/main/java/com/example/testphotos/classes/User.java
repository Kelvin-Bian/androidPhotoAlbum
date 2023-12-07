package com.example.testphotos.classes;
import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class User implements Serializable {
    private HashMap<String, Album> albums;

    /**
     * Constructs a User object with the given name.
     * Initializes the albums HashMap
     *
     */
    public User() {
        albums = new HashMap<>();
    }

    /**
     * Retrieves a list of albums associated with the user.
     *
     * @return An ArrayList containing the user's albums.
     */
    public ArrayList<Album> getAlbums() {
        return new ArrayList<>(albums.values());
    }

    /**
     * @return An ArrayList containing the album names.
     */
    public ArrayList<String> getAlbumNames() {
        ArrayList<Album> albumList = new ArrayList<>(albums.values());
        ArrayList<String> albumNames = new ArrayList<>();
        for (Album a: albumList) {
            albumNames.add(a.getName());
        }
        return albumNames;
    }

    /**
     * Finds and retrieves a specific album by name.
     *
     * @param n The name of the album to be found.
     * @return The Album object if found, null otherwise.
     */
    public Album findAlbum(String n) {
        return albums.get(n);
    }

    /**
     * Adds a new album with the given name to the user's albums.
     *
     * @param n The name of the new album.
     * @return true if the album was added successfully, false if an album with the same name already exists.
     */
    public boolean addAlbum(String n) {
        if (albums.get(n) != null) return false;
        albums.put(n, new Album(n));
        return true;
    }

    /**
     * Deletes the album with the given name from the user's albums.
     *
     * @param n The name of the album to be deleted.
     * @return true if the album was deleted successfully, false if the album does not exist.
     */
    public boolean deleteAlbum(String n) {
        if (albums.get(n) == null) return false;
        albums.remove(n);
        return true;
    }

    /**
     * Updates the metadata of photos with the same path in all albums.
     *
     * @param photo The Photo object containing updated metadata.
     */
    public void updateSamePhotoRefs(Photo photo) {
        for (Photo p : searchPhoto(photo.getUri())) {
            p.setTags(photo.getTags());
        }
    }

    /**
     * Adds a new album to the user's albums.
     *
     * @param a The Album object to be added.
     */
    public void addAlbum(Album a) {
        albums.put(a.getName(), a);
    }

    /**
     * Updates the metadata of a new photo by copying attributes from an existing photo with the same path.
     *
     * @param p The new Photo object to be updated.
     */
    public void updateNewPhoto(Photo p) {
        Photo photo = searchPhoto(p.getUri()).get(0);
        if (photo != null) {
            p.setTags(photo.getTags());
        }
    }

    /**
     * Searches for a photo with a specific path in all albums.
     */
    public ArrayList<Photo> searchPhoto(Uri uri) {
        ArrayList<Photo> result = new ArrayList<Photo>();
        Photo foundPhoto;
        List<Album> allAlbums = new ArrayList<>(albums.values());
        for (Album a : allAlbums) {
            foundPhoto = a.findPhoto(uri);
            if (foundPhoto != null) result.add(foundPhoto);
            foundPhoto = null;
        }
        return result;
    }

    /**
     * Searches for a photo with a specific path in a given album.
     */
    public Photo searchPhoto(Album album, Uri uri) {
        ArrayList<Photo> albumPhotos = album.getPhotos();
        for (int i = 0; i < albumPhotos.size(); i++) {
            if (uri.equals(albumPhotos.get(i).getUri())) {
                return albumPhotos.get(i);
            }
        }
        return null;
    }

    /**
     * Renames an album with a new name.
     *
     * @param a The Album object to be renamed.
     * @param n The new name for the album.
     * @return true if the album was renamed successfully, false if an album with the new name already exists.
     */
    public boolean renameAlbum(Album a, String n) {
        if (findAlbum(n) != null) return false;
        String oldName = a.getName();
        albums.get(oldName).setName(n);
        albums.remove(oldName);
        albums.put(n, a);
        return true;
    }
}
