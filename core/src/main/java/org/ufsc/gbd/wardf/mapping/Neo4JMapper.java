package org.ufsc.gbd.wardf.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.neo4j.driver.v1.*;
import org.ufsc.gbd.wardf.model.Partition;
import org.ufsc.gbd.wardf.model.Query;
import org.ufsc.gbd.wardf.model.Triple;
import org.ufsc.gbd.wardf.model.TriplePattern;

import java.util.ArrayList;
import java.util.List;

public class Neo4JMapper extends NoSQLMapper {

    private static final Log logger = LogFactory.getLog(Neo4JMapper.class);

    private Neo4JMapper(){}

    public Neo4JMapper(Partition partition){}

    public void store(Statement stmt) {
        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "rdf"));

        RDFNode subject = stmt.getSubject();
        RDFNode predicate = stmt.getPredicate();
        RDFNode object = stmt.getObject();

        try (Session session = driver.session()) {

            String statement = "CREATE (a:RDFNode { name: '"+subject.toString()+"' })" +
                    "CREATE (b:RDFNode { name: '"+object.toString()+"' })" +
                    "CREATE (a)-[r:RELTYPE { name: '"+predicate.toString()+"' }]->(b)";

            StatementResult rs = session.run(statement);
        }

    }

    public void store(List<Triple> triples) {

        logger.info("Storing in Neo4J");
        for(Triple triple:triples){
            //store(triple.getStatement());
        }
    }

    public List<Triple> query(Query subQuery) {
        return query(subQuery.getTriplePatterns());
    }

    public List<Triple> query(List<TriplePattern> triplePatterns) {

        List<Triple> triples = new ArrayList<>();

        return triples;
    }
}
