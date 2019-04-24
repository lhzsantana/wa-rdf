package org.ufsc.gbd.wardf.mapping;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;
import org.ufsc.gbd.wardf.model.Fragment;
import org.ufsc.gbd.wardf.model.Query;
import org.ufsc.gbd.wardf.model.Triple;
import org.ufsc.gbd.wardf.model.TriplePattern;

import java.util.ArrayList;
import java.util.List;

public class MongoDBMapper {

    static MongoClient mongoClient = MongoClients.create();
    static MongoDatabase db = mongoClient.getDatabase("wa-rdf");
    static MongoCollection<Document> triplesCollection = db.getCollection("triples");

    private static final Log logger = LogFactory.getLog(MongoDBMapper.class);

    public void store(Fragment fragment) {

        logger.info("Storing in MongoDB");

        Document documentFragment  = new Document();
        documentFragment.put("uuid", fragment.getId());

        triplesCollection.insertOne(documentFragment);
    }

    public List<Triple> query(Query subQuery) {
        return query(subQuery.getTriplePatterns());
    }

    public List<Triple> query(List<TriplePattern> triplePatterns) {
        List<Triple> triples = new ArrayList<>();

        return triples;
    }
}
