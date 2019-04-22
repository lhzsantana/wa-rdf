package org.ufsc.gbd.wardf.mapping;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PreProcessedToCassandraMapper {

    private String keyspace;
    private CqlSession session;
    private VerticalPartitioningToCassandraMapper verticalPartitioningToCassandraMapper;

    public PreProcessedToCassandraMapper(String keyspace){
        this.keyspace=keyspace;
        this.session = CqlSession.builder().withKeyspace(keyspace).build();
        this.verticalPartitioningToCassandraMapper = new VerticalPartitioningToCassandraMapper(keyspace);
    }

    final static Log logger = LogFactory.getLog(PreProcessedToCassandraMapper.class);

    protected void store(Statement stmt) {

        verticalPartitioningToCassandraMapper.store(stmt);

        RDFNode subject = stmt.getSubject();
        RDFNode predicate = stmt.getPredicate();
        RDFNode object = stmt.getObject();

        preComputeJoin(subject, predicate, object);
    }

    private void preComputeJoin(RDFNode subject, RDFNode predicate, RDFNode object){

        List<String> tables = getAllTables();

        for(String table:tables){

            String query = "SELECT * FROM "+table;
            ResultSet rs = session.execute(query);

            Iterator it = rs.iterator();
            while (it.hasNext()){
                Row row = (Row) it.next();

                String persistedSubject = row.getString("subject");
                String persistedObject = row.getString("object");

                if(object.toString().equals(persistedSubject)){
                    extVPOS(table, subject, predicate, object);
                }
                if(subject.toString().equals(persistedObject)){
                    extVPSO(table, subject, predicate, object);
                }
                if(subject.toString().equals(persistedSubject)){
                    extVPSS(table, subject, predicate, object);
                }
            }
        }
    }

    private void extVPOS(String table, RDFNode subject, RDFNode predicate, RDFNode object){

        String tableName = predicate.asResource().getLocalName().toLowerCase()+table+"_os";

        persistExtended(tableName, subject, predicate, object);
    }

    private void extVPSO(String table, RDFNode subject, RDFNode predicate, RDFNode object){

        String tableName = predicate.asResource().getLocalName().toLowerCase()+table+"_so";

        persistExtended(tableName, subject, predicate, object);
    }

    private void extVPSS(String table, RDFNode subject, RDFNode predicate, RDFNode object){

        String tableName = predicate.asResource().getLocalName().toLowerCase()+table+"_ss";

        persistExtended(tableName, subject, predicate, object);
    }

    private void persistExtended(String tableName, RDFNode subject, RDFNode predicate, RDFNode object){

        if(!checkIfTableExists(tableName)){
            createTable(tableName);
        }

        String query = "INSERT INTO "+tableName+" (subject, object) " +
                "  VALUES ('"+subject.toString()+"','"+object.toString()+"')";

        session.execute(query);
    }

    private void createTable(String tableName){

        String query = "CREATE TABLE "+tableName+" ( "
                + "subject text, "
                + "object text, PRIMARY KEY (subject, object))";

        session.execute(query);
    }

    private boolean checkIfTableExists(String tableName){

        String query = "SELECT table_name " +
                "FROM system_schema.tables WHERE keyspace_name='"+keyspace+"' AND table_name='"+tableName+"';";

        ResultSet rs = session.execute(query);
        if(rs.one()!=null) return true;

        return false;
    }


    private List<String> getAllTables(){

        String query = "SELECT table_name FROM system_schema.tables WHERE keyspace_name='"+keyspace+"';";

        List<String> tables = new ArrayList<>();

        ResultSet rs = session.execute(query);
        Iterator it = rs.iterator();
        while (it.hasNext()){

            String tableName = ((Row) it.next()).getString(0);

            if(tableName.contains("_ss") || tableName.contains("_os") || tableName.contains("_so")) continue;

            tables.add(tableName);
        }

        return tables;
    }
}
