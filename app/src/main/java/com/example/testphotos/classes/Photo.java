package com.example.testphotos.classes;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Photo implements Serializable{
    private String path;
    private String caption;
    private LocalDate date;
    private ArrayList<Tag> tags;

    /**
     * Constructs a Photo object with the given path and caption.
     * Extracts the creation date from the file attributes using the last modified time.
     *
     * @param path    The path of the photo file.
     * @param caption The caption associated with the photo.
     */
    public Photo(String path, String caption) {
        try {
            this.path = path;
            this.caption = caption;
            tags = new ArrayList<>();
            Path file = FileSystems.getDefault().getPath(path);
            BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
            Instant lastModifiedInstant = attributes.lastModifiedTime().toInstant();
            date = lastModifiedInstant.atZone(ZoneId.systemDefault()).toLocalDate();

            // Not sure about this part, need to test the whole date extraction part
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // System.out.println("Creation Date: " + formatter.format(date));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs a Photo object with the given path.
     * Extracts the creation date from the file attributes using the last modified time.
     *
     * @param path The path of the photo file.
     */
    public Photo(String path) {
        try {
            this.path = path;
            tags = new ArrayList<>();
            Path file = FileSystems.getDefault().getPath(path);
            BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
            Instant lastModifiedInstant = attributes.lastModifiedTime().toInstant();
            date = lastModifiedInstant.atZone(ZoneId.systemDefault()).toLocalDate();

            // Not sure about this part, need to test the whole date extraction part
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // System.out.println("Creation Date: " + formatter.format(date));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the JavaFX Image object corresponding to the photo file.
     *
     * @return The Image object.
     */
//    public Image getImage() {
//        return new Image("file:" + path);
//    }

    /**
     * Retrieves the path of the photo file.
     *
     * @return The path of the photo.
     */
    public String getPath() {
        return path;
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
     * Sets the caption associated with the photo.
     *
     * @param caption The new caption for the photo.
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Retrieves the creation date of the photo.
     *
     * @return The creation date.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Retrieves the caption associated with the photo.
     *
     * @return The caption.
     */
    public String getCaption() {
        return caption;
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

    /**
     * Checks if this Photo is equal to another object.
     *
     * @param obj The object to compare.
     * @return true if the objects are equal, false otherwise.
     */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Photo p = (Photo) obj;
        return p.getPath().equals(path);
    }
}
