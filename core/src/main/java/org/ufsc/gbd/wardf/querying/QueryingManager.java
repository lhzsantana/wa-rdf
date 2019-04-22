package org.ufsc.gbd.wardf.querying;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.sparql.syntax.Element;
import org.ufsc.gbd.wardf.mapping.VerticalPartitioningToCassandraMapper;
import org.ufsc.gbd.wardf.model.Query;
import org.ufsc.gbd.wardf.model.Shape;
import org.ufsc.gbd.wardf.model.Triple;
import org.ufsc.gbd.wardf.model.TriplePattern;

import java.util.ArrayList;
import java.util.List;

public class QueryingManager {

    private final static Log logger = LogFactory.getLog(QueryingManager.class);

    public List<Triple> query(Query query){

        Element element = query.getQuery().getQueryPattern();

        List<TriplePattern> triplePatterns = new ArrayList<>();






        return null;
    }

    private Shape getShape(List<TriplePattern> triplePatterns){





        return Shape.STAR;
    }
}
