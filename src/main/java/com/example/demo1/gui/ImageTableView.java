package com.example.demo1.gui;

import javafx.scene.image.ImageView;

public class ImageTableView implements RowTableView{

    private ImageView image;

    ImageTableView(ImageView img) {
        this.image = img;
        this.image.setFitHeight(80);
        this.image.setFitWidth(80);
    }

    public void setImage(ImageView value) {image = value;}

    public ImageView getImage() {return image;}
}
