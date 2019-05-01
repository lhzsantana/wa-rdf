package org.ufsc.gbd.wardf.wac;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.core.Var;
import org.ufsc.gbd.wardf.model.Query;
import org.ufsc.gbd.wardf.model.Shape;
import org.ufsc.gbd.wardf.model.Triple;
import org.ufsc.gbd.wardf.model.TriplePattern;

import java.util.*;

public class WAc {

    private static WAc instance = null;
    private static Map<String, TriplePattern> tpMap = new HashMap<>();
    private static Multimap<TriplePattern, Query> tp2Q = ArrayListMultimap.create();
    private static Multimap<Query, TriplePattern> q2TP = ArrayListMultimap.create();
    private static Multimap<String, Query> s2Q = ArrayListMultimap.create();
    private static Multimap<String, Query> p2Q = ArrayListMultimap.create();
    private static Multimap<String, Query> o2Q = ArrayListMultimap.create();

    private WAc(){ }

    public static WAc getInstance() {
        if (instance == null) instance = new WAc();
        return instance;
    }

    public void registerWorkload(Set<TriplePattern> triplePatterns, Query query){

        for(TriplePattern triplePattern : triplePatterns){
            tpMap.put(UUID.randomUUID().toString(), triplePattern);
            tp2Q.put(triplePattern, query);
            q2TP.put(query, triplePattern);

            if(!triplePattern.getSubject().isVariable()) s2Q.put(getIdentifier(triplePattern.getSubject()), query);
            if(!triplePattern.getPredicate().isVariable()) p2Q.put(getIdentifier(triplePattern.getPredicate()), query);
            if(!triplePattern.getObject().isVariable()) o2Q.put(getIdentifier(triplePattern.getObject()), query);
        }
    }

    private String getIdentifier(Node node){
        if(node.isVariable()){
            return node.getName();
        }
        if(node.isURI()){
            return node.getURI();
        }
        if(node.isLiteral()){
            return node.getLiteral().getValue().toString();
        }
        if(node.isBlank()){
            return "";
        }

        return "";
    }

    public TypicalWorkload getTypicalWorkload(Triple triple){
        Set<Shape> shapes = new HashSet<>();
        Set<TriplePattern> triplePatterns = new HashSet<>();

        List<Query> queries = new ArrayList<>();

        queries.addAll(s2Q.get(getIdentifier(triple.getSubject())));
        queries.addAll(p2Q.get(getIdentifier(triple.getPredicate())));
        queries.addAll(o2Q.get(getIdentifier(triple.getObject())));

        for(Query query:queries){
            shapes.add(query.getShape());
            triplePatterns.addAll(query.getTriplePatterns());
        }

        TypicalWorkload typicalWorkload = new TypicalWorkload();
        typicalWorkload.setShapes(shapes);
        typicalWorkload.setTriplePatterns(triplePatterns);

        return typicalWorkload;
    }

}
