package com.example.demo1.persistence;

import com.example.demo1.Configuration;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.exceptions.Neo4jException;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;

import static org.neo4j.driver.Values.parameters;

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
