package org.ufsc.gbd.wardf.querying;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.engine.optimizer.reorder.PatternTriple;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;
import org.ufsc.gbd.wardf.mapping.VerticalPartitioningToCassandraMapper;
import org.ufsc.gbd.wardf.model.Query;
import org.ufsc.gbd.wardf.model.Shape;
import org.ufsc.gbd.wardf.model.TriplePattern;
import org.ufsc.gbd.wardf.wac.WAc;

import java.util.*;

public class QueryingManager {

    private final static Log logger = LogFactory.getLog(QueryingManager.class);

    private final WAc wac = new WAc();

    public Set<Triple> query(Query query){

        Set<Triple> response = new HashSet<>();

        for(Query subQuery:getSubQueries(query)) {

            List<TriplePattern> triplePatterns = subQuery.getTriplePatterns();

            Shape shape = subQuery.getShape();

            wac.registerWorkload(shape, triplePatterns, query);

            if (shape.equals(Shape.CHAIN)) {

            }

            if (shape.equals(Shape.STAR)) {

            }

            if (shape.equals(Shape.SIMPLE)) {

            }
        }

        return response;
    }

    private List<Query> getSubQueries(Query query){

        Multimap<Node, TriplePattern> subjectStars = ArrayListMultimap.create();
        Multimap<Node, TriplePattern> objectStars = ArrayListMultimap.create();

        for(TriplePattern triplePattern : query.getTriplePatterns()){
            subjectStars.put(triplePattern.getObject(), triplePattern);
            objectStars.put(triplePattern.getSubject(), triplePattern);
        }

        List<Query> subQueries = getSubQueriesStar(subjectStars);
        subQueries.addAll(getSubQueriesStar(objectStars));
        subQueries.addAll(getSubQueriesChain(query, subjectStars, objectStars));
        subQueries.addAll(getSubQueriesSimple(query, subQueries));

        return subQueries;
    }

    private List<Query> getSubQueriesChain(Query query, Multimap<Node, TriplePattern> subjects, Multimap<Node, TriplePattern> objects){

        List<List<TriplePattern>> triplePatternsList = new ArrayList<>();

        for(TriplePattern triplePattern:query.getTriplePatterns()){

            //chain is always on the border
            if(subjects.get(triplePattern.getObject()).isEmpty() || objects.get(triplePattern.getSubject()).isEmpty()){
                triplePatternsList.add(getChain(triplePattern, subjects, objects));
            }
        }

        List<Query> subQueries = new ArrayList<>();

        for(List<TriplePattern> triplePattern : triplePatternsList) {
            if(triplePattern.size()>2) {
                Query subQuery = new Query();
                subQuery.setTriplePatterns(new ArrayList<>(triplePattern));
                subQuery.setShape(Shape.CHAIN);
                subQueries.add(subQuery);
            }
        }

        return subQueries;
    }

    private List<TriplePattern> getChain(TriplePattern triplePattern, Multimap<Node, TriplePattern> subjects, Multimap<Node, TriplePattern> objects){

        List<TriplePattern> chains = new ArrayList<>();

        chains.add(triplePattern);

        for(TriplePattern triplePatternSubjects: subjects.get(triplePattern.getObject())){
            chains.addAll(getChain(triplePatternSubjects, subjects, objects));
        }

        for(TriplePattern triplePatternSubjects: subjects.get(triplePattern.getObject())){
            chains.addAll(getChain(triplePatternSubjects, subjects, objects));
        }

        return chains;
    }

    private List<Query> getSubQueriesStar(Multimap<Node, TriplePattern> stars){

        List<Query> subQueries = new ArrayList<>();

        for(Node node:stars.keySet()){
            Collection<TriplePattern> triplePatterns = stars.get(node);

            if(triplePatterns.size()>2){
                Query subQuery = new Query();
                subQuery.setTriplePatterns(new ArrayList<>(triplePatterns));
                subQuery.setShape(Shape.STAR);
                subQueries.add(subQuery);
            }
        }

        return subQueries;
    }

    private List<Query> getSubQueriesSimple(Query query, List<Query> subQueries){

        Set<TriplePattern> triplePatterns = new HashSet<>();

        for(Query subQuery:subQueries){
            triplePatterns.addAll(subQuery.getTriplePatterns());
        }

        List<Query> simpleSubQueries = new ArrayList<>();

        for(TriplePattern triplePattern:query.getTriplePatterns()){
            if(!triplePatterns.contains(triplePattern)){

                List<TriplePattern> simpleTriplePatterns = new ArrayList<>();
                simpleTriplePatterns.add(triplePattern);

                Query subQuery = new Query();
                subQuery.setTriplePatterns(simpleTriplePatterns);
                subQuery.setShape(Shape.SIMPLE);
                simpleSubQueries.add(subQuery);
            }
        }

        return simpleSubQueries;
    }
}
