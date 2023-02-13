package it.unipi.dii.aide.lsmsd.recipeshare.dao.neo4j;

import it.unipi.dii.aide.lsmsd.recipeshare.model.RecipeReduced;
import it.unipi.dii.aide.lsmsd.recipeshare.persistence.Neo4jDriver;
import org.neo4j.driver.Result;
import org.neo4j.driver.exceptions.Neo4jException;
import static org.neo4j.driver.Values.parameters;

public class RecipeNeoDAO {
    public static boolean checkIfRecipeNameIsAvailable(String recipeName) throws Neo4jException {
        return Neo4jDriver.getNeoDriver().getSession().executeRead(tx -> {
            Result result = tx.run("MATCH (n:Recipe {name: $name})" + "RETURN COUNT(n)",
                    parameters("name", recipeName));
            return result.next().get("COUNT(n)").asInt() == 0;
        });
    }
    public static void addRecipe(RecipeReduced recipe){
        Neo4jDriver.getNeoDriver().getSession().executeWriteWithoutResult(tx -> tx.run("CREATE (n:Recipe {name: $name, image: $image})",
                parameters("name", recipe.getName(), "image", recipe.getImage())));
    }
    public static void deleteRecipe(String recipeName){
        Neo4jDriver.getNeoDriver().getSession().executeWriteWithoutResult(tx ->
                tx.run("MATCH (n:Recipe {name: $name}) DETACH DELETE n", parameters("name", recipeName)));
    }

    public static void addRelationWrite(String authorName, String recipeName){
        String query = "MATCH (a:Author {name: $authorName}), (b:Recipe {name: $recipeName})" +
                "CREATE (a)-[r:WRITE]->(b)";
        Neo4jDriver.getNeoDriver().getSession().executeWriteWithoutResult(tx ->
                tx.run(query, parameters("authorName", authorName, "recipeName", recipeName)));
    }
}
