package org.ufsc.gbd.wardf.mapping;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;

public class VerticalPartitioningToCassandraMapper {

    private String keyspace;
    private CqlSession session;

    public VerticalPartitioningToCassandraMapper(String keyspace){
        this.keyspace=keyspace;
        this.session = CqlSession.builder().withKeyspace(keyspace).build();
    }

    final static Log logger = LogFactory.getLog(VerticalPartitioningToCassandraMapper.class);

    protected void store(Statement stmt) {

        RDFNode subject = stmt.getSubject();
        RDFNode predicate = stmt.getPredicate();
        RDFNode object = stmt.getObject();

        storeTriple(subject, predicate, object);
    }

    private boolean checkIfTableExists(RDFNode predicate){

        String query = "SELECT table_name " +
                "FROM system_schema.tables WHERE keyspace_name='"+keyspace+"' AND table_name='"+predicate.asResource().getLocalName().toLowerCase()+"';";

        ResultSet rs = session.execute(query);
        if(rs.one()!=null) return true;

        return false;
    }

    private void createTable(RDFNode predicate){

        String query = "CREATE TABLE "+predicate.asResource().getLocalName().toLowerCase()+" ( "
                + "subject text, "
                + "object text, PRIMARY KEY (subject, object))";

        logger.info(query);
        session.execute(query);
    }

    private void storeTriple(RDFNode subject, RDFNode predicate, RDFNode object) {

        if(!checkIfTableExists(predicate)){
            createTable(predicate);
        }

        String query = "INSERT INTO "+predicate.asResource().getLocalName().toLowerCase()+" (subject, object) " +
                "  VALUES ('"+subject.toString()+"','"+object.toString()+"')";

        session.execute(query);
    }

}
