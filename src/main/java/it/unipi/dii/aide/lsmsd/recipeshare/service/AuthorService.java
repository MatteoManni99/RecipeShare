package it.unipi.dii.aide.lsmsd.recipeshare.service;

import it.unipi.dii.aide.lsmsd.recipeshare.dao.mongo.AuthorMongoDAO;
import it.unipi.dii.aide.lsmsd.recipeshare.dao.mongo.RecipeMongoDAO;
import it.unipi.dii.aide.lsmsd.recipeshare.dao.neo4j.AuthorNeoDAO;
import it.unipi.dii.aide.lsmsd.recipeshare.model.Author;
import com.mongodb.MongoException;
import it.unipi.dii.aide.lsmsd.recipeshare.model.RecipeReducted;
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

    public static boolean changeAvatar(String authorName, Integer newImageIndex, Integer oldImageIndex){
        try{AuthorMongoDAO.changeAvatar(authorName, newImageIndex);
        } catch(MongoException e){return false;}
        try{AuthorNeoDAO.changeAvatar(authorName, newImageIndex);
        } catch(Neo4jException e){
            AuthorMongoDAO.changeAvatar(authorName, oldImageIndex); //rollback su mongo
            return false;
        }
        return true;
    }
    public static List<Author> getFollowers(String authorName,Integer elementsToSkip, Integer elementsToLimit){
        return AuthorNeoDAO.getFollowers(authorName,elementsToSkip,elementsToLimit);
    }
    public static List<Author> getFollowing(String authorName,Integer elementsToSkip, Integer elementsToLimit){
        return AuthorNeoDAO.getFollowing(authorName,elementsToSkip,elementsToLimit);
    }
    public static List<Author> getAuthorSuggested(Author author,Integer elementsToSkip, Integer elementsToLimit){
        List<Author> authorSuggested = AuthorNeoDAO.getAuthorSuggested(author,elementsToSkip,elementsToLimit);
        authorSuggested.removeAll(AuthorNeoDAO.getFollowing(author.getName(),elementsToSkip,elementsToLimit));
        authorSuggested.remove(author);
        return authorSuggested;
    }
    public static List<RecipeReducted> getRecipeSuggested(String authorName,Integer elementsToSkip, Integer elementsToLimit){
        List<RecipeReducted> recipeSuggested = AuthorNeoDAO.getRecipeSuggestedByWrite(authorName);
        recipeSuggested.addAll(AuthorNeoDAO.getRecipeSuggestedByReview(authorName,elementsToSkip,elementsToLimit));
        recipeSuggested.removeAll(AuthorNeoDAO.getRecipeAdded(authorName));
        return recipeSuggested.stream().distinct().toList();
    }
    public static void updatePromotion(String authorName, Integer newPromotionValue){
        AuthorMongoDAO.updatePromotion(authorName, newPromotionValue);
    }

    @Deprecated
    public static boolean changeAuthorName(String newAuthorName, Author currentAuthor){
        return AuthorMongoDAO.changeAuthorName(newAuthorName,currentAuthor);
    }
    public static boolean changePassword(String newPassword, Author author){
        try{
            AuthorMongoDAO.changePassword(newPassword, author);
            return true;
        } catch (MongoException e){ return false; }
    }
    public static Author getAuthor(String authorName){
        return AuthorMongoDAO.getAuthor(authorName);
    }

    public static List<Author> searchAuthors(String nameToSearch, Integer elementsToSkip, Integer elementsToLimit){
        return AuthorMongoDAO.searchAuthors(nameToSearch, elementsToSkip, elementsToLimit);
    }

}
