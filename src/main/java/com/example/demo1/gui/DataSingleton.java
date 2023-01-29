package com.example.demo1.gui;

import com.example.demo1.Configuration;
import javafx.scene.image.ImageView;

public class DataSingleton {
    private static final DataSingleton instance = new DataSingleton();
    private String typeOfUser; //per distinguere tra author e moderatore dopo il login, lo uso per dare l'opzione
                               //di promozione o meno nel fxml Ricerca_Utente
    private String recipeName;
    private Integer pageNumber;
    private Integer authorPromotion = null;
    private String authorName;
    private String password;
    private String otherAuthorName;
    private ImageView avatar;
    private Integer avatarIndex;
    private DataSingleton(){}

    public static DataSingleton getInstance(){
        return instance;
    }
    public Integer getPageNumber(){return pageNumber; }
    public String getRecipeName(){return recipeName; }
    public String getPassword() {
        return password;
    }
    public String getAuthorName() {return authorName; }
    public String getOtherAuthorName() {return otherAuthorName;}
    public void setPassword(String password) {
        this.password = password;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
    public void setOtherAuthorName(String otherAuthorName) {this.otherAuthorName = otherAuthorName;}
    public void setAvatar(int index) {this.avatar = new ImageView(Configuration.AVATAR.get(index - 1));}
    public ImageView getAvatar() {return this.avatar;}
    public void setAvatarIndex(int index) {this.avatarIndex = index;}
    public Integer getAvatarIndex() {return this.avatarIndex;}
    public void setTypeOfUser(String tipo) {typeOfUser = tipo;}
    public String getTypeOfUser() {return this.typeOfUser;}
    public void setAuthorPromotion(int quanto) {authorPromotion = quanto;}
    public int getAuthorPromotion() {return authorPromotion;}
}
