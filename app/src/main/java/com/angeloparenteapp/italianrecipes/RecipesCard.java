package com.angeloparenteapp.italianrecipes;

/**
 * Created by angel on 16/11/2016. Main Activity Card Class
 */

public class RecipesCard {

    private String title;
    private String description;
    private int image;

    public RecipesCard(){
    }

    public RecipesCard(String title, String description, int image){
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }
}
