package com.example.demo1.service;

import com.example.demo1.dao.mongo.AuthorMongoDAO;
import com.example.demo1.dao.neo4j.AuthorNeoDAO;
import com.example.demo1.model.Author;
import com.mongodb.MongoException;
import org.neo4j.driver.exceptions.Neo4jException;

import java.util.List;

public class AuthorService {

    public static boolean login(String name, String password){
        return AuthorMongoDAO.tryLogin(name,password);
    }

    public static boolean register(String authorName, String password, Integer image, Integer standardPromotionValue) {
        try{ //add su Mongo
            if(!AuthorMongoDAO.registration(authorName, password, image, standardPromotionValue)) return false;
        }catch(MongoException e){return false;}

        try{AuthorNeoDAO.addAuthor(authorName,image);} //add su Neo
        catch(Neo4jException e){
            AuthorMongoDAO.deleteAuthor(authorName); //rollback su mongo
            return false;
        }
        return true;
    }
    public static boolean followAnOtherAuthor(String authorName1, String authorName2Follow) {
        try{ //check if follow is available
            if(AuthorNeoDAO.checkIfFollowIsAvailable(authorName1, authorName2Follow)){
                AuthorNeoDAO.addRelationFollow(authorName1, authorName2Follow); //add relation
                return true;
            }else return false;
        }catch(Neo4jException e){return false;}
    }
    public static boolean unfollowAuthor(String authorName1, String authorName2Unfollow) {
        try{ //check if unfollow is available
            if(!AuthorNeoDAO.checkIfFollowIsAvailable(authorName1, authorName2Unfollow)){
                AuthorNeoDAO.removeRelationFollow(authorName1, authorName2Unfollow); //remove relation
                return true;
            }else return false;
        }catch(Neo4jException e){return false;}
    }
    public static boolean checkIfFollowIsAvailable (String authorName1, String authorName2){
        return AuthorNeoDAO.checkIfFollowIsAvailable(authorName1, authorName2);
    }

    public static boolean changeAvatar(String authorName, Integer newImageIndex){
        try{AuthorMongoDAO.changeAvatar(authorName, newImageIndex);}
        catch(MongoException e){return false;}
        try{AuthorNeoDAO.changeAvatar(authorName, newImageIndex);}
        catch(Neo4jException e){
            //TODO rollback su mongo

            return false;
        }
        return true;
    }
    public static List<Author> getFollowers(String authorName){
        return AuthorNeoDAO.getFollowers(authorName);
    }
    public static List<Author> getFollowing(String authorName){
        return AuthorNeoDAO.getFollowing(authorName);
    }

    public static void updatePromotion(String authorName, Integer newPromotionValue){
        AuthorMongoDAO.updatePromotion(authorName, newPromotionValue);
    }
    //TODO decidere cosa fare con changeAuthorName
    public static boolean changeAuthorName(String newAuthorName, Author currentAuthor){
        return AuthorMongoDAO.changeAuthorName(newAuthorName, currentAuthor);
    }
    public static void changePassword(String newPassword, Author author){
        AuthorMongoDAO.changePassword(newPassword, author);
    }
    public static Author getAuthor(String authorName){
        return AuthorMongoDAO.getAuthor(authorName);
    }

    public static List<Author> searchAuthors(String nameToSearch, Integer elementsToSkip, Integer elementsToLimit){
        return AuthorMongoDAO.searchAuthors(nameToSearch, elementsToSkip, elementsToLimit);
    }

}
