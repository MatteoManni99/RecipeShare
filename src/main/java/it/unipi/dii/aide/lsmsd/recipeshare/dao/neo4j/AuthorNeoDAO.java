package it.unipi.dii.aide.lsmsd.recipeshare.dao.neo4j;

import it.unipi.dii.aide.lsmsd.recipeshare.model.Author;
import it.unipi.dii.aide.lsmsd.recipeshare.model.RecipeReduced;
import it.unipi.dii.aide.lsmsd.recipeshare.persistence.Neo4jDriver;
import org.neo4j.driver.Result;
import org.neo4j.driver.exceptions.Neo4jException;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class AuthorNeoDAO {

    public static boolean checkIfAuthorNameIsAvailable(String authorName) throws Neo4jException {
        return Neo4jDriver.getNeoDriver().getSession().executeRead(tx -> {
            Result result = tx.run("MATCH (n:Author {name: $name})" + "RETURN COUNT(n)",
                    parameters("name", authorName));
            return result.next().get("COUNT(n)").asInt() == 0;
        });
    }
    public static void addAuthor(String authorName, Integer avatar) throws Neo4jException {
        Neo4jDriver.getNeoDriver().getSession().executeWriteWithoutResult(tx -> tx.run("CREATE (n:Author {name: $name, avatar: $avatar})",
                parameters("name", authorName, "avatar", avatar)).consume());
    }
    public static void changeAvatar(String authorName, Integer newAvatar) throws Neo4jException{
        String query = "MATCH (a:Author {name: $authorName})" +
                "SET a.avatar = $newAvatar";
        Neo4jDriver.getNeoDriver().getSession().executeWriteWithoutResult(tx -> tx.run(query, parameters("authorName", authorName, "newAvatar", newAvatar)));
    }
    public static boolean checkIfFollowIsAvailable(String authorName1, String authorName2Follow) throws Neo4jException {
        String query = "MATCH (a:Author {name: $authorName1})-[r:FOLLOW]->(b:Author {name: $authorName2Follow})";
        return Neo4jDriver.getNeoDriver().getSession().executeRead(tx -> {
            Result result = tx.run(query + "RETURN COUNT(r)",
                    parameters("authorName1", authorName1, "authorName2Follow", authorName2Follow));
            return result.next().get("COUNT(r)").asInt() == 0;
        });
    }
    public static void addRelationFollow(String authorName1, String authorName2Follow) throws Neo4jException {
        String query = "MATCH (a:Author {name: $authorName1}), (b:Author {name: $authorName2Follow})" +
                "CREATE (a)-[r:FOLLOW]->(b)";
        Neo4jDriver.getNeoDriver().getSession().executeWriteWithoutResult(tx ->
                tx.run(query, parameters("authorName1", authorName1, "authorName2Follow", authorName2Follow)));
    }
    public static void removeRelationFollow(String authorName1, String authorName2Unfollow) throws Neo4jException {
        String query = "MATCH (a:Author {name: $authorName1})-[r:FOLLOW]->(b:Author {name: $authorName2Unfollow})" +
                "DELETE r";
        Neo4jDriver.getNeoDriver().getSession().executeWriteWithoutResult(tx ->
                tx.run(query, parameters("authorName1", authorName1, "authorName2Unfollow", authorName2Unfollow)));
    }
    public static void addRelationReview(String authorName, String recipeName, Integer rating) throws Neo4jException {
        String query = "MATCH (a:Author {name: $authorName}), (b:Recipe {name: $recipeName})" +
                "CREATE (a)-[r:REVIEW {rating:$rating}]->(b)";
        Neo4jDriver.getNeoDriver().getSession().executeWriteWithoutResult(tx ->
                tx.run(query, parameters("authorName", authorName, "recipeName", recipeName, "rating",rating)));
    }
    public static void removeRelationReview(String authorName, String recipeName) throws Neo4jException {
        String query = "MATCH (a:Author {name: $authorName})-[r:REVIEW]->(b:Author {name: $recipeName})" +
                "DELETE r";
        Neo4jDriver.getNeoDriver().getSession().executeWriteWithoutResult(tx ->
                tx.run(query, parameters("authorName", authorName, "recipeName", recipeName)));
    }

    public static List<Author> getFollowers(String authorName,Integer elementsToSkip, Integer elementsToLimit) throws Neo4jException {
        String query = "MATCH (f:Author)-[:FOLLOW]->(a:Author {name: $authorName})" +
                "RETURN f.name as Name, f.avatar as Avatar " +
                "SKIP " + elementsToSkip +
                " LIMIT " + elementsToLimit;
        return Neo4jDriver.getNeoDriver().getSession().executeRead(tx -> {
            Result result = tx.run(query, parameters("authorName", authorName));
            List<Author> follower = new ArrayList<>();
            result.forEachRemaining(record ->follower.add(new Author(record.get("Name").asString(),record.get("Avatar").asInt())));
            return follower;
        });
    }
    public static List<Author> getFollowing(String authorName,Integer elementsToSkip, Integer elementsToLimit) throws Neo4jException {
        String query = "MATCH (a:Author {name: $authorName})-[:FOLLOW]->(f:Author)" +
                "RETURN f.name as Name, f.avatar as Avatar " +
                "SKIP " + elementsToSkip +
                " LIMIT " + elementsToLimit;
        return Neo4jDriver.getNeoDriver().getSession().executeRead(tx -> {
            Result result = tx.run(query, parameters("authorName", authorName));
            List<Author> follower = new ArrayList<>();
            result.forEachRemaining(record -> follower.add(new Author(record.get("Name").asString(),(record.get("Avatar").asInt()))));
            return follower;
        });
    }
    public static List<Author> getAuthorSuggested(Author author,Integer elementsToSkip, Integer elementsToLimit) throws Neo4jException {
        String query = "MATCH (a:Author {name: $authorName})-[:FOLLOW]->(f1:Author)-[:FOLLOW]->(f2:Author) " +
                "WHERE NOT (a)-[:FOLLOW]->(f2) AND f2.name <> $authorName " +
                "RETURN f2.name as Name, f2.avatar as Avatar, COUNT(*) as Frequency " +
                "ORDER BY Frequency DESC " +
                "SKIP " + elementsToSkip +
                " LIMIT " + elementsToLimit;
        return Neo4jDriver.getNeoDriver().getSession().executeRead(tx -> {
            Result result = tx.run(query, parameters("authorName", author.getName()));
            List<Author> authorSuggested = new ArrayList<>();
            result.forEachRemaining(record -> authorSuggested.add(new Author(record.get("Name").asString(),record.get("Avatar").asInt())));
            return authorSuggested;
        });
    }
    public static List<RecipeReduced> getRecipeAdded(String authorName) throws Neo4jException {
        String query = "MATCH (a:Author {name: $authorName})-[:WRITE]->(r:Recipe)" +
                "RETURN r.name as Name, r.image as Image";
        return Neo4jDriver.getNeoDriver().getSession().executeRead(tx -> {
            Result result = tx.run(query, parameters("authorName", authorName));
            List<RecipeReduced> recipeAdded = new ArrayList<>();
            result.forEachRemaining(record ->recipeAdded.add(new RecipeReduced(record.get("Name").asString(),authorName,record.get("Image").asString())));
            return recipeAdded;
        });
    }
    @Deprecated
    public static List<RecipeReduced> getRecipeSuggestedByWrite(String authorName) throws Neo4jException {
        String query = "MATCH (a:Author {name: $authorName})-[:FOLLOW]->(f:Author)-[:WRITE]->(n:Recipe) " +
                "RETURN f.name as AuthorName, n.name as Name,  n.image as Image";
        return Neo4jDriver.getNeoDriver().getSession().executeRead(tx -> {
            Result result = tx.run(query, parameters("authorName", authorName));
            List<RecipeReduced> recipeSuggested = new ArrayList<>();
            result.forEachRemaining(record ->  recipeSuggested.add(new RecipeReduced(record.get("Name").asString(),record.get("AuthorName").asString(),
                    record.get("Image").asString())));
            return recipeSuggested;
        });
    }
    @Deprecated
    public static List<RecipeReduced> getRecipeSuggestedByReview(String authorName, Integer elementsToSkip, Integer elementsToLimit) throws Neo4jException {
        String query = "MATCH (a:Author {name: $authorName})-[:FOLLOW]->(f:Author)-[r:REVIEW]->(n:Recipe) <-[:WRITE]-(b:Author)" +
                "WHERE r.rating > 2 " +
                "RETURN b.name as AuthorName, n.name as Name,  n.image as Image " +
                "SKIP " + elementsToSkip +
                " LIMIT " + elementsToLimit;
        return Neo4jDriver.getNeoDriver().getSession().executeRead(tx -> {
            Result result = tx.run(query, parameters("authorName", authorName));
            List<RecipeReduced> recipeSuggested = new ArrayList<>();
            result.forEachRemaining(record -> recipeSuggested.add(new RecipeReduced(record.get("Name").asString(),record.get("AuthorName").asString(),
                    record.get("Image").asString())));
            return recipeSuggested;
        });
    }
    public static List<RecipeReduced> getRecipeSuggested(String authorName, Integer elementsToSkip, Integer elementsToLimit) throws Neo4jException {
        String query =  //CALL{ call method was implemented only in 4.1+ v. of neo4j
                "MATCH (a:Author {name: $authorName})-[:FOLLOW]->(b:Author)-[:WRITE]->(r:Recipe) "+
                        "WHERE NOT (r)<-[:WRITE]-(a) AND NOT (r)<-[:REVIEW]-(a) "+
                        "RETURN b.name as AuthorName, r.name as Name,  r.image as Image "+
                        "UNION "+
                        "MATCH (a:Author {name:$authorName})-[:FOLLOW]->(b:Author)-[rev:REVIEW]->(r:Recipe) "+
                        "WHERE NOT (r)<-[:WRITE]-(a) AND NOT (r)<-[:REVIEW]-(a) AND rev.rating>2 "+
                        "RETURN b.name as AuthorName, r.name as Name,  r.image as Image ";
        //}
        //"SKIP $elementsToSkip " +
        //"LIMIT $elementsToLimit";
        return Neo4jDriver.getNeoDriver().getSession().executeRead(tx -> {
            Result result = tx.run(query, parameters("authorName", authorName));
            //  ,"elementsToSkip", elementsToSkip, "elementsToLimit", elementsToLimit));
            List<RecipeReduced> recipeSuggested = new ArrayList<>();
            result.forEachRemaining(record -> recipeSuggested.add(new RecipeReduced(record.get("Name").asString(),record.get("AuthorName").asString(),
                    record.get("Image").asString())));

            if(recipeSuggested.size() > elementsToSkip + elementsToLimit)
            {
                return recipeSuggested.subList(elementsToSkip, elementsToLimit + elementsToSkip);
            }
            else
            {
                if (elementsToSkip > elementsToSkip + (recipeSuggested.size() - elementsToSkip)) return new ArrayList<>();
                System.out.println(elementsToSkip);
                System.out.println(elementsToSkip + (recipeSuggested.size() - elementsToSkip));
                return recipeSuggested.subList(elementsToSkip, elementsToSkip + (recipeSuggested.size() - elementsToSkip));
            }
        });
    }
}