package org.ufsc.gbd.wardf.mapping;

import com.datastax.oss.driver.api.core.CqlSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;

public class HierarchicalToCassandraMapper {

    final static Log logger = LogFactory.getLog(HierarchicalToCassandraMapper.class);

    private String keyspace = "triplestore";

    final CqlSession session = CqlSession.builder().withKeyspace(keyspace).build();

    public HierarchicalToCassandraMapper(){
        createTableSPO();
        createTableOPS();
    }

    protected void store(Statement stmt) {

        RDFNode subject = stmt.getSubject();
        RDFNode predicate = stmt.getPredicate();
        RDFNode object = stmt.getObject();

        storeTripleSPO(subject, predicate, object);
        storeTripleOPS(subject, predicate, object);
    }

    private void createTableSPO(){
        try {
            session.execute("DROP TABLE spo");

        }catch (Exception e){

        }
        try {
            String query = "CREATE TABLE spo ( "
                    + "subject text, "
                    + "object text, PRIMARY KEY (subject))";
            session.execute(query);
        }catch (Exception e){

        }
    }

    private void createTableOPS(){
        try {
            session.execute("DROP TABLE ops");

        }catch (Exception e){

        }
        try {
            String query = "CREATE TABLE ops ( "
                    + "subject text, "
                    + "object text, PRIMARY KEY (object))";
            session.execute(query);
        }catch (Exception e){}
    }

    private void storeTripleSPO(RDFNode subject, RDFNode predicate, RDFNode object) {

        try {
            String createColumn = "ALTER TABLE spo ADD (" + predicate.asResource().getLocalName().toLowerCase() + " text)";
            session.execute(createColumn);
        }catch (Exception e){}

        String query = "INSERT INTO spo (subject, " + predicate.asResource().getLocalName().toLowerCase() + ") " +
                " VALUES ('"+subject.toString()+"','"+object.toString()+"')";

        session.execute(query);
    }

    private void storeTripleOPS(RDFNode subject, RDFNode predicate, RDFNode object) {

        try {
            String createColumn = "ALTER TABLE ops ADD (" + predicate.asResource().getLocalName().toLowerCase() + " text)";
            session.execute(createColumn);
        }catch (Exception e){}

        String query = "INSERT INTO ops (object, " + predicate.asResource().getLocalName().toLowerCase() + ") " +
                 " VALUES ('"+object.toString()+"','"+subject.toString()+"')";

        session.execute(query);
    }
}
