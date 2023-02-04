package it.unipi.dii.aide.lsmsd.recipeshare.dao.neo4j;

import it.unipi.dii.aide.lsmsd.recipeshare.model.Author;
import it.unipi.dii.aide.lsmsd.recipeshare.model.RecipeReducted;
import it.unipi.dii.aide.lsmsd.recipeshare.persistence.Neo4jDriver;
import org.neo4j.driver.Record;
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
        Neo4jDriver.getNeoDriver().getSession().executeWriteWithoutResult(tx -> {
            tx.run("CREATE (n:Author {name: $name, avatar: $avatar})",
                    parameters("name", authorName, "avatar", avatar)).consume();
        });
    }
    public static void changeAvatar(String authorName, Integer newAvatar) throws Neo4jException{
        String query = "MATCH (a:Author {name: $authorName})" +
                "SET a.avatar = $newAvatar";
        Neo4jDriver.getNeoDriver().getSession().executeWriteWithoutResult(tx -> {
            tx.run(query, parameters("authorName", authorName, "newAvatar", newAvatar));
        });
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
        Neo4jDriver.getNeoDriver().getSession().executeWriteWithoutResult(tx -> {
            tx.run(query, parameters("authorName1", authorName1, "authorName2Follow", authorName2Follow));
        });
    }
    public static void removeRelationFollow(String authorName1, String authorName2Unfollow) throws Neo4jException {
        String query = "MATCH (a:Author {name: $authorName1})-[r:FOLLOW]->(b:Author {name: $authorName2Unfollow})" +
                "DELETE r";
        Neo4jDriver.getNeoDriver().getSession().executeWriteWithoutResult(tx -> {
            tx.run(query, parameters("authorName1", authorName1, "authorName2Unfollow", authorName2Unfollow));
        });
    }
    public static List<Author> getFollowers(String authorName) throws Neo4jException {
        String query = "MATCH (f:Author)-[:FOLLOW]->(a:Author {name: $authorName})" +
                "RETURN f.name as Name, f.avatar as Avatar";
        return Neo4jDriver.getNeoDriver().getSession().executeRead(tx -> {
            Result result = tx.run(query, parameters("authorName", authorName));
            List<Author> follower = new ArrayList<>();
            while(result.hasNext()) {
                Record r = result.next();
                follower.add(new Author(r.get("Name").asString(),r.get("Avatar").asInt()));
            }
            return follower;
        });
    }
    public static List<Author> getFollowing(String authorName) throws Neo4jException {
        String query = "MATCH (a:Author {name: $authorName})-[:FOLLOW]->(f:Author)" +
                "RETURN f.name as Name, f.avatar as Avatar";
        return Neo4jDriver.getNeoDriver().getSession().executeRead(tx -> {
            Result result = tx.run(query, parameters("authorName", authorName));
            List<Author> follower = new ArrayList<>();
            while(result.hasNext()) {
                Record r = result.next();
                follower.add(new Author(r.get("Name").asString(),r.get("Avatar").asInt()));
            }
            return follower;
        });
    }
    public static List<Author> getAuthorSuggested(Author author) throws Neo4jException {
        String query = "MATCH (a:Author {name: $authorName})-[:FOLLOW]->(f1:Author)-[:FOLLOW]->(f2:Author) " +
                "RETURN f2.name as Name, f2.avatar as Avatar, COUNT(*) as Frequency " +
                "ORDER BY Frequency DESC";
        List<Author> authorSuggested = Neo4jDriver.getNeoDriver().getSession().executeRead(tx -> {
            Result result = tx.run(query, parameters("authorName", author.getName()));
            List<Author> authorSuggested_ = new ArrayList<>();
            while(result.hasNext()) {
                Record r = result.next();
                authorSuggested_.add(new Author(r.get("Name").asString(),r.get("Avatar").asInt()));
            }
            return authorSuggested_;
        });
        authorSuggested.removeAll(AuthorNeoDAO.getFollowing(author.getName()));
        authorSuggested.remove(author);
        return authorSuggested;
    }
    public static List<RecipeReducted> getRecipeSuggested(String authorName) throws Neo4jException {
        String query = "MATCH (a:Author {name: $authorName})-[:FOLLOW]->(f:Author)-[:WRITE]->(n:Recipe) " +
                "RETURN f.name as AuthorName, n.name as Name,  n.image as Image";

        List<RecipeReducted> recipeSuggested = Neo4jDriver.getNeoDriver().getSession().executeRead(tx -> {
            Result result = tx.run(query, parameters("authorName", authorName));
            List<RecipeReducted> recipeSuggested_ = new ArrayList<>();
            while(result.hasNext()) {
                Record r = result.next();
                recipeSuggested_.add(new RecipeReducted(r.get("Name").asString(),r.get("AuthorName").asString(),
                        r.get("Image").asString()));
            }
            return recipeSuggested_;
        });
        //recipeSuggested.removeAll(AuthorNeoDAO.getFollowing(author.getName()));
        //recipeSuggested.remove(author);
        return recipeSuggested;
    }
}
