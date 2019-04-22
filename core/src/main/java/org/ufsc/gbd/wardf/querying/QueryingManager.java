package org.ufsc.gbd.wardf.querying;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.sparql.syntax.Element;
import org.ufsc.gbd.wardf.mapping.VerticalPartitioningToCassandraMapper;
import org.ufsc.gbd.wardf.model.Query;
import org.ufsc.gbd.wardf.model.Triple;

import java.util.List;

public class QueryingManager {

    private final static Log logger = LogFactory.getLog(QueryingManager.class);

    public List<Triple> query(Query query){

        Element element = query.getQuery().getQueryPattern();

        System.out.println(element.toString());



        return null;
    }
}
