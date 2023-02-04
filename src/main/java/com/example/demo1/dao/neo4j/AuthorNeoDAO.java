package com.example.demo1.dao.neo4j;

import com.example.demo1.persistence.Neo4jDriver;
import org.neo4j.driver.Result;
import org.neo4j.driver.exceptions.Neo4jException;

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
    public static void removeRelationFollow(String authorName1, String authorName2Unfollow) {
        String query = "MATCH (a:Author {name: $authorName1})-[r:FOLLOW]->(b:Author {name: $authorName2Unfollow})" +
                "DELETE r";
        Neo4jDriver.getNeoDriver().getSession().executeWriteWithoutResult(tx -> {
            tx.run(query, parameters("authorName1", authorName1, "authorName2Unfollow", authorName2Unfollow));
        });
    }
}
