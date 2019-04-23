package org.ufsc.gbd.wardf.fragmenter;

import org.ufsc.gbd.wardf.mapping.MongoDBMapper;
import org.ufsc.gbd.wardf.mapping.Neo4JMapper;
import org.ufsc.gbd.wardf.model.Fragment;
import org.ufsc.gbd.wardf.model.Shape;
import org.ufsc.gbd.wardf.model.Triple;
import org.ufsc.gbd.wardf.model.TriplePattern;
import org.ufsc.gbd.wardf.wac.WAc;

import java.util.ArrayList;
import java.util.List;

public class Fragmenter {

    private final MongoDBMapper mongoDBMapper = new MongoDBMapper();
    private final Neo4JMapper neo4JMapper = new Neo4JMapper();

    private WAc wac = WAc.getInstance();

    public List<Fragment> fragment(Triple triple) {

        List<Fragment> fragments = new ArrayList<>();

        for(Shape shape : wac.getShape(triple)){

            List<Triple> triples = new ArrayList<>();
            triples.add(triple);
            triples.addAll(expandTriple(triple, shape));
            fragments.add(new Fragment(triple, shape, triples));
        }

        return fragments;
    }

    private List<Triple> expandTriple(Triple triple, Shape shape){

        List<Triple> triples = new ArrayList<>();

        List<TriplePattern> triplePatterns = wac.getTypicalWorkload(triple, shape);

        if (shape.equals(Shape.STAR) ) {
            List<Triple> starResponse = mongoDBMapper.query(triplePatterns);
            triples.addAll(starResponse);
        }

        if (shape.equals(Shape.CHAIN)) {
            List<Triple> chainResponse = neo4JMapper.query(triplePatterns);
            triples.addAll(chainResponse);
        }

        return triples;
    }
}
