package com.example.demo1.persistence;

import com.example.demo1.Configuration;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.exceptions.Neo4jException;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;

import static org.neo4j.driver.Values.parameters;

public class Neo4jDriver {
    private final Driver driver;
    private static final Neo4jDriver driverN = new Neo4jDriver();
    //construct driver instance: a connection URI and
    //authentication information must be supplied. Using Basic Authentication
    public Neo4jDriver() {
        driver = GraphDatabase.driver(Configuration.NEO4J_URL,
                AuthTokens.basic(Configuration.NEO4J_USERNAME, Configuration.NEO4J_PASSWORD));
    }

    public boolean checkIfAuthorNameIsAvailable(String authorName) throws Neo4jException {
        Session session = driver.session();
        return session.executeRead(tx -> {
            Result result = tx.run("MATCH (n:Author {name: $name})" + "RETURN COUNT(n)",
                    parameters("name", authorName));
            if(result.next().get("COUNT(n)").asInt() == 0){
                return true;
            }else return false;
        });
    }

    //prima di essere chiamato va chimato checkIfAuthorNameIsAvailable() per vedere se è disponibile l'authorName
    public void addAuthor(String authorName){
        try (Session session = driver.session()) {
            session.executeWriteWithoutResult(tx -> {
                tx.run("CREATE (n:Author {name: $name})",
                        parameters("name", authorName)).consume();
                //.consume() se ho capito bene è per liberare memoria; per dire che hai finito
                // e dopo non devi e non puoi fare .hasNext o .next()
            });
        }
    }

    public boolean checkIfRecipeNameIsAvailable(String recipeName){
        boolean check;
        try (Session session = driver.session()) {
            check = session.executeRead(tx -> {
                Result result = tx.run("MATCH (n:Recipe {name: $name})" + "RETURN COUNT(n)",
                        parameters("name", recipeName));
                if(result.next().get("COUNT(n)").asInt() == 0){
                    return true;
                }else return false;
            });
        }catch (Exception e){check = false;}
        return check;
    }

    //prima di essere chiamato va chiamato checkIfRecipeNameIsAvailable() per vedere se è disponibile recipeName
    public void addRecipe(String recipeName, String image){
        try (Session session = driver.session()) {
            session.executeWriteWithoutResult(tx -> {
                tx.run("CREATE (n:Recipe {name: $name, image: $image})",
                        parameters("name", recipeName, "image", image)).consume();
                //.consume() se ho capito bene è per liberare memoria; per dire che hai finito
                // e dopo non devi e non puoi fare .hasNext o .next()
            });
        }
    }

    public void addRelationWrite(String authorName, String recipeName){
        String query = "MATCH (a:Author {name: $authorName}), (b:Recipe {name: $recipeName})" +
                "CREATE (a)-[r:WRITE]->(b)";
        try (Session session = driver.session()) {
            session.executeWriteWithoutResult(tx -> {
                tx.run(query, parameters("authorName", authorName, "recipeName", recipeName)).consume();
                //.consume() se ho capito bene è per liberare memoria; per dire che hai finito
                // e dopo non devi e non puoi fare .hasNext o .next()
            });
        }
    }

    public boolean checkIfFollowIsAvailable(String authorName1, String authorName2){
        boolean check;
        String query = "MATCH (a:Author {name: $authorName1})-[r:FOLLOW]->(b:Author {name: $authorName2})";
        try (Session session = driver.session()) {
            check = session.executeRead(tx -> {
                Result result = tx.run(query + "RETURN COUNT(r)",
                        parameters("authorName1", authorName1, "authorName2", authorName2));
                if(result.next().get("COUNT(r)").asInt() == 0){
                    return true;
                }else return false;
            });
        }catch (Exception e){check = false;}
        return check;
    }

    //prima di essere chiamato va chiamato checkIfRelationIsAvailable() per vedere se è c'è già
    public void addRelationFollow(String authorName1, String authorName2){
        String query = "MATCH (a:Author {name: $authorName1}), (b:Author {name: $authorName2})" +
                "CREATE (a)-[r:FOLLOW]->(b)";
        try (Session session = driver.session()) {
            session.executeWriteWithoutResult(tx -> {
                tx.run(query, parameters("authorName1", authorName1, "authorName2", authorName2)).consume();
                //.consume() se ho capito bene è per liberare memoria; per dire che hai finito
                // e dopo non devi e non puoi fare .hasNext o .next()
            });
        }
    }
}
