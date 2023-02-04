package it.unipi.dii.aide.lsmsd.recipeshare.persistence;

import it.unipi.dii.aide.lsmsd.recipeshare.Configuration;
import org.neo4j.driver.*;

public class Neo4jDriver {
    private static Driver driver;
    private static final Neo4jDriver neoDriver = new Neo4jDriver();
    //construct driver instance: a connection URI and
    //authentication information must be supplied. Using Basic Authentication
    public Neo4jDriver() {
        driver = GraphDatabase.driver(Configuration.NEO4J_URL,
                AuthTokens.basic(Configuration.NEO4J_USERNAME, Configuration.NEO4J_PASSWORD));
    }
    public Session getSession(){
        return driver.session();
    }
    public static Neo4jDriver getNeoDriver() {
        return neoDriver;
    }

}
