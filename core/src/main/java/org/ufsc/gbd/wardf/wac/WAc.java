package org.ufsc.gbd.wardf.wac;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.jena.graph.Node;
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

    private WAc(){ }

    public static WAc getInstance() {
        if (instance == null) instance = new WAc();
        return instance;
    }

    public void registerWorkload(List<TriplePattern> triplePatterns, Query query){

        for(TriplePattern triplePattern : triplePatterns){
            tpMap.put(UUID.randomUUID().toString(), triplePattern);
            tp2Q.put(triplePattern, query);
            q2TP.put(query, triplePattern);
        }
    }

    public List<Shape> getShape(Triple triple) {
        return match(triple);
    }

    private List<Shape> match(Triple triple){
        List<Shape> shapes = new ArrayList<>();

        shapes.add(Shape.CHAIN);
        shapes.add(Shape.STAR);

        return shapes;
    }

    public List<TriplePattern> getTypicalWorkload(Triple triple, Shape shape) {
        return null;
    }
}
