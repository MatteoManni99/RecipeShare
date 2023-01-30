package com.example.demo1.gui;

import javafx.scene.image.ImageView;

public class ImageTableView {

    private ImageView image;

    ImageTableView(ImageView img) {
        this.image = img;
        this.image.setFitHeight(50);
        this.image.setFitWidth(50);
    }

    public void setImage(ImageView value) {image = value;}

    public ImageView getImage() {return image;}
}
