package com.example.demo1.service;

import com.example.demo1.dao.mongo.ModeratorMongoDAO;
import com.mongodb.MongoException;

import static com.mongodb.client.model.Filters.eq;

public class ModeratorService {

    public static boolean checkModeratorName(String name) throws MongoException {
        return ModeratorMongoDAO.checkModeratorName(name);
    }

    public static boolean tryLogin(String name, String password) throws MongoException{
        return ModeratorMongoDAO.tryLogin(name, password);
    }

    public static boolean checkRegistration(String name, String password) throws MongoException{
        return ModeratorMongoDAO.checkRegistration(name, password);
    }

}
