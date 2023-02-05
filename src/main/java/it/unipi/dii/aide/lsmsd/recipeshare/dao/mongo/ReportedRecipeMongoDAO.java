package it.unipi.dii.aide.lsmsd.recipeshare.dao.mongo;

import it.unipi.dii.aide.lsmsd.recipeshare.Configuration;
import it.unipi.dii.aide.lsmsd.recipeshare.model.Author;
import it.unipi.dii.aide.lsmsd.recipeshare.model.ReportedRecipe;
import it.unipi.dii.aide.lsmsd.recipeshare.persistence.MongoDBDriver;
import it.unipi.dii.aide.lsmsd.recipeshare.service.AuthorService;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;

public class ReportedRecipeMongoDAO {

    public static void addReportedRecipe(ReportedRecipe reportedRecipe) throws MongoException {
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_REPORTED_RECIPE).
                insertOne(fromReportedRecipeToDocument(reportedRecipe)).wasAcknowledged();
    }
    public static boolean checkIfRecipeAlreadyReported(ReportedRecipe reportedRecipe) throws MongoException {
        MongoCollection<Document> reportedCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_REPORTED_RECIPE);
        Bson filter = Filters.and(
                Filters.eq("name", reportedRecipe.getName()),
                Filters.eq("reporterName", reportedRecipe.getReporterName()));
        return reportedCollection.find(filter).cursor().hasNext();
    }

    private static Document fromReportedRecipeToDocument(ReportedRecipe reportedRecipe) throws MongoException{
        return new Document("name",reportedRecipe.getName()).append("authorName",reportedRecipe.getAuthorName())
                .append("reporterName",reportedRecipe.getReporterName()).append("dateReporting",reportedRecipe.getDateReporting())
                .append("image",reportedRecipe.getImage());
    }
    private static ReportedRecipe fromDocToReportedRecipe(Document doc) throws MongoException{
        return new ReportedRecipe(doc.getString("name"),doc.getString("authorName"),
                doc.getString("reporterName"),doc.getString("dateReporting"), doc.getString("image"));
    }

    public static List<ReportedRecipe> getListReportedRecipes(String name, Integer elementToSkip, Integer elementsToLimit) throws MongoException{
        List<ReportedRecipe> reportedRecipesList = new ArrayList<>();
        /*
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_REPORTED_RECIPE).find()
                .forEach(document -> reportedRecipesList.add(fromDocToReportedRecipe(document)));*/

        MongoCollection<Document> collection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_REPORTED_RECIPE);
        Bson match = match(new Document("name",new Document("$regex",name).append("$options","i")));
        MongoCursor<Document> cursor = (name == null) ?
                collection.aggregate(Arrays.asList(skip(elementToSkip),limit(elementsToLimit))).iterator() :
                collection.aggregate(Arrays.asList(match,skip(elementToSkip),limit(elementsToLimit))).iterator();
        cursor.forEachRemaining(document -> reportedRecipesList.add(fromDocToReportedRecipe(document)));
        return reportedRecipesList;
    }

    public static List<Author> onHighestRatioQueryClick() throws MongoException{
        Map<String, Double> map = new TreeMap<>();
        List<Author> authorList = new ArrayList<>();
        List<String> reportedAuthor = new ArrayList<>();
        Bson group1 = new Document("$group", new Document("_id", "$authorName")
                .append("count",new Document("$count",new Document())));
        Bson match1 = match(gte("count", 1));
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_REPORTED_RECIPE).aggregate(List.of(group1,match1))
                .forEach(document -> {
                    map.put(document.getString("_id"), document.getInteger("count").doubleValue());
                    reportedAuthor.add(document.getString("_id"));
                });

        Bson group2 = new Document("$group", new Document("_id", "$AuthorName")
                .append("count",new Document("$count",new Document())));
        Bson match2 = match(in("AuthorName", reportedAuthor));
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE).aggregate(List.of(match2,group2))
                .forEach(document -> {
                    String authorName = document.getString("_id");
                    Double score = document.getInteger("count") / map.get(authorName);
                    authorList.add(new Author(authorName,score,AuthorService.searchAuthors(authorName, 0,1).get(0).getImage()));
                });

        authorList.sort(Comparator.comparingDouble(Author::getScore));

        System.out.println(authorList.get(0).getName());
        return authorList;
    }

    public static void removeReportedRecipe(String reportedRecipeToRemove) throws MongoException{
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_REPORTED_RECIPE).deleteMany(new Document("name",reportedRecipeToRemove));
    }
}

