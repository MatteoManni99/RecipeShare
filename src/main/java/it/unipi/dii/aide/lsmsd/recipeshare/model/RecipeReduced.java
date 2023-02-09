package it.unipi.dii.aide.lsmsd.recipeshare.model;

import java.util.Objects;

public class RecipeReduced {
    private String name;
    private String authorName;
    private String image;

    public RecipeReduced(String name, String authorName, String image){
        this.name = name;
        this.authorName = authorName;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeReduced recipe = (RecipeReduced) o;
        return Objects.equals(image, recipe.image) &&
                Objects.equals(authorName, recipe.authorName) &&
                Objects.equals(name, recipe.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, image, authorName);
    }
}

