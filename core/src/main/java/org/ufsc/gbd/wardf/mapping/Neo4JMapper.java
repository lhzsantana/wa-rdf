package org.ufsc.gbd.wardf.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.neo4j.driver.v1.*;
import org.ufsc.gbd.wardf.mapping.node.Node2Json;
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

                String statement = "CREATE (a:RDFNode { name: '" + Node2Json.mapNode(subject) + "' })" +
                        "CREATE (b:RDFNode { name: '" + Node2Json.mapNode(object) + "' })" +
                        "CREATE (a)-[r:RELTYPE { name: '" + Node2Json.mapNode(predicate) + "' }]->(b)";

                session.run(statement);
            }
        }
    }

    public List<Triple> query(Set<TriplePattern> triplePatterns) {

        List<Triple> triples = new ArrayList<>();

        for(TriplePattern triplePattern: triplePatterns) {

            Node subject = triplePattern.getSubject();
            Node predicate = triplePattern.getPredicate();
            Node object = triplePattern.getObject();

            try (Session session = driver.session()) {

                String statement = "MATCH (n:RDFNode) RETURN n.name";

                StatementResult rs = session.run(statement);

                while(rs.hasNext()){
                    System.out.println(rs.toString());
                }
            }
        }


        return triples;
    }
}
