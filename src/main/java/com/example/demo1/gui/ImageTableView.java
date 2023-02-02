package com.example.demo1.gui;

import com.example.demo1.gui.row.RowTableView;
import javafx.scene.image.ImageView;

public class ImageTableView implements RowTableView {

    private ImageView image;

    public ImageTableView(ImageView img) {
        this.image = img;
        this.image.setFitHeight(80);
        this.image.setFitWidth(80);
    }

    public void setImage(ImageView value) {image = value;}

    public ImageView getImage() {return image;}
}
