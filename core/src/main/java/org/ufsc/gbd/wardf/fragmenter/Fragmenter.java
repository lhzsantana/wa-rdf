package org.ufsc.gbd.wardf.fragmenter;

import org.ufsc.gbd.wardf.mapping.NoSQLMapper;
import org.ufsc.gbd.wardf.model.Fragment;
import org.ufsc.gbd.wardf.model.Shape;
import org.ufsc.gbd.wardf.model.Triple;
import org.ufsc.gbd.wardf.model.TriplePattern;
import org.ufsc.gbd.wardf.partition.dictionary.DistributionDictionary;
import org.ufsc.gbd.wardf.wac.TypicalWorkload;
import org.ufsc.gbd.wardf.wac.WAc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Fragmenter {

    private WAc wac = WAc.getInstance();
    private final DistributionDictionary dictionary = new DistributionDictionary();

    public List<Fragment> fragment(Triple triple) {

        List<Fragment> fragments = new ArrayList<>();

        TypicalWorkload typicalWorkload = wac.getTypicalWorkload(triple);

        for(Shape shape : typicalWorkload.getShapes()){

            List<Triple> triples = new ArrayList<>();
            triples.add(triple);
            triples.addAll(expandTriple(shape, typicalWorkload.getTriplePatterns()));
            fragments.add(new Fragment(triple, shape, triples));
        }

        return fragments;
    }

    private List<Triple> expandTriple(Shape shape, Set<TriplePattern> triplePatterns){

        List<Triple> triples = new ArrayList<>();

        NoSQLMapper noSQLMapper = dictionary.checkDictionary(triplePatterns, shape);

        triples.addAll(noSQLMapper.query(triplePatterns));

        return triples;
    }
}
