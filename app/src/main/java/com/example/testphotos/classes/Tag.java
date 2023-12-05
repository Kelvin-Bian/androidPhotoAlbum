package com.example.testphotos.classes;

public class Tag {
    private String name;
    private String value;

    /**
     * Constructor to create a new Tag.
     *
     * @param name  the name of the tag, e.g., "location"
     * @param value the value of the tag, e.g., "New York"
     */
    public Tag(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Gets the name of the tag.
     *
     * @return the tag name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the tag.
     *
     * @param name the new name of the tag
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the value of the tag.
     *
     * @return the tag value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the tag.
     *
     * @param value the new value of the tag
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns a string representation of the tag.
     *
     * @return a string in the format "value"
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Checks if two tags are equal. Tags are considered equal if both their names and values are the same.
     *
     * @param obj the object to compare with
     * @return true if the tags are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Tag tag = (Tag) obj;

        if (!name.equals(tag.name)) return false;
        return value.equals(tag.value);
    }
}
