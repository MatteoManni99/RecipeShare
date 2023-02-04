package it.unipi.dii.aide.lsmsd.recipeshare.persistence;

import it.unipi.dii.aide.lsmsd.recipeshare.Configuration;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBDriver {

    private static final MongoDBDriver driver = new MongoDBDriver();
    private final MongoDatabase databaseAP;
    private final MongoDatabase databaseCP;

    private MongoDBDriver() {
        MongoClientSettings settingAP = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(Configuration.MONGODB_URL))
                .readPreference(ReadPreference.nearest())
                .retryWrites(true)
                .writeConcern(WriteConcern.W1).build();

        MongoClientSettings settingCP = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(Configuration.MONGODB_URL))
                .readPreference(ReadPreference.primary())
                .retryWrites(true)
                .writeConcern(WriteConcern.MAJORITY).build();

        MongoClient mongoClientAP = MongoClients.create(settingAP);
        MongoClient mongoClientCP = MongoClients.create(settingCP);
        databaseAP = mongoClientAP.getDatabase(Configuration.MONGODB_DB);
        databaseCP = mongoClientCP.getDatabase(Configuration.MONGODB_DB);
    }
    public MongoCollection<Document> getCollection(String collection) {
        return databaseAP.getCollection(collection);
    }
    public MongoCollection<Document> getCollectionCP(String collection) {
        return databaseCP.getCollection(collection);
    }
    public static MongoDBDriver getDriver() {
        return driver;
    }

    //public void closeConnection() {mongoClientAP.close();}
}
