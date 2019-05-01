package org.ufsc.gbd.wardf.querying;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.graph.Node;
import org.ufsc.gbd.wardf.cache.Cache;
import org.ufsc.gbd.wardf.mapping.NoSQLMapper;
import org.ufsc.gbd.wardf.model.*;
import org.ufsc.gbd.wardf.partition.dictionary.DistributionDictionary;
import org.ufsc.gbd.wardf.wac.WAc;

import java.util.*;

public class QueryingManager {

    private final static Log logger = LogFactory.getLog(QueryingManager.class);

    private final WAc wac = WAc.getInstance();
    private final Cache cache = new Cache();

    private final DistributionDictionary dictionary = new DistributionDictionary();

    public Set<Triple> query(Query query){

        Set<Triple> response = new HashSet<>();

        for(Query subQuery:getSubQueries(query)) {

            Set<TriplePattern> triplePatterns = subQuery.getTriplePatterns();
            Shape shape = subQuery.getShape();

            registerWorkload(shape, triplePatterns, subQuery);

            Set<Triple> cacheResponse = cache.checkCache(triplePatterns);

            if(cacheResponse!=null){
                response.addAll(cacheResponse);
            }else{
                NoSQLMapper noSQLMapper = getNoSQLMapper(triplePatterns, subQuery.getShape());
                List<Triple> starResponse = noSQLMapper.query(subQuery);
                response.addAll(starResponse);
                cache(subQuery, starResponse);
            }
        }

        return response;
    }

    public NoSQLMapper getNoSQLMapper(Set<TriplePattern> triplePatterns, Shape shape){
        return dictionary.checkDictionary(triplePatterns, shape);
    }

    private void registerWorkload(Shape shape, Set<TriplePattern> triplePatterns, Query query){
        new Thread(() -> {
            wac.registerWorkload(triplePatterns, query);
        }).start();
    }

    private void cache(Query subquery, List<Triple> response){
        new Thread(() -> {
            //cache.store(subquery, response);
        }).start();
    }

    private List<Query> getSubQueries(Query query){

        long startTime = System.nanoTime();

        Multimap<Node, TriplePattern> subjectStars = ArrayListMultimap.create();
        Multimap<Node, TriplePattern> objectStars = ArrayListMultimap.create();
        Set<Node> objects = new HashSet<>();
        Set<Node> subjects = new HashSet<>();

        for(TriplePattern triplePattern : query.getTriplePatterns()){
            subjectStars.put(triplePattern.getObject(), triplePattern);
            objectStars.put(triplePattern.getSubject(), triplePattern);
            objects.add(triplePattern.getObject());
            subjects.add(triplePattern.getSubject());
        }

        List<Query> subQueries = getSubQueriesStar(subjectStars);
        subQueries.addAll(getSubQueriesStar(objectStars));
        subQueries.addAll(getSubQueriesChain(query, objects, objectStars));
        subQueries.addAll(getSubQueriesSimple(query, subQueries));

        long estimatedTime = System.nanoTime() - startTime;

        logger.debug("Parser time " + estimatedTime);

        return subQueries;
    }

    private Collection<? extends Query> getSubQueriesChain(Query query, Set<Node> objects, Multimap<Node, TriplePattern> objectStars) {

        List<Query> subQueries = new ArrayList<>();

        for(TriplePattern triplePattern:query.getTriplePatterns()){
            if(!objects.contains(triplePattern.getSubject())){
                subQueries.addAll(getSubQueriesChain(query.getTriplePatterns().size(), triplePattern, objectStars));
            }
        }

        return subQueries;
    }

    private List<Query> getSubQueriesChain(Integer numberTriplePatterns, TriplePattern firstTriplePattern, Multimap<Node, TriplePattern> objectStars){

        List<List<TriplePattern>> triplePatternsList = getTriplePatternList(numberTriplePatterns, firstTriplePattern, objectStars);

        List<Query> subQueries = new ArrayList<>();

        for(List<TriplePattern> triplePattern : triplePatternsList) {
            if(triplePattern.size()>2) {
                ChainQuery subQuery = new ChainQuery();
                subQuery.setChain(triplePattern);
                subQuery.setShape(Shape.CHAIN);
                subQueries.add(subQuery);
            }
        }

        return subQueries;
    }

    private List<List<TriplePattern>> getTriplePatternList(Integer numberTriplePatterns, TriplePattern triplePattern, Multimap<Node, TriplePattern> objectStars) {

        List<List<TriplePattern>> newTriplePatternsList = new ArrayList<>();
        List<List<TriplePattern>> oldTriplePatternsList = new ArrayList<>();

        List<TriplePattern> chain = new ArrayList<>();
        chain.add(triplePattern);
        oldTriplePatternsList.add(chain);

        for(int i=0;i<numberTriplePatterns;i++){

            for(List<TriplePattern> currentChain:oldTriplePatternsList){

                for(TriplePattern currentTriplePattern:objectStars.get(currentChain.get(currentChain.size()-1).getObject())){
                    List<TriplePattern> newChain = new ArrayList<>();
                    newChain.addAll(currentChain);
                    newChain.add(currentTriplePattern);
                    newTriplePatternsList.add(newChain);
                }
            }

            if(newTriplePatternsList.isEmpty()) break;

            oldTriplePatternsList = new ArrayList<>();
            oldTriplePatternsList.addAll(newTriplePatternsList);
            newTriplePatternsList = new ArrayList<>();
        }

        return oldTriplePatternsList;
    }

    private List<Query> getSubQueriesStar(Multimap<Node, TriplePattern> stars){

        List<Query> subQueries = new ArrayList<>();

        for(Node node:stars.keySet()){
            Collection<TriplePattern> triplePatterns = stars.get(node);

            if(triplePatterns.size()>2){
                StarQuery subQuery = new StarQuery();
                subQuery.setTriplePatterns(new HashSet<>(triplePatterns));
                subQuery.setShape(Shape.STAR);
                subQuery.setCenter(node);
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

                Set<TriplePattern> simpleTriplePatterns = new HashSet<>();
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
