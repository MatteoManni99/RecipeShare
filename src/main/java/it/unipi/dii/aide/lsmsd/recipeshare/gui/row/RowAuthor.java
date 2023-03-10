package it.unipi.dii.aide.lsmsd.recipeshare.gui.row;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class RowAuthor implements RowTableView {

    private final SimpleStringProperty authorNameTable;
    private final SimpleIntegerProperty promotionTable;
    private final ImageView imageTable;

    public RowAuthor(String authorName, Integer promotion, ImageView image) {
        authorNameTable = new SimpleStringProperty(authorName);
        promotionTable = new SimpleIntegerProperty(promotion);
        imageTable = new RowImage(image).getImage();
    }

    public String getAuthorName() { return authorNameTable.get(); }
    public Integer getPromotion() { return promotionTable.get(); }
    public ImageView getImage() { return imageTable; }
}
