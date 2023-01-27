package com.example.demo1.service;

import com.example.demo1.dao.mongo.AuthorMongoDAO;

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
}
