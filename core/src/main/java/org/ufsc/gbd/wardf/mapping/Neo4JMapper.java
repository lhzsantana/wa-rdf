package org.ufsc.gbd.wardf.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.neo4j.driver.v1.*;
import org.ufsc.gbd.wardf.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Neo4JMapper extends NoSQLMapper {

    private static Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "rdf"));

    private static final Log logger = LogFactory.getLog(Neo4JMapper.class);

    private Neo4JMapper(){}

    public Neo4JMapper(Partition partition){}

    @Override
    public List<Triple> query(Query subQuery) {
        return query(subQuery.getTriplePatterns());
    }

    @Override
    public void store(Fragment fragment) {

        for(Triple triple: fragment.getTriples()) {

            Node subject = triple.getSubject();
            Node predicate = triple.getPredicate();
            Node object = triple.getObject();

            try (Session session = driver.session()) {

                String statement = "CREATE (a:RDFNode { name: '" + subject.toString() + "' })" +
                        "CREATE (b:RDFNode { name: '" + object.toString() + "' })" +
                        "CREATE (a)-[r:RELTYPE { name: '" + predicate.toString() + "' }]->(b)";

                StatementResult rs = session.run(statement);
            }
        }
    }

    public List<Triple> query(Set<TriplePattern> triplePatterns) {

        List<Triple> triples = new ArrayList<>();

        return triples;
    }
}
