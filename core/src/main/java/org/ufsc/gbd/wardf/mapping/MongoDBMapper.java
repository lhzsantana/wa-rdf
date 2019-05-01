package org.ufsc.gbd.wardf.mapping;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.ufsc.gbd.wardf.model.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MongoDBMapper extends NoSQLMapper {

    static MongoClient mongoClient = MongoClients.create();
    static MongoDatabase db = mongoClient.getDatabase("wa-rdf");
    static MongoCollection<Document> triplesCollection = db.getCollection("triples");

    private static final Log logger = LogFactory.getLog(MongoDBMapper.class);

    private MongoDBMapper(){}

    public MongoDBMapper(Partition partition){}

    @Override
    public void store(Fragment fragment) {

        logger.info("Storing in MongoDB");

        Document documentFragment  = new Document();
        documentFragment.put("uuid", fragment.getId());

        documentFragment.put("predicate", fragment.getCoreTriple().getPredicate().toString());

        for(Triple triple:fragment.getTriples()){

            Document subjectSubDocument  = new Document();
            documentFragment.put("subject", subjectSubDocument);
        }

        System.out.println(documentFragment.toJson());

        triplesCollection.insertOne(documentFragment);
    }

    @Override
    public List<Triple> query(Query subQuery) {
        return query((StarQuery) subQuery);
    }

    @Override
    public List<Triple> query(Set<TriplePattern> triplePatterns) {
        return null;
    }

    public List<Triple> query(StarQuery subQuery) {
        List<Triple> triples = new ArrayList<>();

        Document documentFragment  = new Document();

        documentFragment.put("subject", "");


        for(TriplePattern triplePattern:subQuery.getTriplePatterns()){

            Document subjectSubDocument  = new Document();
            documentFragment.put("subject", triplePattern.getSubject().toString());
            documentFragment.put("predicate", triplePattern.getPredicate().toString());
            documentFragment.put("object", triplePattern.getObject().toString());

            documentFragment.put("subdocument", subjectSubDocument);
        }

        System.out.println(documentFragment.toJson());

        Iterator cursor = triplesCollection.find(documentFragment).iterator();
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }

        return triples;
    }
}
