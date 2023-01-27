package com.example.demo1.service;

import com.example.demo1.dao.mongo.AuthorMongoDAO;
import com.example.demo1.model.Author;

import java.util.ArrayList;

public class AuthorService {

    public static boolean login(String name, String password){
        return AuthorMongoDAO.tryLogin(name,password);
    }

    public static boolean register(String authorName, String password, Integer image, Integer standardPromotionValue) {
        if (AuthorMongoDAO.registration(authorName, password, image, standardPromotionValue)) {
            //if(NEO)
            return true;
        } else {
            return false;
        }
    }

    public static void updateImage(String authorName, Integer newImageIndex){
        AuthorMongoDAO.updateImage(authorName, newImageIndex);
    }

    public static void updatePromotion(String authorName, Integer newPromotionValue){
        AuthorMongoDAO.updatePromotion(authorName, newPromotionValue);
    }

    public static boolean changeAuthorName(String newAuthorName, Author authorName){
        return AuthorMongoDAO.changeAuthorName(newAuthorName, authorName);
    }
    public static void changePassword(String newPassword, Author author){
        AuthorMongoDAO.changePassword(newPassword, author);
    }

    public static Author getAuthor(String authorName){
        return AuthorMongoDAO.getAuthor(authorName);
    }

    public static ArrayList<Author> searchAuthors(String nameToSearch, Integer elementsToSkip, Integer elementsToLimit){
        return AuthorMongoDAO.searchAuthors(nameToSearch, elementsToSkip, elementsToLimit);
    }
}
